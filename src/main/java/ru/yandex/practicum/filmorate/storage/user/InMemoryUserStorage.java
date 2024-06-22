package ru.yandex.practicum.filmorate.storage.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.*;

@Slf4j
@Component
@Getter
@RequiredArgsConstructor
public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> users = new HashMap<>();

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
        if (userId == null || friendId == null || !users.containsKey(userId) || !users.containsKey(friendId)) {
            log.trace("Ошибка добавления в друзья");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Неверный ID");
        }
        users.get(userId).getFriends().add(friendId);
        users.get(friendId).getFriends().add(userId);
    }

    @Override
    public void deleteFriend(Long userId, Long friendId) {
        if (userId == null || friendId == null) {
            log.trace("Ошибка удаления из друзей");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Неверный ID");
        } else if (!users.containsKey(userId) || !users.get(userId).getFriends().contains(friendId)) {
            log.trace("Ошибка удаления из друзей");
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, "Неверный ID");
        }
        users.get(userId).getFriends().remove(friendId);
        users.get(friendId).getFriends().remove(userId);
    }

    @Override
    public Set<Long> getUserFriends(Long userId) {
        if (userId == null || !users.containsKey(userId) || users.get(userId).getFriends() == null) {
            log.trace("Ошибка обновления пользователя");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Неверный ID");
        }
        return users.get(userId).getFriends();
    }

    @Override
    public Set<Long> getCommonFriends(Long userId, Long otherUserId) {
        if (userId == null || otherUserId == null || !users.containsKey(userId) || !users.containsKey(otherUserId)) {
            log.trace("Ошибка поиска общих друзей");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Неверный ID");
        }
        Set<Long> commonFriends = users.get(userId).getFriends();
        commonFriends.retainAll(users.get(otherUserId).getFriends());
        return commonFriends;
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
