package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.*;
import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class FilmValidatorTest {
    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testWrongFilmReleaseDateValidation() {
        Film film = new Film("name", "qedfbg",
                LocalDate.of(1860, 3, 3), 100);

        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertFalse(violations.isEmpty());
    }

    @Test
    void testRightFilmReleaseDateValidation() {
        Film film = new Film("name", "qedfbg",
                LocalDate.of(1999, 3, 3), 100);

        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertTrue(violations.isEmpty());
    }

    @Test
    void testWrongFilmDurationValidation() {
        Film film = new Film("name", "qedfbg",
                LocalDate.of(2003, 3, 3), -100);

        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertFalse(violations.isEmpty());
    }

    @Test
    void shouldNotCreateFilmWithoutName() {
        Film film = new Film(null, "qedfbg",
                LocalDate.of(2003, 3, 3), 100);

        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertFalse(violations.isEmpty());
    }

    @Test
    void shouldCreateValidFilm() {
        Film film = new Film("name", "qedfbg",
                LocalDate.of(2003, 3, 3), 100);

        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertTrue(violations.isEmpty());
    }

    @Test
    void shouldNotCreateFilmWithLongDescription() {
        String description = "Пятеро друзей ( комик-группа «Шарло»), приезжают в город Бризуль. " +
                "Здесь они хотят разыскать господина Огюста Куглова, который задолжал им деньги, " +
                "а именно 20 миллионов. о Куглов, который за время «своего отсутствия», стал кандидатом Коломбани.";
        Film film = new Film("name", description,
                LocalDate.of(2003, 3, 3), 100);

        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertFalse(violations.isEmpty());
    }

    @Test
    void shouldUpdateFilm() {
        FilmController controller = new FilmController();
        Film newFilm = new Film("name", "qedfbg",
                LocalDate.of(2003, 3, 3), 100);

        controller.createFilm(newFilm);
        newFilm.setName("Name");

        Film updatedFilm = controller.updateFilm(newFilm);

        assertEquals(newFilm, updatedFilm);
    }

    @Test
    void shouldThrowValidationExceptionWithUnknownFilm() {
        FilmController controller = new FilmController();
        Film newFilm = new Film("name", "qedfbg",
                LocalDate.of(2003, 3, 3), 100);

        controller.createFilm(newFilm);
        newFilm.setId(99);

        assertThrows(NoSuchElementException.class, () -> controller.updateFilm(newFilm));
    }

    @Test
    void shouldGetAllFilms() {
        FilmController controller = new FilmController();
        Film film1 = new Film("name1", "qedfbg",
                LocalDate.of(2003, 3, 3), 100);

        Film film2 = new Film("name2", "qedfbg",
                LocalDate.of(2000, 9, 14), 140);

        controller.createFilm(film1);
        controller.createFilm(film2);

        assertEquals(List.of(film1, film2), controller.getAllFilms());
    }
}