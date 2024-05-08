package ru.yandex.practicum.filmorate.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.genre.GenreService;

import java.util.List;

@RestController
@RequestMapping("/genres")
@AllArgsConstructor
public class GenreController {
    private GenreService genreService;

    @GetMapping("{id}")
    public Genre getGenre(@PathVariable Long id) {
        if (id <= 0 || id > genreService.getAll().size()) {
            throw new IllegalArgumentException("Некорректный идентификатор жанра: " + id);
        }
        return genreService.getGenre(id);
    }

    @GetMapping
    public List<Genre> getAll() {
        return genreService.getAll();
    }
}
