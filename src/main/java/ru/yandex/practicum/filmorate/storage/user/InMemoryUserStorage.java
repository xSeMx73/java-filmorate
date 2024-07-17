package ru.yandex.practicum.filmorate.storage.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.time.LocalDate;
import java.util.*;

@Slf4j
@Component
@Getter
@RequiredArgsConstructor
public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> users = new HashMap<>();
    UserService userService;

    @Override
    public Collection<User> findAllUsers() {
        return users.values();
    }

    @Override
    public User createUser(User user) {
        validate(user, "добавления");
        return user;
    }

    @Override
    public User updateUser(User user) {
        if (user.getId() == null || !users.containsKey(user.getId())) {
            log.trace("Ошибка обновления пользователя");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Неверный ID");
        } else {
            validate(user, "обновления");
        }
        return user;
    }

    @Override
    public void addFriend(Long userId, Long friendId) {
        userService.addFriend(userId, friendId);
    }

    @Override
    public void deleteFriend(Long userId, Long friendId) {
        userService.deleteFriend(userId, friendId);
    }

    @Override
    public Set<User> getUserFriends(Long userId) {
        return userService.getUserFriends(userId);
    }

    @Override
    public Set<User> getCommonFriends(Long userId, Long otherUserId) {
        return userService.getCommonFriends(userId, otherUserId);
    }

    @Override
    public User getUser(Long id) {
        if (id == null || !users.containsKey(id)) {
            log.trace("Неверный Id пользователя");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Id должен быть указан");
        }
        return users.get(id);
    }

    private void validate(User user, String operation) {
        if (user.getEmail() == null || !user.getEmail().contains("@")) {
            log.trace("Ошибка " + operation + " пользователя");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Неверно указана электронная почта");
        }
        for (User us : users.values()) {
            Optional<Boolean> optionalId = Optional.of(us.getId().equals(user.getId()));
            if (us.getEmail().equals(user.getEmail()) && !optionalId.get()) {
                log.trace("Ошибка " + operation + " пользователя");
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Данная электронная почта уже используется");
            }
        }
        if (user.getLogin() == null || user.getLogin().isBlank()) {
            log.trace("Ошибка " + operation + " пользователя");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Логин не может быть пустым и содержать пробелы");

        } else if (user.getBirthday().isAfter(LocalDate.now())) {
            log.trace("Ошибка " + operation + " пользователя");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Неверная дата рождения");
        } else if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        if (operation.equals("добавления")) {
            user.setId(getNextId());
        }
        users.put(user.getId(), user);
        log.trace("Пользователь " + user.getLogin() + operation.substring(0, operation.length() - 2));

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
