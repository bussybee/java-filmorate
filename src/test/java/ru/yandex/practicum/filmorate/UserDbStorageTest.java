package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@JdbcTest // указываем, о необходимости подготовить бины для работы с БД
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserDbStorageTest {
    private final JdbcTemplate jdbcTemplate;
    private User newUser;
    private UserDbStorage userStorage;

    @BeforeEach
    void beforeEach() {
        newUser = new User(1L, "user@email.ru", "vanya123",
                "Ivan Petrov", LocalDate.of(1990, 1, 1));
        userStorage = new UserDbStorage(jdbcTemplate);
    }

    @Test
    public void testFindUserById() {
        userStorage.create(newUser);

        // вызываем тестируемый метод
        User savedUser = userStorage.getUser(1L);

        // проверяем утверждения
        assertThat(savedUser)
                .isNotNull() // проверяем, что объект не равен null
                .usingRecursiveComparison() // проверяем, что значения полей нового
                .isEqualTo(newUser);        // и сохраненного пользователя - совпадают
    }

    @Test
    public void testCreateNewUser() {
        User createdUser = userStorage.create(newUser);

        assertThat(createdUser)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(newUser);
    }

    @Test
    void testUpdateLoginUser() {
        userStorage.create(newUser);
        User updatedUser = new User(1L, "user@email.ru", "vanya",
                "Ivan Petrov", LocalDate.of(1990, 1, 1));

        userStorage.update(updatedUser);

        User savedUser = userStorage.getUser(1L);

        assertThat(savedUser)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(updatedUser);
    }

    @Test
    void testGetAllUsers() {
        User user2 = new User(2L, "user2@email.ru", "viktoria",
                "Viktoria Semenova", LocalDate.of(1980, 1, 1));
        userStorage.create(newUser);
        userStorage.create(user2);

        List<User> allUsers = userStorage.getAll();

        assertThat(allUsers)
                .isEqualTo(List.of(newUser, user2));
    }
}
