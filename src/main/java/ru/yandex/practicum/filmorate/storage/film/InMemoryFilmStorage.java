package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {

    InMemoryUserStorage userStorage;
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
    public Film addLike(Long userId, Long filmId) {
        if (userId == null || filmId == null || !userStorage.getUsers().containsKey(userId)
                || !films.containsKey(filmId) || films.get(filmId).getLikes().contains(userId)) {
            log.trace("Ошибка добавления лайка");
            throw new ValidationException("Неверный Id или вы уже ставили лайк");
        }
        films.get(filmId).getLikes().add(userId);
        return films.get(filmId);
    }

    @Override
    public Film deleteLike(Long userId, Long filmId) {
        if (userId == null || filmId == null || !userStorage.getUsers().containsKey(userId)
                || !films.containsKey(filmId) || !films.get(filmId).getLikes().contains(userId)) {
            log.trace("Ошибка удаления лайка");
            throw new ValidationException("Неверный Id или вы не ставили лайк");
        }
        films.get(filmId).getLikes().remove(userId);
        return films.get(filmId);
    }

    @Override
    public Set<Film> topFilms(Long count) {
        List<Map.Entry<Long, Film>> sortedFilms = new ArrayList<>(films.entrySet());
        sortedFilms.sort((entry1, entry2) ->
                Long.compare(entry2.getValue().getLikes().size(), entry1.getValue().getLikes().size()));
        return sortedFilms.stream()
                .limit(count)
                .map(Map.Entry::getValue)
                .collect(Collectors.toSet());
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
