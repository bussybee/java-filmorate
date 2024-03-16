package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
public class FilmController {
    private Map<Integer, Film> films = new HashMap<>();
    private int nextId = 1;

    @GetMapping("/films")
    public List<Film> getAllFilms() {
        return new ArrayList<>(films.values());
    }

    @PostMapping(value = "/film")
    public Film createFilm(@RequestBody Film film) throws ValidationException {
        if (isValid(film)) {
            film.setId(nextId);
            films.put(nextId++, film);
            log.info("Добавлен новый фильм {}", film);
        }
        return film;
    }

    @PutMapping("/film")
    public Film updateFilm(@RequestBody Film film) throws ValidationException {
        int id = film.getId();
        if (films.containsKey(id) && isValid(film)) {
            films.put(film.getId(), film);
            log.info("Фильм {} обновлен", film);
            return film;
        } else {
            return null;
        }
    }

    private boolean isValid(Film film) throws ValidationException {
        try {
            if (film.getName() == null) {
                throw new ValidationException("Название фильма пустое");
            }

            if (film.getDescription().length() >= 200) {
                throw new ValidationException("Длина описания больше 200 символов");
            }

            if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
                throw new ValidationException("Дата релиза раньше дня рождения кино");
            }

            if (film.getDuration().isNegative()) {
                throw new ValidationException("Продолжительность фильма отрицательна");
            }

            return true;
        } catch (NullPointerException e) {
            throw new ValidationException("Обнаружена нулевая ссылка");
        }
    }
}
