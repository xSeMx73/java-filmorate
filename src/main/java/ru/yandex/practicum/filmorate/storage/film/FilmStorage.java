package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.Set;

public interface FilmStorage {

    Film createFilm(Film film);

    Collection<Film> findAllFilms();

    Film updateFilm(Film film);

    void addLike(Long userID, Long filmId);

    void deleteLike(Long userId, Long filmId);

    Set<Film> topFilms(Long count);

    Film getFilm(Long id);
}
