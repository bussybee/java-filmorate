package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.genre.GenreDbStorage;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;
import ru.yandex.practicum.filmorate.storage.mpa.MpaDbStorage;
import ru.yandex.practicum.filmorate.storage.mpa.MpaStorage;

import java.time.LocalDate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@JdbcTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class FilmDbStorageTest {
    private final JdbcTemplate jdbcTemplate;
    private Film newFilm;
    private FilmStorage filmStorage;

    @BeforeEach
    void setUp() {
        MpaStorage mpaStorage = new MpaDbStorage(jdbcTemplate);
        GenreStorage genreStorage = new GenreDbStorage(jdbcTemplate);
        newFilm = new Film(1L, "new", "description",
                LocalDate.of(1999, 12, 13), 120);
        filmStorage = new FilmDbStorage(jdbcTemplate, mpaStorage, genreStorage);
    }

    @Test
    void createFilm() {
        Film createdFilm = filmStorage.createFilm(newFilm);

        assertThat(createdFilm)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(newFilm);
    }

    @Test
    void updateFilm() {
    }

    @Test
    void getAllFilms() {
    }

    @Test
    void getFilmById() {
        filmStorage.createFilm(newFilm);

        Film savedFilm = filmStorage.getFilm(1L);

        assertThat(savedFilm)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(newFilm);
    }

    @Test
    void getPopularFilms() {
    }
}