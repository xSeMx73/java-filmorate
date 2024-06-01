package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    private final Map<Long, User> users = new HashMap<>();

    @GetMapping
    public Collection<User> findAll() {
        return users.values();
    }

    @PostMapping
    public User create(@RequestBody User user) {
        if (user.getEmail() == null || !user.getEmail().contains("@")) {
            log.trace("Ошибка добавления пользователя");
            throw new ValidationException("Неверно указана электронная почта");
        }
        for (User us : users.values()) {
            if (us.getEmail().equals(user.getEmail())) {
                log.trace("Ошибка добавления пользователя");
                throw new ValidationException("Данная электронная почта уже используется");
            }
        }
        if (user.getLogin() == null || user.getLogin().isBlank()) {
            log.trace("Ошибка добавления пользователя");
            throw new ValidationException("Логин не может быть пустым и содержать пробелы");

        } else if (user.getBirthday().isAfter(LocalDate.now())) {
            log.trace("Ошибка добавления пользователя");
            throw new ValidationException("Неверная дата рождения");
        }  else if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
            user.setId(getNextId());
            users.put(user.getId(), user);
            log.trace("Пользователь " + user.getLogin() + " добавлен");
            return user;

    }

    @PutMapping
    public User update(@RequestBody User newUser) {
        if (newUser.getId() == null || !users.containsKey(newUser.getId())) {
            log.trace("Ошибка обновления пользователя");
            throw new ValidationException("Неверный ID");
        }
        if (newUser.getEmail() != null && newUser.getEmail().contains("@")) {
            for (User us : users.values()) {
                if (us.getEmail().equals(newUser.getEmail())) {
                    log.trace("Ошибка обновления пользователя");
                    throw new ValidationException("Данная электронная почта уже используется");
                }
            }
        } else {
            log.trace("Ошибка обновления пользователя");
            throw new ValidationException("Неверно указана электронная почта");
        }
        if (newUser.getLogin() == null || newUser.getLogin().isBlank()) {
            log.trace("Ошибка обновления пользователя");
            throw new ValidationException("Логин не может быть пустым и содержать пробелы");
        } else if (newUser.getBirthday().isAfter(LocalDate.now())) {
            log.trace("Ошибка обновления пользователя");
            throw new ValidationException("Неверная дата рождения");
        } else if (newUser.getName().isBlank()) {
            newUser.setName(newUser.getLogin());
        }
            users.put(newUser.getId(), newUser);
        log.trace("Пользователь " + newUser.getLogin() + " обновлен");
            return newUser;
    }

    private long getNextId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
