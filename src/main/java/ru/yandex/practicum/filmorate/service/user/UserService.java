package ru.yandex.practicum.filmorate.service.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserService {
    User create(User user);
    User update(User user);
    List<User> getAllUsers();
    User getUser(Long id);
    void addToFriends(Long userId, Long friendId);
    void deleteFriend(Long userId, Long friendId);
    List<User> getFriends(Long id);
    List<User> getCommonFriends(Long userId, Long otherId);
}
