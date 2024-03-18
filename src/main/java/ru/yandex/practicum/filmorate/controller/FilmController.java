package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import javax.validation.ValidationException;
import java.util.*;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private final Map<Integer, Film> films = new HashMap<>();
    private int nextId = 1;

    @GetMapping
    public List<Film> getAllFilms() {
        return new ArrayList<>(films.values());
    }

    @PostMapping
    public Film createFilm(@RequestBody @Valid Film film) {
        film.setId(nextId);
        film.setDuration(film.getDuration());
        films.put(nextId++, film);
        log.info("Добавлен новый фильм {}", film);
        return film;
    }

    @PutMapping
    public Film updateFilm(@RequestBody @Valid Film film) {
        Integer id = film.getId();

        if (id == null) {
            throw new ValidationException("ID фильма не указан");
        }

        if (!films.containsKey(id)) {
            log.info("Фильм с id={} не найден", id);
            throw new NoSuchElementException();
        }

        film.setDuration(film.getDuration());
        films.put(film.getId(), film);
        log.info("Фильм с id={} обновлен", id);
        return film;
    }
}
