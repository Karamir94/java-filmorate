package ru.yandex.practicum.filmorate.repository.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface FriendsRepository {
    void addFriend(long userId, long friendId);

    boolean deleteFriend(long userId, long friendId);

    boolean checkUserInFriends(long filmId, long userId);

    List<User> getUsersFriends(long userId);

    List<User> getCommonFriends(long user1Id, long user2Id);
}
