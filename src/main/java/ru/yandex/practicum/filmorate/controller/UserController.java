package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
public class UserController {
    private Map<Integer, User> users = new HashMap<>();
    private int nextId = 1;

    @GetMapping("/users")
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    @PostMapping(value = "/user")
    public User createUser(@RequestBody User user) throws ValidationException {
        if (isValid(user)) {
            user.setId(nextId);
            users.put(nextId++, user);
            log.info("Создан новый пользователь {}", user);
        }
        return user;
    }

    @PutMapping("/user")
    public User updateUser(@RequestBody User user) throws ValidationException {
        int id = user.getId();
        if (users.containsKey(id) && isValid(user)) {
            users.put(user.getId(), user);
            log.info("Пользователь {} обновлен", user);
            return user;
        } else {
            return null;
        }
    }

    private boolean isValid(User user) throws ValidationException {
        try {
            if (user.getEmail() == null || !user.getEmail().contains("@")) {
                throw new ValidationException("Электронная почта пустая или не содержит символ @");
            }

            if (user.getLogin() == null || user.getLogin().isBlank()) {
                throw new ValidationException("Логин пустой или содержит пробелы");
            }

            if (user.getName() == null || user.getName().isBlank()) {
                user.setName(user.getLogin());
            }

            if (user.getBirthday().isAfter(LocalDate.now())) {
                throw new ValidationException("Дата рождения находится в будущем");

            }

            return true;
        } catch (NullPointerException e) {
            throw new ValidationException("Обнаружена нулевая ссылка");
        }
    }
}
