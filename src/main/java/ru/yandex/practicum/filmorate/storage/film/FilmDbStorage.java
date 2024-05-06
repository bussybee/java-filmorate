package ru.yandex.practicum.filmorate.storage.film;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;

import java.util.List;

@Component
@Qualifier(("db"))
@AllArgsConstructor
@Slf4j
public class FilmDbStorage implements FilmStorage {
    private JdbcTemplate jdbcTemplate;

    @Override
    public Film createFilm(Film film) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("films")
                .usingGeneratedKeyColumns("id");

        Long id = simpleJdbcInsert.executeAndReturnKey(film.toMap()).longValue();
        film.setId(id);
        log.info("Создали новый фильм с id = {}", id);

        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        String sql = "update films set name = ?, description = ?, release_date = ?, duration = ?, mpa_id = ? where id = ?";
        int rowCount = jdbcTemplate.update(sql, film.getName(), film.getDescription(), film.getReleaseDate(),
                film.getDuration(), film.getMpa().getId(), film.getId());

        if (rowCount == 0) {
            throw new FilmNotFoundException(String.format("Фильм с id=%s не найден", film.getId()));
        }
        log.info("Обновили фильм с id={}", film.getId());
        return film;
    }

    @Override
    public List<Film> getAllFilms() {
        List<Film> allFilms = jdbcTemplate.query("select f.id as film_id, f.name as film_name, description, release_date, duration, " +
                "m.id as mpa_id, m.name as mpa_name, fg.genre_id as genre_id, g.name as genre_name " +
                "from films f " +
                "join mpa m on m.id = f.mpa_id " +
                "left join film_genres fg on fg.film_id = f.id " +
                "left join genres g on g.id = fg.genre_id", filmRowMapper());
        log.info("Получили список всех фильмов");
        return allFilms;
    }

    @Override
    public Film getFilm(Long id) {
        try {
            String sql = "select f.id as film_id, f.name as film_name, description, release_date, duration, " +
                    "m.id as mpa_id, m.name as mpa_name, fg.genre_id as genre_id, g.name as genre_name " +
                    "from films f " +
                    "left join mpa m on m.id = f.mpa_id " +
                    "left join film_genres fg on fg.film_id = f.id " +
                    "left join genres g on g.id = fg.genre_id " +
                    "where f.id = ?";
            Film film = jdbcTemplate.queryForObject(sql, filmRowMapper(), id);
            log.info("Вернули фильм с id={}", id);
            return film;
        } catch (EmptyResultDataAccessException e) {
            throw new FilmNotFoundException(String.format("Фильм с id=%s не найден", id));
        }
    }

    @Override
    public List<Film> getPopularFilms(Integer count) {
        String sql = "SELECT COUNT(USER_ID) AS QUANTITY, F.ID AS film_id, F.NAME AS film_name, F.DESCRIPTION, " +
                "F.RELEASE_DATE, F.DURATION " +
                "FROM LIKES " +
                "RIGHT JOIN FILMS F ON F.ID = LIKES.FILM_ID " +
                "GROUP BY F.ID " +
                "ORDER BY QUANTITY DESC " +
                "LIMIT ?";
        List<Film> popularFilms = jdbcTemplate.query(sql, filmRowMapperWithoutMpa(), count);
        log.info("Получили список популярных фильмов");
        return popularFilms;
    }

    private RowMapper<Film> filmRowMapper() {
        return (rs, rowNum) -> {
            Film film = new Film();
            film.setId(rs.getLong("film_id"));
            film.setName(rs.getString("film_name"));
            film.setDescription(rs.getString("description"));
            film.setReleaseDate(rs.getDate("release_date").toLocalDate());
            long duration = rs.getLong("duration");
            if (duration > 1000) { // Если продолжительность в секундах
                film.setDuration(duration / 60);
            } else {
                film.setDuration(duration);
            }
            long mpaId = rs.getLong("mpa_id");
            if (mpaId != 0) {
                MPA mpa = new MPA(mpaId, rs.getString("mpa_name"));
                film.setMpa(mpa);
            }

            do {
                long genreId = rs.getLong("genre_id");
                if (genreId != 0) {
                    Genre genre = new Genre(genreId, rs.getString("genre_name"));
                    film.getGenres().add(genre);
                }
            } while (rs.next());
            return film;
        };
    }

    private RowMapper<Film> filmRowMapperWithoutMpa() {
        return (rs, rowNum) -> {
            Film film = new Film();
            film.setId(rs.getLong("id"));
            film.setName(rs.getString("name"));
            film.setDescription(rs.getString("description"));
            film.setReleaseDate(rs.getDate("release_date").toLocalDate());
            film.setDuration(rs.getLong("duration"));
            return film;
        };
    }
}
