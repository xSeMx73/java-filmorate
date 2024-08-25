package ru.yandex.practicum.filmorate.service;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.repository.GenreRepository;

import java.util.Collection;
import java.util.Optional;

@Slf4j
@Service
public class GenreService {
    private final GenreRepository genreRepository;

    public GenreService(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }


    public Collection<Genre> findAllGenres() {
        return genreRepository.findAllGenres();
    }

    public Optional<Genre> findOneGenre(Long id) {
        Optional<Genre> genre = genreRepository.findOneGenre(id);
        if (genre.isEmpty() || genreRepository.findAllGenres().contains(genre)) {
            log.trace("Жанр не неайден");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Неверный Id жанра");
        } else {
            return genreRepository.findOneGenre(id);
        }
    }
}