package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.ValidationException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Qualifier("inMemory")
@Slf4j
public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> users = new HashMap<>();
    private Long idGenerator = 1L;

    @Override
    public List<User> getAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User create(User user) {
        user.setId(idGenerator++);
        user.setName(user.getName());
        users.put(user.getId(), user);
        log.info("Создан новый пользователь {}", user);

        return user;
    }

    @Override
    public User update(User user) {
        Long id = user.getId();

        if (id == null) {
            throw new ValidationException("ID пользователя не указан");
        }

        if (!users.containsKey(id)) {
            throw new UserNotFoundException(String.format("Пользователь с id=%s не найден", id));
        }

        users.put(user.getId(), user);
        log.info("Пользователь с id={} обновлен", id);
        return user;
    }

    @Override
    public User getUser(Long id) {
        User user = users.get(id);
        if (user == null) {
            throw new FilmNotFoundException(String.format("Пользователь с id=%s не найден", id));
        }
        return user;
    }
}
