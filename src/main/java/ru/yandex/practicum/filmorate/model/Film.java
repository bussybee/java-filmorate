package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import ru.yandex.practicum.filmorate.validator.PositiveDuration;
import ru.yandex.practicum.filmorate.validator.ReleaseDate;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.Duration;
import java.time.LocalDate;

/**
 * Film.
 */
@Data
public class Film {
    private Integer id;
    @NotNull(message = "Имя не должно быть пустым")
    private String name;
    @Size(max = 200, message = "Описание не должно превышать 200 символов")
    private String description;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ReleaseDate
    private LocalDate releaseDate;
    @PositiveDuration
    private Duration duration;

    public Film(String name, String description, LocalDate releaseDate, int durationMinutes) {
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = Duration.ofMinutes(durationMinutes);
    }
}


