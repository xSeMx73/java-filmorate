package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.Collection;
import java.util.Optional;


@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @GetMapping
    public Collection<User> findAll() {
        return userService.findAllUsers();
    }

    @PostMapping
    public User create(@RequestBody User user) {
        return userService.createUser(user);
    }

    @GetMapping("/users/{id}")
    public Optional<User> getUser(@PathVariable Long id) {
        return userService.getUser(id);
    }

    @PutMapping
    public User update(@RequestBody User user) {
        return userService.updateUser(user);
    }

    @PutMapping("/{userId}/friends/{friendId}")
    public void addFriend(@PathVariable Long userId, @PathVariable Long friendId) {
        userService.addFriend(userId, friendId);
    }

    @DeleteMapping("/{userId}/friends/{friendId}")
    public void deleteFriend(@PathVariable Long userId, @PathVariable Long friendId) {
        userService.deleteFriend(userId, friendId);
    }

    @GetMapping("/{userId}/friends")
    public Collection<User> getUserFriends(@PathVariable Long userId) {
        return userService.getUserFriends(userId);

    }

    @GetMapping("/{userId}/friends/common/{otherUserId}")
    public Collection<User> getCommonFriends(@PathVariable Long userId, @PathVariable Long otherUserId) {
        return userService.getCommonFriends(userId, otherUserId);
    }
}
