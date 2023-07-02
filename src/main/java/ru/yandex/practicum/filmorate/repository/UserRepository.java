package ru.yandex.practicum.filmorate.repository;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface UserRepository {

    List<User> getAll();

    void create(User user);

    User update(User user);

    Optional<User> get(long userId);

    void addFriend(long userId, long friendId);

    void deleteFriend(long userId, long friendId);

    Set<Long> getFriends(long userId);
}
