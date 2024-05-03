package ru.yandex.practicum.filmorate.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.service.mpa.MpaService;

import java.util.List;

@RestController
@RequestMapping("/mpa")
@AllArgsConstructor
public class MpaController {
    private MpaService mpaService;

    @GetMapping("{id}")
    public MPA getMpa(@PathVariable Long id) {
        if (id <= 0 || id > mpaService.getAll().size()) {
            throw new IllegalArgumentException("Некорректный идентификатор MPA: " + id);
        }
            return mpaService.getMpa(id);
    }

    @GetMapping
    public List<MPA> getAll() {
        return mpaService.getAll();
    }
}
