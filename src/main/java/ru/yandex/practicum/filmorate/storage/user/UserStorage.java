package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Set;

public interface UserStorage {
    User createUser(User user);

    Collection<User> findAllUsers();

    User updateUser(User user);

    User addFriend(Long userId, Long friendId);

    User deleteFriend(Long userId, Long friendId);

    Set<User> getUserFriends(Long id);

    Set<User> getCommonFriends(Long userId, Long otherUserId);

    User getUser(Long id);
}
