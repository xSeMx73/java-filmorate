package ru.yandex.practicum.filmorate.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {


    public final Map<Long, Film> films = new HashMap<>();

    @GetMapping()
    public Collection<Film> findAll() {
        return films.values();
    }


    @PostMapping
    public Film create(@RequestBody Film film) {
        if (film.getName().isEmpty()) {
            log.trace("Ошибка добавления фильма");
            throw new ValidationException("Название фильма не может быть пустым");
        } else if (film.getDescription().length() > 200) {
            log.trace("Ошибка добавления фильма");
            throw new ValidationException("Максимальная длина описания — 200 символов");
        } else if (film.getDuration() == null || film.getDuration() < 0) {
            log.trace("Ошибка добавления фильма");
            throw new ValidationException("Продолжительность фильма должна быть положительным числом");
        } else if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.trace("Ошибка добавления фильма");
            throw new ValidationException("Неверная дата релиза");
        } else {
            film.setId(getNextId());
            films.put(film.getId(), film);
            log.trace("Фильм " + film.getName() + " добавлен");
            return film;
        }
    }

    @PutMapping
    public Film update(@RequestBody Film newFilm) {
    if (!films.containsKey(newFilm.getId())) {
        log.trace("Ошибка обновления фильма");
            throw new ValidationException("Id должен быть указан");
    } else if (newFilm.getName().isEmpty()) {
        log.trace("Ошибка обновления фильма");
            throw new ValidationException("Название фильма не может быть пустым");
    } else if (newFilm.getDescription().length() > 200) {
        log.trace("Ошибка обновления фильма");
        throw new ValidationException("Максимальная длина описания — 200 символов");
    } else if (newFilm.getDuration() == null || newFilm.getDuration() < 0) {
        log.trace("Ошибка обновления фильма");
        throw new ValidationException("Продолжительность фильма должна быть положительным числом");
    } else if (newFilm.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
        log.trace("Ошибка обновления фильма");
        throw new ValidationException("Неверная дата релиза");
    } else {
        films.put(newFilm.getId(), newFilm);
        log.trace("Фильм " + newFilm.getName() + " обновлен");
        return newFilm;
    }
  }

    private long getNextId() {
        long currentMaxId = films.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

}
