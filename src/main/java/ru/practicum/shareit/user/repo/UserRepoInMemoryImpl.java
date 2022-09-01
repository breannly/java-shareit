package ru.practicum.shareit.user.repo;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class UserRepoInMemoryImpl implements UserRepo {

    private final Map<Long, User> users = new HashMap<>();

    @Override
    public List<User> getAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User save(User user) {
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User update(Long userId, User user) {
        User foundUser = users.get(userId);

        if (user.getName() != null) {
            foundUser.setName(user.getName());
        }
        if (user.getEmail() != null) {
            foundUser.setEmail(user.getEmail());
        }
        users.put(userId, foundUser);

        return foundUser;
    }

    @Override
    public User getById(Long userId) {
        return users.get(userId);
    }

    @Override
    public void delete(Long userId) {
        users.remove(userId);
    }

    @Override
    public boolean checkEmailExists(String email) {
        return users.values().stream().anyMatch(user -> user.getEmail().equals(email));
    }

    @Override
    public boolean checkUserExists(Long userId) {
        return users.containsKey(userId);
    }
}
