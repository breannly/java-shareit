package ru.practicum.shareit.user.repo;

import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserRepo {

    List<User> getAll();

    User save(User user);

    User update(Long userId, User user);

    User getById(Long userId);

    void delete(Long userId);

    boolean checkEmailExists(String email);

    boolean checkUserExists(Long userId);
}
