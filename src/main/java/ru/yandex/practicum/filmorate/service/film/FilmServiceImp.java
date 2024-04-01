package ru.yandex.practicum.filmorate.service.film;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.IncorrectParameterException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class FilmServiceImp implements FilmService {
    private FilmStorage filmStorage;
    private UserStorage userStorage;

    @Override
    public Film createFilm(Film film) {
        return filmStorage.createFilm(film);
    }

    @Override
    public Film updateFilm(Film film) {
        return filmStorage.updateFilm(film);
    }

    @Override
    public List<Film> getAllFilms() {
        return filmStorage.getAllFilms();
    }

    @Override
    public void putLike(Long filmId, Long userId) {
        Film film = filmStorage.getFilm(filmId);
        userStorage.getUser(userId);

        film.getLikes().add(userId);
        log.info("Пользователь с id={} поставил лайк фильму c id={}", userId, filmId);
    }

    @Override
    public void deleteLike(Long filmId, Long userId) {
        Film film = filmStorage.getFilm(filmId);
        userStorage.getUser(userId);

        film.getLikes().remove(userId);
        log.info("Пользователь с id={} убрал лайк фильма c id={}", userId, filmId);
    }

    @Override
    public List<Film> getPopularFilms(Integer count) {
        if (count <= 0) {
            throw new IncorrectParameterException("Параметр count имеет отрицательное значение.");
        }

        count = Optional.ofNullable(count).orElse(10);

        List<Film> allFilms = filmStorage.getAllFilms();
        allFilms.sort((film1, film2) -> film2.getLikes().size() - film1.getLikes().size());

        log.info("Список {} популярных фильмов", count);
        return allFilms.subList(0, Math.min(count, allFilms.size()));
    }
}
