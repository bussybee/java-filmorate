package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import javax.validation.ValidationException;
import java.util.*;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final Map<Integer, User> users = new HashMap<>();
    private int idGenerator = 1;

    @GetMapping
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    @PostMapping
    public User createUser(@RequestBody @Valid User user) {
        user.setId(idGenerator++);
        user.setName(user.getName());
        users.put(user.getId(), user);
        log.info("Создан новый пользователь {}", user);

        return user;
    }

    @PutMapping
    public User updateUser(@RequestBody @Valid User user) {
        Integer id = user.getId();

        if (id == null) {
            throw new ValidationException("ID пользователя не указан");
        }

        if (!users.containsKey(id)) {
            log.info("Пользователь с id={} не найден", id);
            throw new NoSuchElementException();
        }

        users.put(user.getId(), user);
        log.info("Пользователь с id={} обновлен", id);
        return user;
    }
}
