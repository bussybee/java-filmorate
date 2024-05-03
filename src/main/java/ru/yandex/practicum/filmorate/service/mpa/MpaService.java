package ru.yandex.practicum.filmorate.service.mpa;

import ru.yandex.practicum.filmorate.model.MPA;

import java.util.List;

public interface MpaService {
    MPA getMpa(Long id);

    List<MPA> getAll();
}
