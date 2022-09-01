package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface UserService {

    List<UserDto> getAll();

    UserDto save(UserDto userDto);

    UserDto update(Long userId, UserDto userDto);

    UserDto getById(Long userId);

    void delete(Long userId);
}
