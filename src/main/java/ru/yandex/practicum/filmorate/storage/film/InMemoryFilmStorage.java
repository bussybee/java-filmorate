package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.ValidationException;
import java.util.*;

@Component
@Qualifier("inMemory")
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Long, Film> films = new HashMap<>();
    private Long idGenerator = 1L;

    @Override
    public List<Film> getAllFilms() {
        return new ArrayList<>(films.values());
    }

    @Override
    public Film createFilm(Film film) {
        film.setId(idGenerator);
        film.setDuration(film.getDuration());
        films.put(idGenerator++, film);
        log.info("Добавлен новый фильм {}", film);
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        Long id = film.getId();

        if (id == null) {
            throw new ValidationException("ID фильма не указан");
        }

        if (!films.containsKey(id)) {
            throw new FilmNotFoundException(String.format("Фильм с id=%s не найден", id));
        }

        film.setDuration(film.getDuration());
        films.put(film.getId(), film);
        log.info("Фильм с id={} обновлен", id);
        return film;
    }

    @Override
    public Film getFilm(Long id) {
        Film film = films.get(id);
        if (film == null) {
            throw new FilmNotFoundException(String.format("Фильм с id=%s не найден", id));
        }
        return film;
    }

    @Override
    public List<Film> getPopularFilms(Integer count) {
        return null;
    }
}
