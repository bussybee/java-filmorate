package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
public class User {
    private Integer id;
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

    public void setName(String name) {
        if (name == null || name.isBlank()) {
            this.name = this.getLogin();
        } else {
            this.name = name;
        }
    }
}
