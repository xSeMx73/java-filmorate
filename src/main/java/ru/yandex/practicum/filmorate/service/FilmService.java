package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FilmService implements FilmStorage {
    FilmStorage filmStorage;
    UserStorage userStorage;


    public FilmService(InMemoryFilmStorage filmStorage, InMemoryUserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    @Override
    public Film createFilm(Film film) {
        return filmStorage.createFilm(film);
    }

    @Override
    public Collection<Film> findAllFilms() {
        return filmStorage.findAllFilms();
    }

    public Film getFilm(Long id) {
        return filmStorage.getFilm(id);
    }

    @Override
    public Film updateFilm(Film film) {
        return filmStorage.updateFilm(film);
    }

    @Override
    public void addLike(Long userId, Long filmId) {
        if (userId == null || filmId == null || !userStorage.findAllUsers().contains(userStorage.getUser(userId))
                || !filmStorage.findAllFilms().contains(getFilm(filmId))) {
            log.trace("Ошибка добавления лайка");
            throw new ValidationException("Неверный Id или вы уже ставили лайк");
        }
        filmStorage.getFilm(filmId).getLikes().add(userId);
    }

    @Override
    public void deleteLike(Long userId, Long filmId) {
        filmStorage.deleteLike(userId, filmId);
    }

    @Override
    public Set<Film> topFilms(Long count) {
        return filmStorage.findAllFilms().stream()
                .sorted((film1, film2) -> film2.getLikes().size() - film1.getLikes().size())
                .limit(count).collect(Collectors.toSet());
    }
}
