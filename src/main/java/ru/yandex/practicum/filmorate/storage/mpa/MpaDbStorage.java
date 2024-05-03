package ru.yandex.practicum.filmorate.storage.mpa;

import lombok.AllArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.InvalidMpaException;
import ru.yandex.practicum.filmorate.model.MPA;

import java.util.List;

@Component
@AllArgsConstructor
public class MpaDbStorage implements MpaStorage {
    private JdbcTemplate jdbcTemplate;

    @Override
    public MPA getMpa(Long id) {
        try {
            return jdbcTemplate.queryForObject("select * from mpa where id = ?", mpaRowMapper(), id);
        } catch (EmptyResultDataAccessException e) {
            throw new InvalidMpaException("Рейтинг не найден");
        }
    }

    @Override
    public List<MPA> getAll() {
        return jdbcTemplate.query("select * from mpa ORDER BY id", mpaRowMapper());
    }

    private RowMapper<MPA> mpaRowMapper() {
        return (rs, rowNum) -> new MPA(rs.getLong("id"), rs.getString("name"));
    }
}
