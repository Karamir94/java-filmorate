package ru.yandex.practicum.filmorate.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.user.DBFriendsRepository;
import ru.yandex.practicum.filmorate.repository.user.FriendsRepository;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@JdbcTest
@Import(DBFriendsRepository.class)
public class JdbcFriendsRepositoryTest {

    @Autowired
    private FriendsRepository friendsRepository;

    @Test
    public void shouldAddFriendAndGetFriendList() {
        friendsRepository.addFriend(1, 2);

        List<User> friendList = friendsRepository.getUsersFriends(1);

        assertEquals(1, friendList.size());
        assertEquals(2L, friendList.get(0).getId());
        assertEquals("user2@ya.ru", friendList.get(0).getEmail());
        assertEquals("user2Login", friendList.get(0).getLogin());
        assertEquals("user2Name", friendList.get(0).getName());
        assertEquals(LocalDate.of(1999, 9, 9), friendList.get(0).getBirthday());
    }

    @Test
    public void shouldGetCommonFriends() {
        friendsRepository.addFriend(2, 1);
        friendsRepository.addFriend(3, 1);

        List<User> commonFriendsList = friendsRepository.getCommonFriends(2, 3);

        assertThat(commonFriendsList.size())
                .isEqualTo(1);
        assertThat(commonFriendsList.get(0).getId())
                .isEqualTo(1);
    }
}
