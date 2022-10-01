package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.common.Create;
import ru.practicum.shareit.common.Update;

@Slf4j
@Controller
@Validated
@RequiredArgsConstructor
@RequestMapping(path = "/users")
public class UserController {

    private final UserClient userClient;

    @GetMapping
    public ResponseEntity<Object> getUsers() {
        return userClient.getUsers();
    }

    @PostMapping
    public ResponseEntity<Object> saveUser(@Validated({Create.class}) @RequestBody UserDto userDto) {
        return userClient.saveUser(userDto);
    }

    @PatchMapping("/{user_id}")
    public ResponseEntity<Object> updateUser(@PathVariable("user_id") Long userId,
                          @Validated({Update.class}) @RequestBody UserDto userDto) {
        return userClient.updateUser(userId, userDto);
    }

    @GetMapping("/{user_id}")
    public ResponseEntity<Object> getUser(@PathVariable("user_id") Long userId) {
        return userClient.getUser(userId);
    }

    @DeleteMapping("/{user_id}")
    public ResponseEntity<Object> deleteUser(@PathVariable("user_id") Long userId) {
        return userClient.deleteUser(userId);
    }
}
