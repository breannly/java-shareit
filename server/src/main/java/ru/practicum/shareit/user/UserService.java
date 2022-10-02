package ru.practicum.shareit.user;

import java.util.List;

public interface UserService {

    List<UserDto> getUsers();

    UserDto saveUser(UserDto userDto);

    UserDto updateUser(Long userId, UserDto userDto);

    UserDto getUser(Long userId);

    void deleteUser(Long userId);
}
