package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;

import java.util.Collection;
import java.util.Set;

@Service
public class FilmService implements FilmStorage {
    InMemoryFilmStorage filmStorage;

    public FilmService(InMemoryFilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    @Override
    public Film createFilm(Film film) {
        return filmStorage.createFilm(film);
    }

    @Override
    public Collection<Film> findAllFilms() {
        return filmStorage.findAllFilms();
    }

    @Override
    public Film updateFilm(Film film) {
        return filmStorage.updateFilm(film);
    }

    @Override
    public Film addLike(Long userId, Long filmId) {
        return filmStorage.addLike(userId, filmId);
    }

    @Override
    public Film deleteLike(Long userId, Long filmId) {
        return filmStorage.deleteLike(userId, filmId);
    }

    @Override
    public Set<Film> topFilms(Long count) {
        return filmStorage.topFilms(count);
    }

}
