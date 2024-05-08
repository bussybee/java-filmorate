package ru.yandex.practicum.filmorate.storage.genre;

import lombok.AllArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.InvalidGenreException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

@Component
@AllArgsConstructor
public class GenreDbStorage implements GenreStorage {
    private JdbcTemplate jdbcTemplate;

    @Override
    public Genre getGenre(Long id) {
        try {
            return jdbcTemplate.queryForObject("select * from genres where id = ?", genreRowMapper(), id);
        } catch (EmptyResultDataAccessException e) {
            throw new InvalidGenreException("Жанр не найден");
        }
    }

    @Override
    public List<Genre> getAll() {
        return jdbcTemplate.query("select * from genres ORDER BY id", genreRowMapper());
    }

    private RowMapper<Genre> genreRowMapper() {
        return (rs, rowNum) -> new Genre(rs.getLong("id"), rs.getString("name"));
    }
}
