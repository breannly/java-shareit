package ru.practicum.shareit.user;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.practicum.shareit.exception.ObjectNotFoundException;

import java.util.Optional;

class UserServiceImplTest {

    UserRepository userRepository;
    UserService userService;

    @BeforeEach
    void beforeEach() {
        userRepository = Mockito.mock(UserRepository.class);
        userService = new UserServiceImpl(userRepository);
    }

    @Test
    void shouldSaveUserWithRightParameters() {
        User user = new User(1L, "test", "test@gmail.com");
        UserDto userDto = new UserDto(null, "test", "test@gmail.com");

        Mockito.when(userRepository.save(Mockito.any(User.class)))
                .thenReturn(user);

        UserDto foundUser = userService.saveUser(userDto);

        Assertions.assertNotNull(foundUser);
        Assertions.assertEquals(user.getId(), foundUser.getId());
        Assertions.assertEquals(user.getName(), foundUser.getName());
        Assertions.assertEquals(user.getEmail(), foundUser.getEmail());
    }

    @Test
    void shouldUpdateUserWithRightParameters() {
        User user = new User(1L, "test", "test@gmail.com");
        User updateUser = new User(1L, "testUpdate", "testUpdate@gmail.com");
        UserDto userDto = new UserDto(null, "testUpdate", "testUpdate@gmail.com");

        Mockito.when(userRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(user));

        Mockito.when(userRepository.save(Mockito.any(User.class)))
                .thenReturn(updateUser);

        UserDto foundUser = userService.updateUser(user.getId(), userDto);

        Assertions.assertNotNull(foundUser);
        Assertions.assertEquals(updateUser.getId(), foundUser.getId());
        Assertions.assertEquals(updateUser.getName(), foundUser.getName());
        Assertions.assertEquals(updateUser.getEmail(), foundUser.getEmail());
    }

    @Test
    void shouldThrowExceptionUsingUpdateWhenWrongUserId() {
        UserDto userDto = new UserDto(null, "testUpdate", "testUpdate@gmail.com");

        Mockito.when(userRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.empty());

        Exception exception = Assertions.assertThrows(ObjectNotFoundException.class,
                () -> userService.updateUser(1L, userDto));

        Assertions.assertEquals("User not found", exception.getMessage());
    }

    @Test
    void shouldReturnUserById() {
        User user = new User(1L, "test", "test@gmail.com");

        Mockito.when(userRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(user));

        UserDto foundUser = userService.getUser(user.getId());

        Assertions.assertNotNull(foundUser);
        Assertions.assertEquals(user.getId(), foundUser.getId());
        Assertions.assertEquals(user.getName(), foundUser.getName());
        Assertions.assertEquals(user.getEmail(), foundUser.getEmail());
    }

    @Test
    void shouldThrowExceptionUsingGetByIdWhenWrongUserId() {
        Mockito.when(userRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.empty());

        Exception exception = Assertions.assertThrows(ObjectNotFoundException.class,
                () -> userService.getUser(1L));

        Assertions.assertEquals("User not found", exception.getMessage());
    }
}