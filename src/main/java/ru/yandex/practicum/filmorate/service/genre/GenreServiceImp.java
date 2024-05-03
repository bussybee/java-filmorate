package ru.yandex.practicum.filmorate.service.genre;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class GenreServiceImp implements GenreService {
    private GenreStorage genreStorage;

    @Override
    public Genre getGenre(Long id) {
        return genreStorage.getGenre(id);
    }

    @Override
    public List<Genre> getAll() {
        return genreStorage.getAll();
    }
}
