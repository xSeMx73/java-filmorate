package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.service.MPAService;

import java.util.Collection;
import java.util.Optional;


@RestController
@RequestMapping("/mpa")
@Qualifier("MPADbStorage")
public class MPAController {
    private final MPAService mpaService;

    public MPAController(MPAService mpaService) {
        this.mpaService = mpaService;
    }

    @GetMapping
    public Collection<MPA> findAllMPA() {
        return mpaService.findAllMPA();
    }

    @GetMapping("/{id}")
    public Optional<MPA> findOneMPA(@PathVariable Long id) {
        return mpaService.findOneMPA(id);
    }
}

