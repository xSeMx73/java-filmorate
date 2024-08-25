package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.UserRepository;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;


@Slf4j
@Service
public class UserServiceImp implements UserService {

    private final UserRepository userRepository;

    public UserServiceImp(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User createUser(User user) {
        validate(user, "добавления");
        return userRepository.createUser(user);
    }

    @Override
    public Optional<User> getUser(Long id) {
        return userRepository.getUser(id);
    }

    @Override
    public Collection<User> findAllUsers() {
        return userRepository.findAllUsers();
    }

    @Override
    public User updateUser(User user) {
        if (user.getId() == null || userRepository.getUser(user.getId()).isEmpty()) {
            log.trace("Ошибка обновления пользователя");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Неверный ID");
        } else {
            validate(user, "обновления");
        }
        return userRepository.updateUser(user);
    }

    @Override
    public void addFriend(Long userId, Long friendId) {
        if (userId == null || friendId == null || userRepository.getUser(userId).isEmpty() || userRepository.getUser(friendId).isEmpty()) {
            log.trace("Ошибка добавления в друзья");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Неверный ID");
        } else {
            userRepository.addFriend(userId, friendId);
        }
    }

    @Override
    public void deleteFriend(Long userId, Long friendId) {
        if (userId == null || friendId == null) {
            log.trace("Ошибка удаления из друзей");
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, "Неверный ID");
        } else if (userRepository.getUser(userId).isEmpty() || userRepository.getUser(friendId).isEmpty()) {
            log.trace("Ошибка удаления из  друзей");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Неверный ID");
        } else {
            userRepository.deleteFriend(userId, friendId);
        }
    }

    @Override
    public List<User> getUserFriends(Long userId) {
        if (userId == null || userRepository.getUser(userId).isEmpty()) {
            log.trace("Ошибка запроса друзей пользователя");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Неверный ID");
        }
        return userRepository.getUserFriends(userId);
    }

    @Override
    public Collection<User> getCommonFriends(Long userId, Long otherUserId) {
        if (userId == null || otherUserId == null || userRepository.getUser(userId).isEmpty()
                || getUser(otherUserId).isEmpty()) {
            log.trace("Ошибка поиска общих друзей");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Неверный ID");
        }
        return userRepository.getCommonFriends(userId, otherUserId);
    }

    private void validate(User user, String operation) {
        if (user.getEmail() == null || !user.getEmail().contains("@")) {
            log.trace("Ошибка " + operation + " пользователя");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Неверно указана электронная почта");
        }
        for (User us : userRepository.findAllUsers()) {
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
    }

}
