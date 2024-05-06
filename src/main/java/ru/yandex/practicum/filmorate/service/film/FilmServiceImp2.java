package ru.yandex.practicum.filmorate.service.film;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.InvalidGenreException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.service.genre.GenreService;
import ru.yandex.practicum.filmorate.service.mpa.MpaService;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Qualifier("db")
@AllArgsConstructor
@Slf4j
public class FilmServiceImp2 implements FilmService {
    @Qualifier("db")
    private FilmStorage filmStorage;
    private MpaService mpaService;
    private GenreService genreService;
    private JdbcTemplate jdbcTemplate;

    @Override
    public Film createFilm(Film film) {
        MPA mpa = film.getMpa();
        if (mpa != null) {
            mpaService.getMpa(mpa.getId());
        }

        Film createdFilm = filmStorage.createFilm(film);

        List<Genre> genres = film.getGenres();
        List<Long> genreIds = genres.stream()
                .map(Genre::getId)
                .collect(Collectors.toList());

        List<Long> existingGenreIds = genreService.getAll().stream()
                .map(Genre::getId)
                .collect(Collectors.toList());

        if (existingGenreIds.containsAll(genreIds)) {
            jdbcTemplate.batchUpdate("INSERT INTO film_genres (film_id, genre_id) VALUES (?, ?)",
                    genres, genres.size(),
                    (ps, argument) -> {
                        ps.setLong(1, createdFilm.getId());
                        ps.setLong(2, argument.getId());
                    });
        } else {
            List<Long> invalidGenreIds = genreIds.stream()
                    .filter(id -> !existingGenreIds.contains(id))
                    .collect(Collectors.toList());
            throw new InvalidGenreException("Некорректные жанры: " + invalidGenreIds);
        }

        return createdFilm;
    }

    @Override
    public Film updateFilm(Film film) {
        MPA mpa = film.getMpa();
        if (mpa != null) {
            mpaService.getMpa(mpa.getId());
        }
        return filmStorage.updateFilm(film);
    }

    @Override
    public List<Film> getAllFilms() {
        return filmStorage.getAllFilms();
    }

    @Override
    public Film getFilm(Long id) {
        return filmStorage.getFilm(id);
    }

    @Override
    public void putLike(Long filmId, Long userId) {
        String sql = "insert into LIKES (FILM_ID, USER_ID) VALUES ( ?, ? )";
        jdbcTemplate.update(sql, filmId, userId);
    }

    @Override
    public void deleteLike(Long filmId, Long userId) {
        String sql = "delete from LIKES where FILM_ID = ? AND USER_ID = ?";
        jdbcTemplate.update(sql, filmId, userId);
    }

    @Override
    public List<Film> getPopularFilms(Integer count) {
        return filmStorage.getPopularFilms(count);
    }
}
