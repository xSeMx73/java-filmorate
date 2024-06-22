package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.time.LocalDate;

public class UserTest {
    InMemoryUserStorage memoryUserStorage = new InMemoryUserStorage();
    UserService userService = new UserService(memoryUserStorage);
    UserController userController = new UserController(userService);

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
    @Test
    void testAddFriend() {
        User user = new User();
        user.setEmail("111@111");
        user.setLogin("Login");
        user.setName("Name");
        user.setBirthday(LocalDate.of(2020, 1, 1));
        userController.create(user);
        User user2 = new User();
        user2.setEmail("222@222");
        user2.setLogin("Login2");
        user2.setName("Name2");
        user2.setBirthday(LocalDate.of(2022, 2, 2));
        userController.create(user2);
        userController.addFriend(user.getId(), user2.getId());
        System.out.println(user.getId().toString() + userController.getUserFriends(user.getId()));
        System.out.println(user2.getId().toString() + userController.getUserFriends(user2.getId()));
    }

    @Test
    void getCommonFriends() {
        User user = new User();
        user.setEmail("111@111");
        user.setLogin("Login");
        user.setName("Name");
        user.setBirthday(LocalDate.of(2020, 1, 1));
        userController.create(user);
        User user2 = new User();
        user2.setEmail("222@222");
        user2.setLogin("Login2");
        user2.setName("Name2");
        user2.setBirthday(LocalDate.of(2022, 2, 2));
        userController.create(user2);
        User user3 = new User();
        user3.setEmail("333@333");
        user3.setLogin("Login3");
        user3.setName("Name3");
        user3.setBirthday(LocalDate.of(2020, 1, 1));
        userController.create(user3);
        User user4 = new User();
        user4.setEmail("444@444");
        user4.setLogin("Login4");
        user4.setName("Name4");
        user4.setBirthday(LocalDate.of(2022, 2, 2));
        userController.create(user4);
        User user5 = new User();
        user5.setEmail("555@444");
        user5.setLogin("Login5");
        user5.setName("Name5");
        user5.setBirthday(LocalDate.of(2022, 2, 2));
        userController.create(user5);
        User user6 = new User();
        user6.setEmail("666@444");
        user6.setLogin("Login6");
        user6.setName("Name6");
        user6.setBirthday(LocalDate.of(2022, 2, 2));
        userController.create(user6);
        userController.addFriend(user.getId(),user2.getId());
        userController.addFriend(user3.getId(),user2.getId());
        userController.addFriend(user.getId(),user4.getId());
        userController.addFriend(user3.getId(),user4.getId());
        userController.addFriend(user.getId(),user5.getId());
        userController.addFriend(user.getId(),user6.getId());
        System.out.println("frinds user 1" + user.getFriends());
        System.out.println("frinds user 2" + user2.getFriends());
        System.out.println("frinds user 3" + user3.getFriends());
        System.out.println("frinds user 4" + user4.getFriends());
        System.out.println("frinds user 5" + user5.getFriends());
        System.out.println("frinds user 6" + user6.getFriends());
        System.out.println(userController.getUserFriends(1L));
        userController.deleteFriend(1L, 6L);
        System.out.println(userController.getUserFriends(1L));
        System.out.println("frinds user 6" + user6.getFriends());
    }

}