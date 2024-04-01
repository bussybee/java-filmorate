package ru.yandex.practicum.filmorate.service.user;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.ArrayList;
import java.util.List;

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
        User user = userStorage.getUser(userId);
        User other = userStorage.getUser(otherId);
        List<User> commonFriends = new ArrayList<>();

        for (Long userFriend : user.getFriends()) {
            for (Long otherFriend : other.getFriends()) {
                if (userFriend.equals(otherFriend)) {
                    commonFriends.add(userStorage.getUser(userFriend));
                }
            }
        }

        log.info("Список общих друзей пользователей с id={},{}", userId, otherId);

        return commonFriends;
    }
}
