package ru.yandex.practicum.filmorate.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.user.DBUserRepository;
import ru.yandex.practicum.filmorate.repository.user.UserRepository;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@JdbcTest
@Import(DBUserRepository.class)
public class JdbcUserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void shouldCreateAndFindUserById() {
        User user2 = User.builder()
                .email("user@ya.ru")
                .login("userLogin")
                .name("userName")
                .birthday(LocalDate.of(1999, 9, 9))
                .build();

        userRepository.create(user2);
        User user = userRepository.get(4).get();

        assertNotNull(user);
        assertEquals(4, user.getId());
        assertEquals("user@ya.ru", user.getEmail());
        assertEquals("userLogin", user.getLogin());
        assertEquals("userName", user.getName());
        assertEquals(LocalDate.of(1999, 9, 9), user.getBirthday());
    }

    @Test
    public void shouldUpdateUser() {
        User user1 = User.builder()
                .id(1L)
                .email("newUser@ya.ru")
                .login("newUserLogin")
                .name("newUserName")
                .birthday(LocalDate.of(1990, 1, 5))
                .build();

        userRepository.update(user1);
        User user = userRepository.get(1).get();

        assertNotNull(user);
        assertEquals(1, user.getId());
        assertEquals("newUser@ya.ru", user.getEmail());
        assertEquals("newUserLogin", user.getLogin());
        assertEquals("newUserName", user.getName());
        assertEquals(LocalDate.of(1990, 1, 5), user.getBirthday());
    }

    @Test
    public void shouldGetAllUsers() {
        List<User> users = userRepository.getAll();

        assertNotNull(users);
        assertEquals(3, users.size());
        assertEquals(1, users.get(0).getId());
        assertEquals("user1@ya.ru", users.get(0).getEmail());
        assertEquals("user1Login", users.get(0).getLogin());
        assertEquals("user1Name", users.get(0).getName());
        assertEquals(LocalDate.of(1999, 9, 9), users.get(0).getBirthday());
    }
}
