package ru.yandex.practicum.filmorate.storage.user;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

@Component
@Qualifier("db")
@AllArgsConstructor
@Slf4j
public class UserDbStorage implements UserStorage {
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<User> getAll() {
        List<User> allUsers = jdbcTemplate.query("select * from users", userRowMapper());
        log.info("Получили список всех пользователей");
        return allUsers;
    }

    @Override
    public User create(User user) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");

        Long id = simpleJdbcInsert.executeAndReturnKey(user.toMap()).longValue();
        user.setId(id);
        log.info("Создали пользователя с id = {}", id);

        return user;
    }

    @Override
    public User update(User user) {
        String sql = "update users set email = ?, login = ?, name = ?, birthday = ? where id = ?";
        int rowCount = jdbcTemplate.update(sql, user.getEmail(), user.getLogin(),
                user.getName(), user.getBirthday(), user.getId());

        if (rowCount == 0) {
            throw new UserNotFoundException(String.format("Пользователь с id=%s не найден", user.getId()));
        }
        return user;
    }

    @Override
    public User getUser(Long id) {
        try {
            return jdbcTemplate.queryForObject("select * from users where id = ?", userRowMapper(), id);
        } catch (EmptyResultDataAccessException e) {
            throw new UserNotFoundException(String.format("Пользователь с id=%s не найден", id));
        }
    }

    private RowMapper<User> userRowMapper() {
        return (rs, rowNum) -> new User(rs.getLong("id"), rs.getString("email"),
                rs.getString("login"), rs.getString("name"),
                rs.getDate("birthday").toLocalDate());
    }
}
