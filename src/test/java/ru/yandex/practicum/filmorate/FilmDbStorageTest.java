package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.time.LocalDate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@JdbcTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class FilmDbStorageTest {
    private final JdbcTemplate jdbcTemplate;
    private Film newFilm;
    private FilmStorage filmStorage;
    private final MPA mpa = new MPA(1L, "G");

    @BeforeEach
    void setUp() {
        newFilm = new Film(1L, "new", "description",
                LocalDate.of(1999, 12, 13), 120, mpa);
        filmStorage = new FilmDbStorage(jdbcTemplate);
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
    void testUpdateFilmName() {
        filmStorage.createFilm(newFilm);

        newFilm.setName("OTHER");
        filmStorage.updateFilm(newFilm);

        Film savedFilm = filmStorage.getFilm(1L);

        assertThat(savedFilm)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(newFilm);
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
}