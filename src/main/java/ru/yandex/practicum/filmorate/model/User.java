package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
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
}
