package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private Long id;
    @NotEmpty(message = "Почта не может быть пустой")
    @Email(message = "Почта должна содержать символ @")
    private String email;
    @NotEmpty(message = "Логин не должен быть пустым")
    @NotBlank(message = "Логин не должен содержать пробелы")
    private String login;
    private String name;
    @Past(message = "Дата рождения находится в будущем")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthday;
    private Set<Long> friends = new HashSet<>();

    public void setName(String name) {
        if (name == null || name.isBlank()) {
            this.name = this.getLogin();
        } else {
            this.name = name;
        }
    }

    public User(final Long id, final String email, final String login, final String name, final LocalDate birthday) {
        this.id = id;
        this.email = email;
        this.login = login;
        this.name = name;
        this.birthday = birthday;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> values = new HashMap<>();
        values.put("email", email);
        values.put("login", login);
        values.put("name", name);
        values.put("birthday", birthday);

        return values;
    }
}
