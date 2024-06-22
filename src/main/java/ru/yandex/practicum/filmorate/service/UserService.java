package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserService implements UserStorage {


    UserStorage userStorage;

    public UserService(InMemoryUserStorage memoryUserStorage) {
        this.userStorage = memoryUserStorage;
    }

    @Override
    public User createUser(User user) {
        return userStorage.createUser(user);
    }

    @Override
    public User getUser(Long id) {
        return userStorage.getUser(id);
    }

    @Override
    public Collection<User> findAllUsers() {
        return userStorage.findAllUsers();
    }

    @Override
    public User updateUser(User user) {
        return userStorage.updateUser(user);
    }

    @Override
    public User addFriend(Long userId, Long friendId) {
        if (userId == null || friendId == null || !userStorage.findAllUsers().contains(getUser(userId))
                || !userStorage.findAllUsers().contains(getUser(friendId))) {
            log.trace("Ошибка добавления в друзья");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Неверный ID");
        }
        getUser(userId).getFriends().add(friendId);
        getUser(friendId).getFriends().add(userId);
        return getUser(userId);
    }

    @Override
    public User deleteFriend(Long userId, Long friendId) {
        if (userId == null || friendId == null) {
            log.trace("Ошибка удаления из друзей");
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, "Неверный ID");
        } else if (!findAllUsers().contains(getUser(userId)) || !findAllUsers().contains(getUser(friendId))) {
            log.trace("Ошибка удаления из друзей");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Неверный ID");
        } else {
            getUser(userId).getFriends().remove(friendId);
            getUser(friendId).getFriends().remove(userId);
        }
        return userStorage.getUser(userId);
    }

    @Override
    public Set<User> getUserFriends(Long userId) {
        if (userId == null || !userStorage.findAllUsers().contains(getUser(userId))) {
            log.trace("Ошибка запроса друзей пользователя");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Неверный ID");
        }
        return findAllUsers().stream()
                .filter(user -> user.getFriends().contains(userId))
                .collect(Collectors.toSet());
    }

    @Override
    public Set<User> getCommonFriends(Long userId, Long otherUserId) {
        if (userId == null || otherUserId == null || !userStorage.findAllUsers().contains(userStorage.getUser(userId))
                || !userStorage.findAllUsers().contains(userStorage.getUser(otherUserId))) {
            log.trace("Ошибка поиска общих друзей");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Неверный ID");
        }
        Set<User> commonFriends = getUserFriends(userId);
        commonFriends.retainAll(getUserFriends(otherUserId));
        return commonFriends;
    }

//    public boolean validateForFriend(Long userId, Long friendId) {
//        return userId != null || friendId != null || userStorage.findAllUsers().contains(getUser(userId))
//                || userStorage.findAllUsers().contains(getUser(friendId));
//    }
}
