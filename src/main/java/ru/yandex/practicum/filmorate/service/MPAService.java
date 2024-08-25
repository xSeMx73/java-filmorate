package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.repository.MPARepository;

import java.util.Collection;
import java.util.Optional;

@Slf4j
@Service
public class MPAService {
    private final MPARepository mpaRepository;

    public MPAService(MPARepository mpaRepository) {
        this.mpaRepository = mpaRepository;
    }


    public Collection<MPA> findAllMPA() {
        return mpaRepository.findAllMPA();
    }

    public Optional<MPA> findOneMPA(Long id) {
        Optional<MPA> mpa = mpaRepository.findOneMPA(id);
        if (mpa.isEmpty() || mpaRepository.findAllMPA().size() < id) {
            log.trace("Рейтинг MPA не неайден");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Рейтинг MPA не неайден");
        } else {
            return mpaRepository.findOneMPA(id);
        }
    }
}
