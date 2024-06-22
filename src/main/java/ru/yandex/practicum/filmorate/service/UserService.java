package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;
import java.util.Set;

@Service
public class UserService implements UserStorage {


    InMemoryUserStorage memoryUserStorage;

    public UserService(InMemoryUserStorage memoryUserStorage) {
        this.memoryUserStorage = memoryUserStorage;
    }

    @Override
    public User createUser(User user) {
        return memoryUserStorage.createUser(user);

    }

    @Override
    public Collection<User> findAllUsers() {
        return memoryUserStorage.findAllUsers();
    }

    @Override
    public User updateUser(User user) {
        return memoryUserStorage.updateUser(user);
    }

    @Override
    public void addFriend(Long userId, Long friendId) {
        memoryUserStorage.addFriend(userId, friendId);
    }

    @Override
    public void deleteFriend(Long userId, Long friendId) {
        memoryUserStorage.deleteFriend(userId, friendId);
    }

    @Override
    public Set<Long> getUserFriends(Long userId) {
        return memoryUserStorage.getUserFriends(userId);
    }

    @Override
    public Set<Long> getCommonFriends(Long userId, Long otherUserId) {
        return memoryUserStorage.getCommonFriends(userId, otherUserId);
    }
}
