package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Optional;

@Component
public interface UserService {
    User createUser(User user);

    Collection<User> findAllUsers();

    User updateUser(User user);

    void addFriend(Long userId, Long friendId);

    void deleteFriend(Long userId, Long friendId);

    Collection<User> getUserFriends(Long id);

    Collection<User> getCommonFriends(Long userId, Long otherUserId);

    Optional<User> getUser(Long id);
}
