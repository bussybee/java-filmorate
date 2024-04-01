package ru.yandex.practicum.filmorate.service.user;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class UserServiceImp implements UserService {
    private UserStorage userStorage;

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
        User user = userStorage.getUser(userId);
        User friend = userStorage.getUser(friendId);

        user.getFriends().add(friendId);
        friend.getFriends().add(userId);
        log.info("Пользователи с id={},{} теперь являются друзьями", userId, friendId);
    }

    @Override
    public void deleteFriend(Long userId, Long friendId) {
        User user = userStorage.getUser(userId);
        User friend = userStorage.getUser(friendId);

        user.getFriends().remove(friendId);
        friend.getFriends().remove(userId);
        log.info("Пользователи с id={},{} теперь не являются друзьями", userId, friendId);
    }

    @Override
    public List<User> getFriends(Long id) {
        User user = userStorage.getUser(id);
        List<User> friends = new ArrayList<>();

        for (Long friendId : user.getFriends()) {
            friends.add(userStorage.getUser(friendId));
        }

        log.info("Список друзей пользователя с id={}", id);

        return friends;
    }

    @Override
    public List<User> getCommonFriends(Long userId, Long otherId) {
        final User user = userStorage.getUser(userId);
        final User other = userStorage.getUser(otherId);
        final Set<Long> friends = user.getFriends();
        final Set<Long> otherFriends = other.getFriends();

        log.info("Вернули список общих друзей пользователей с id={},{}", userId, otherId);

        return friends.stream()
                .filter(otherFriends::contains)
                .map(id -> userStorage.getUser(id))
                .collect(Collectors.toList());
    }
}
