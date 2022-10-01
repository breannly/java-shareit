package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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
    public UserDto save(@RequestBody UserDto userDto) {
        return userService.save(userDto);
    }

    @PatchMapping("/{user_id}")
    public UserDto update(@PathVariable("user_id") Long userId,
                          @RequestBody UserDto userDto) {
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
