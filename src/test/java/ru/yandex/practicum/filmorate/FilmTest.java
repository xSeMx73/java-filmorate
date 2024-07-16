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

    @Test
    void addLikeTest() {
        User user = new User();
        user.setEmail("111@111");
        user.setLogin("Login");
        user.setName("Name");
        user.setBirthday(LocalDate.of(2020, 1, 1));
        userController.create(user);
        User user2 = new User();
        user2.setEmail("222@222");
        user2.setLogin("Login2");
        user2.setName("Name2");
        user2.setBirthday(LocalDate.of(2022, 2, 2));
        userController.create(user2);
        User user3 = new User();
        user3.setEmail("333@333");
        user3.setLogin("Login3");
        user3.setName("Name3");
        user3.setBirthday(LocalDate.of(2020, 1, 1));
        userController.create(user3);
        User user4 = new User();
        user4.setEmail("444@444");
        user4.setLogin("Login4");
        user4.setName("Name4");
        user4.setBirthday(LocalDate.of(2020, 1, 1));
        userController.create(user4);
        User user5 = new User();
        user5.setEmail("555@555");
        user5.setLogin("Login5");
        user5.setName("Name5");
        user5.setBirthday(LocalDate.of(2020, 1, 1));
        userController.create(user5);
        User user6 = new User();
        user6.setEmail("666@666");
        user6.setLogin("Login6");
        user6.setName("Name6");
        user6.setBirthday(LocalDate.of(2020, 1, 1));
        userController.create(user6);
        Film film = new Film();
        film.setName("film1");
        film.setDescription("description1");
        film.setReleaseDate(LocalDate.of(2020, 1, 1));
        film.setDuration(100L);
        filmController.create(film);
        Film film2 = new Film();
        film2.setName("film2");
        film2.setDescription("description2");
        film2.setReleaseDate(LocalDate.of(2020, 1, 1));
        film2.setDuration(100L);
        filmController.create(film2);
        Film film3 = new Film();
        film3.setName("film3");
        film3.setDescription("description3");
        film3.setReleaseDate(LocalDate.of(2020, 1, 1));
        film3.setDuration(100L);
        filmController.create(film3);
        Film film4 = new Film();
        film4.setName("film4");
        film4.setDescription("description4");
        film4.setReleaseDate(LocalDate.of(2020, 1, 1));
        film4.setDuration(100L);
        filmController.create(film4);
        Film film5 = new Film();
        film5.setName("film5");
        film5.setDescription("description5");
        film5.setReleaseDate(LocalDate.of(2020, 1, 1));
        film5.setDuration(100L);
        filmController.create(film5);
        Film film6 = new Film();
        film6.setName("film6");
        film6.setDescription("description6");
        film6.setReleaseDate(LocalDate.of(2020, 1, 1));
        film6.setDuration(100L);
        filmController.create(film6);
        filmController.addLike(1L, 1L);
        filmController.addLike(1L, 2L);
        filmController.addLike(1L, 3L);
        filmController.addLike(1L, 4L);
        filmController.addLike(2L, 1L);
        filmController.addLike(2L, 2L);
        filmController.addLike(2L, 3L);
        filmController.addLike(3L, 1L);
        filmController.addLike(3L, 2L);
        filmController.addLike(4L, 1L);
        filmController.addLike(4L, 2L);
        filmController.addLike(4L, 3L);
        filmController.addLike(4L, 4L);
        filmController.addLike(4L, 5L);
        filmController.addLike(4L, 6L);
        filmController.addLike(5L, 1L);
        filmController.addLike(6L, 2L);
        filmController.addLike(6L, 1L);
        int c = 1;
        int t = 1;
        for(Film f : filmController.findAll()){
            System.out.printf("All #%s %s%n", t,f);
            t++;
        }
        for(Film f : filmController.topFilms(10)){
            System.out.printf("Top #%s %s%n", c,f);
            c++;
        }


    }
}
