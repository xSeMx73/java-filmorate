package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.UserService;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {

    UserService userService;
    FilmService filmService;
    public final Map<Long, Film> films = new HashMap<>();

    @Override
    public Collection<Film> findAllFilms() {
        return films.values();
    }

    @Override
    public Film createFilm(Film film) {
        validate(film, "добавления");
        return film;
    }

    @Override
    public Film getFilm(Long id) {
        if (id == null || !films.containsKey(id)) {
            log.trace("Неверный Id фильма");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Id должен быть указан");
        }
        return films.get(id);
    }

    @Override
    public Film updateFilm(Film film) {
        if (!films.containsKey(film.getId())) {
            log.trace("Ошибка обновления фильма");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Id должен быть указан");
        } else {
            validate(film, "обновления");
            return film;
        }
    }

    @Override
    public void addLike(Long userId, Long filmId) {
        filmService.addLike(userId, filmId);
    }

    @Override
    public void deleteLike(Long userId, Long filmId) {
        filmService.deleteLike(userId, filmId);
    }

    @Override
    public Set<Film> topFilms(Long count) {
        return filmService.topFilms(count);
    }


    private void validate(Film film, String operation) {
        if (film.getName().isEmpty()) {
            log.trace("Ошибка " + operation + " фильма");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Название фильма не может быть пустым");
        } else if (film.getDescription().length() > 200) {
            log.trace("Ошибка " + operation + " фильма");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Максимальная длина описания — 200 символов");
        } else if (film.getDuration() == null || film.getDuration() < 0) {
            log.trace("Ошибка " + operation + " фильма");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Продолжительность фильма должна быть положительным числом");
        } else if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.trace("Ошибка " + operation + " фильма");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Неверная дата релиза");
        } else {
            if (operation.equals("добавления")) {
                film.setId(getNextId());
            }
            films.put(film.getId(), film);
            log.trace("Фильм " + film.getName() + operation.substring(0, operation.length() - 2));
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
