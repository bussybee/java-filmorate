package ru.yandex.practicum.filmorate.service.user;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;

@Service
@Qualifier("db")
@Slf4j
@AllArgsConstructor
public class UserDbService implements UserService {
    @Qualifier("db")
    private UserStorage userStorage;
    private JdbcTemplate jdbcTemplate;

    @Override
    public User create(User user) {
        return userStorage.create(user);
    }

    @Override
    public User update(User user) {
        return userStorage.update(user);
    }

    @Override
    public List<User> getAllUsers() {
        return userStorage.getAll();
    }

    @Override
    public User getUser(Long id) {
        return userStorage.getUser(id);
    }

    @Override
    public void addToFriends(Long userId, Long friendId) {
        userStorage.getUser(userId);
        userStorage.getUser(friendId);
        String sql = "insert into friends (USER_ID, FRIEND_ID) values (?, ?)";
        jdbcTemplate.update(sql, userId, friendId);
    }

    @Override
    public void deleteFriend(Long userId, Long friendId) {
        userStorage.getUser(userId);
        userStorage.getUser(friendId);
        String sql = "delete from friends where user_id = ?";
        jdbcTemplate.update(sql, userId);
    }

    @Override
    public List<User> getFriends(Long id) {
        userStorage.getUser(id);
        List<User> friends = jdbcTemplate.query("select * from users " +
                "u join PUBLIC.FRIENDS F on u.ID = F.FRIEND_ID" +
                " where user_id = ?", (rs, rowNum) ->
                new User(rs.getLong("id"), rs.getString("email"),
                        rs.getString("login"), rs.getString("name"),
                        rs.getDate("birthday").toLocalDate()), id);
        log.info("Получили список друзей пользователя с id={}", id);
        return friends;
    }

    @Override
    public List<User> getCommonFriends(Long userId, Long otherId) {
        String sql = "SELECT u.* FROM users u " +
                "JOIN friends f ON u.id = f.friend_id " +
                "WHERE f.user_id = ? AND f.friend_id IN" +
                "(SELECT friend_id FROM friends WHERE user_id = ?)";
        List<User> commonFriends = jdbcTemplate.query(sql, (rs, rowNum) ->
                new User(rs.getLong("id"), rs.getString("email"),
                        rs.getString("login"), rs.getString("name"),
                        rs.getDate("birthday").toLocalDate()), userId, otherId);
        log.info("Вывели список общих друзей у пользователей с id={}, {}", userId, otherId);
        return commonFriends;
    }
}
