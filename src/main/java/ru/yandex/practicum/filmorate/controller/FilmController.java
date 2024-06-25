package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.Collection;
import java.util.Set;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/films")
public class FilmController {

    private final FilmService filmService;

    @GetMapping()
    public Collection<Film> findAll() {
        return filmService.findAllFilms();
    }

    @PostMapping
    public Film create(@RequestBody Film film) {
        return filmService.createFilm(film);
    }

    @PutMapping
    public Film update(@RequestBody Film film) {
        return filmService.updateFilm(film);
    }

    @GetMapping("/{id}")
    public Film getFilm(@PathVariable Long id) {
        return filmService.getFilm(id);
    }

    @PutMapping("/{filmId}/like/{userId}")
    public void addLike(@RequestBody Long filmId,@RequestBody Long userId) {
         filmService.addLike(filmId, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLike(@RequestBody Long id,@RequestBody Long userId) {
        filmService.deleteLike(userId, id);
    }

    @GetMapping("/popular?count={count}")
    public Set<Film> topFilms(@RequestParam(value = "count", defaultValue = "10", required = false) Long count) {
        return filmService.topFilms(count);
    }
 //

}
