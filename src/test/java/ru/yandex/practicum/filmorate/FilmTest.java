package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.time.LocalDate;

public class FilmTest {
    FilmService filmService;
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
        Film film1 = filmController.create(film); //Без этой .... не отправлялся коммит, т.к. метод create нужно было сделать void
        film1.setId(1L);
    }
}
