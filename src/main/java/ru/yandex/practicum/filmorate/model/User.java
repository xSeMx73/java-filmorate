package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class User {
    private Long id = 1L;
    private String email;
    private String login;
    private LocalDate birthday;
    private String name;
    private Set<Long> friends = new HashSet<>();

}