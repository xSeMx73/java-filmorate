package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;
import java.util.Comparator;
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
    public void addLike(Long filmId, Long userId) {
        if (userId == null || filmId == null || !filmStorage.findAllFilms().contains(getFilm(filmId)) ||
                filmStorage.getFilm(filmId).getLikes().contains(userId)) {
            log.trace("Ошибка добавления лайка");
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, "Неверный Id или вы уже ставили лайк");
        } else if (!userStorage.findAllUsers().contains(userStorage.getUser(userId))) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Неверный Id или вы уже ставили лайк");
        }

        filmStorage.getFilm(filmId).getLikes().add(userId);
    }

    @Override
    public void deleteLike(Long userId, Long filmId) {
        if (userId == null || filmId == null || !userStorage.findAllUsers().contains(userStorage.getUser(userId))
                || !filmStorage.findAllFilms().contains(getFilm(filmId)) || !filmStorage.getFilm(filmId).getLikes().contains(userId)) {
            log.trace("Ошибка удаления лайка");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Неверный Id или вы не ставили лайк");
        }
        filmStorage.getFilm(filmId).getLikes().remove(userId);
    }

    @Override
    public Collection<Film> topFilms(int count) {
        return filmStorage.findAllFilms().stream()
                .sorted(Comparator.comparing(Film::getName))
                .sorted((film1, film2) -> film2.getLikes().size() - film1.getLikes().size())
                .limit(count)
                .collect(Collectors.toList());
    }

}
