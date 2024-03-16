package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.Duration;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class FilmValidatorTest {

    @Test
    void createValidFilm() throws ValidationException {
        Film film = new Film("Name", "Description",
                LocalDate.of(2022, 12, 12 ), Duration.ofHours(2));
        FilmController controller = new FilmController();

        Film createdFilm = controller.createFilm(film);

        assertEquals(film.getName(), createdFilm.getName());
        assertEquals(film.getDescription(), createdFilm.getDescription());
        assertEquals(film.getReleaseDate(), createdFilm.getReleaseDate());
        assertEquals(film.getDuration(), createdFilm.getDuration());
    }

    @Test
    void createFilmWithoutName() {
        Film film = new Film(null, "d",
                LocalDate.of(1980, 12, 12 ), Duration.ofHours(2));
        FilmController controller = new FilmController();

        assertThrows(ValidationException.class, () -> controller.createFilm(film));
    }

    @Test
    void createFilmWithNegativeDuration() {
        Film film = new Film("name", "d",
                LocalDate.of(1980, 12, 12 ), Duration.ofHours(-2));
        FilmController controller = new FilmController();

        assertThrows(ValidationException.class, () -> controller.createFilm(film));
    }

    @Test
    void updateValidFilm() throws ValidationException {
        Film film = new Film("Name", "Description",
                LocalDate.of(2022, 12, 12 ), Duration.ofHours(2));
        FilmController controller = new FilmController();

        controller.createFilm(film);
        film.setName("Avatar");
        Film updatedFilm = controller.updateFilm(film);

        assertEquals("Avatar", updatedFilm.getName());
    }

    @Test
    void updateFilmWithWrongReleaseDate() throws ValidationException {
        Film film = new Film("Name", "Description",
                LocalDate.of(2022, 12, 12 ), Duration.ofHours(2));
        FilmController controller = new FilmController();

        controller.createFilm(film);
        LocalDate wrongDate = LocalDate.of(1880, 12, 12);
        film.setReleaseDate(wrongDate);

        assertThrows(ValidationException.class, () -> controller.updateFilm(film));
    }

    @Test
    void createNullFilm() {
        Film film = null;
        FilmController controller = new FilmController();

        assertThrows(ValidationException.class, () -> controller.createFilm(film));
    }
}