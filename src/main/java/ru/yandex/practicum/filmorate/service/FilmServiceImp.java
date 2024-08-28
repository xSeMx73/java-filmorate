package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.repository.FilmRepository;
import ru.yandex.practicum.filmorate.repository.GenreRepository;
import ru.yandex.practicum.filmorate.repository.MPARepository;
import ru.yandex.practicum.filmorate.repository.UserRepository;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Optional;


@Slf4j
@Service
public class FilmServiceImp implements FilmService {
    private final UserRepository userRepository;
    private final FilmRepository filmRepository;
    private final MPARepository mpaRepository;
    private final GenreRepository genreRepository;

    public FilmServiceImp(UserRepository userRepository, FilmRepository filmRepository, MPARepository mpaRepository, GenreRepository genreRepository) {
        this.userRepository = userRepository;
        this.filmRepository = filmRepository;
        this.mpaRepository = mpaRepository;
        this.genreRepository = genreRepository;
    }

    @Override
    public Film createFilm(Film film) {
        validate(film, "добавления");
        return filmRepository.createFilm(film);
    }

    @Override
    public Collection<Film> findAllFilms() {
        return filmRepository.findAllFilms();
    }

    public Optional<Film> getFilm(Long id) {
        return filmRepository.getFilm(id);
    }

    @Override
    public Film updateFilm(Film film) {
        if (filmRepository.getFilm(film.getId()).isEmpty()) {
            log.trace("Ошибка обновления фильма");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Id должен быть указан");
        } else {
            validate(film, "обновления");
            return filmRepository.updateFilm(film);
        }
    }

    @Override
    public void addLike(Long filmId, Long userId) {
        Optional<Film> film = filmRepository.getFilm(filmId);
        if (film.isEmpty() || userId == null || filmId == null || userRepository.getUser(userId).isEmpty() ||
                film.get().getLikes().contains(userId)) {
            log.trace("Ошибка добавления лайка");
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, "Неверный Id или вы уже ставили лайк");
        } else {
            filmRepository.addLike(filmId, userId);
        }
    }

    @Override
    public void deleteLike(Long userId, Long filmId) {
        Optional<Film> film = filmRepository.getFilm(filmId);
        if (film.isEmpty() || userId == null || filmId == null || userRepository.getUser(userId).isEmpty()
                || !film.get().getLikes().contains(userId)) {
            log.trace("Ошибка удаления лайка");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Неверный Id или вы не ставили лайк");
        }
        filmRepository.deleteLike(filmId, userId);
    }

    @Override
    public Collection<Film> topFilms(int count) {
        return filmRepository.topFilms(count);
    }

    private void validate(Film film, String operation) {
        int numberOfGenres = genreRepository.findAllGenres().size();
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
        } else if (film.getMpa().getId() > mpaRepository.findAllMPA().size()) {
            log.trace("Рейтинг MPA не неайден");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Рейтинг MPA не неайден");
        }
        for (Genre g : film.getGenres()) {
            if (g.getId() > numberOfGenres) {
                log.trace("Жанр фильма не неайден");
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Жанр фильма не неайден");
            }
        }
    }
}
