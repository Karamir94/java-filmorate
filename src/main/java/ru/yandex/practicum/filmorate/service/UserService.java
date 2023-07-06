package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAll() {
        return userRepository.getAll();
    }

    public void create(User user) {
        userRepository.create(user);
    }

    public void update(User user) {
        get(user.getId());
        userRepository.update(user);
    }

    public User get(long userId) {
        User user = userRepository.get(userId)
                .orElseThrow(() -> new NotFoundException(String.format("User № %d not found", userId)));

        return user;
    }

    public void addFriend(long userId, long friendId) {
        checkId(userId);
        checkId(friendId);
        get(userId);
        get(friendId);

        userRepository.addFriend(userId, friendId);
    }

    private void checkId(long userId) {
        if (userId <= 0) {
            throw new NotFoundException("id must be positive");
        }
    }

    public void deleteFriend(long userId, long friendId) {
        checkId(userId);
        checkId(friendId);
        get(userId);
        get(friendId);

        userRepository.deleteFriend(userId, friendId);
    }

    public List<User> getFriends(long userId) {
        checkId(userId);
        get(userId);

        return userRepository.getFriends(userId)
                .stream()
                .map((i) -> userRepository.get(i).get())
                .collect(Collectors.toList());
    }

    public List<User> getCommonFriends(long user1Id, long user2Id) {
        List<User> user1Friends = getFriends(user1Id);
        return getFriends(user2Id)
                .stream()
                .filter(n -> {
                    boolean b = user1Friends.contains(n);
                    return b;
                })
                .collect(Collectors.toList());
    }
}
