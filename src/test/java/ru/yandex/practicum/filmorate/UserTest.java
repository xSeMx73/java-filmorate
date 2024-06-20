package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

public class UserTest {

    UserController userController = new UserController();

    @Test
    void testUserLogin() {
        User user = new User();
        user.setEmail("111@111");
        user.setLogin("Login");
        user.setName("Name");
        user.setBirthday(LocalDate.of(2020, 1, 1));
        userController.create(user);
    }

    @Test
    void testUserBirthday() {
        User user = new User();
        user.setEmail("111@111");
        user.setLogin("Login");
        user.setName("Name");
        user.setBirthday(LocalDate.of(2020, 1, 1));
        userController.create(user);
    }

    @Test
    void testUserEmail() {
        User user = new User();
        user.setEmail("111@1111");
        user.setLogin("Login");
        user.setName("Name");
        user.setBirthday(LocalDate.of(2020, 1, 1));
        User user1 = userController.create(user);
        user1.setName("name3");

    }
}