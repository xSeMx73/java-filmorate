package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Set;

public interface UserStorage {
    User createUser(User user);

    Collection<User> findAllUsers();

    User updateUser(User user);

    void addFriend(Long userId, Long friendId);

    void deleteFriend(Long userId, Long friendId);

    Set<Long> getUserFriends(Long id);

    Set<Long> getCommonFriends(Long userId, Long otherUserId);
}
