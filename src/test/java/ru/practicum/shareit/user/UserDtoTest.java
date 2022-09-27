package ru.practicum.shareit.user;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import java.io.IOException;

@JsonTest
class UserDtoTest {

    @Autowired
    private JacksonTester<UserDto> jackson;

    @Test
    void serializeUserTest() throws IOException {
        User user = new User(1L, "test", "test@gmail.com");
        UserDto userDto = UserMapper.toDto(user);

        JsonContent<UserDto> json = jackson.write(userDto);

        Assertions.assertThat(json)
                .extractingJsonPathStringValue("$.name").isEqualTo(user.getName());
        Assertions.assertThat(json)
                .extractingJsonPathStringValue("$.email").isEqualTo(user.getEmail());
    }
}
