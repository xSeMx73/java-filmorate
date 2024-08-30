package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.Optional;


@Component
public interface FilmService {

    Film createFilm(Film film);

    Collection<Film> findAllFilms();

    Film updateFilm(Film film);

    void addLike(Long userID, Long filmId);

    void deleteLike(Long userId, Long filmId);

    Collection<Film> topFilms(int count);

    Optional<Film> getFilm(Long id);
}
