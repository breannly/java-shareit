package ru.practicum.shareit.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.entity.Create;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users")
public class UserController {

    private final UserService userService;

    @GetMapping
    public List<UserDto> getAll() {
        return userService.getAll();
    }

    @PostMapping
    public UserDto save(@Validated({Create.class}) @RequestBody UserDto userDto) {
        return userService.save(userDto);
    }

    @PatchMapping("/{user_id}")
    public UserDto update(@PathVariable("user_id") Long userId, @RequestBody UserDto userDto) {
        return userService.update(userId, userDto);
    }

    @GetMapping("/{user_id}")
    public UserDto getById(@PathVariable("user_id") Long userId) {
        return userService.getById(userId);
    }

    @DeleteMapping("/{user_id}")
    public void delete(@PathVariable("user_id") Long userId) {
        userService.delete(userId);
    }
}
