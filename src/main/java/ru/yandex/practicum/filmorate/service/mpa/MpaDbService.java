package ru.yandex.practicum.filmorate.service.mpa;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.storage.mpa.MpaStorage;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class MpaDbService implements MpaService {
    private MpaStorage mpaStorage;

    @Override
    public MPA getMpa(Long id) {
        return mpaStorage.getMpa(id);
    }

    @Override
    public List<MPA> getAll() {
        return mpaStorage.getAll();
    }
}
