package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

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
       validate(user, "добавления");
         return user;
    }

    @PutMapping
    public User update(@RequestBody User newUser) {
        if (newUser.getId() == null || !users.containsKey(newUser.getId())) {
            log.trace("Ошибка обновления пользователя");
            throw new ValidationException("Неверный ID");
        } else {
            validate(newUser,"обновления");
        }
            return newUser;
    }

    private void validate(User user, String operation) {
        if (user.getEmail() == null || !user.getEmail().contains("@")) {
            log.trace("Ошибка " + operation + " пользователя");
            throw new ValidationException("Неверно указана электронная почта");
        }
        for (User us : users.values()) {
            Optional<Boolean> optionalId = Optional.of(us.getId().equals(user.getId()));
            if (us.getEmail().equals(user.getEmail()) && !optionalId.get()) {
                log.trace("Ошибка " + operation + " пользователя");
                throw new ValidationException("Данная электронная почта уже используется");
            }
        }
        if (user.getLogin() == null || user.getLogin().isBlank()) {
            log.trace("Ошибка " + operation + " пользователя");
            throw new ValidationException("Логин не может быть пустым и содержать пробелы");

        } else if (user.getBirthday().isAfter(LocalDate.now())) {
            log.trace("Ошибка " + operation + " пользователя");
            throw new ValidationException("Неверная дата рождения");
        }  else if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        if (operation.equals("добавления")) {
            user.setId(getNextId());
        }
        users.put(user.getId(), user);
        log.trace("Пользователь " + user.getLogin() + operation.substring(0,operation.length() - 2));

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
