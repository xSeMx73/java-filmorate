package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;


import java.time.LocalDate;

public class FilmTest {

    InMemoryFilmStorage filmStorage = new InMemoryFilmStorage();
    InMemoryUserStorage userStorage = new InMemoryUserStorage();
    UserService userService = new UserService(userStorage);

    UserController userController = new UserController(userService);
    FilmService filmService = new FilmService(filmStorage, userStorage);
    FilmController filmController = new FilmController(filmService);





    @Test
    void createTestNameFilm() {
        Film film = new Film();
        film.setName("Name");
        film.setDescription("description");
        film.setReleaseDate(LocalDate.of(2020, 1, 1));
        film.setDuration(100L);
        filmController.create(film);
    }

    @Test
    void createTestDescriptionFilm() {
        Film film = new Film();
        film.setName("film");
        film.setDescription("description");
        film.setReleaseDate(LocalDate.of(2020, 1, 1));
        film.setDuration(100L);
        filmController.create(film);
    }


    @Test
    void createTestReleaseFilm() {
        Film film = new Film();
        film.setName("film");
        film.setDescription("description");
        film.setReleaseDate(LocalDate.of(2000, 1, 1));
        film.setDuration(100L);
        filmController.create(film);
    }

    @Test
    void createTestDurationFilm() {
        Film film = new Film();
        film.setName("film");
        film.setDescription("description");
        film.setReleaseDate(LocalDate.of(2020, 1, 1));
        film.setDuration(100L);
        Film film1 = filmController.create(film);
        film1.setId(1L);
    }
}
