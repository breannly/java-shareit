package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.StandardCharsets;
import java.util.List;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc
class UserControllerTest {

    @MockBean
    UserService userService;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;

    @Test
    void getAllUsersTest() throws Exception {
        List<UserDto> users = List.of(UserMapper.toDto(new User(null, "test", "test@gmail.com")));

        Mockito.when(userService.getAll())
                .thenReturn(users);

        mockMvc.perform(get("/users")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(1)));

        Mockito.verify(userService, Mockito.times(1))
                .getAll();
    }

    @Test
    void saveUserTest() throws Exception {
        UserDto userDto = new UserDto(null, "test", "test@gmail.com");

        Mockito.when(userService.save(Mockito.any(UserDto.class)))
                .thenReturn(userDto);

        mockMvc.perform(post("/users")
                        .content(mapper.writeValueAsString(userDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", Matchers.is(userDto.getName()), String.class))
                .andExpect(jsonPath("$.email", Matchers.is(userDto.getEmail()), String.class));

        Mockito.verify(userService, Mockito.times(1))
                .save(Mockito.any(UserDto.class));
    }

    @Test
    void updateUserTest() throws Exception {
        UserDto userDto = new UserDto(null, "test", "test@gmail.com");

        Mockito.when(userService.update(Mockito.anyLong(), Mockito.any(UserDto.class)))
                .thenReturn(userDto);

        mockMvc.perform(patch("/users/1")
                        .content(mapper.writeValueAsString(userDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", Matchers.is(userDto.getName()), String.class))
                .andExpect(jsonPath("$.email", Matchers.is(userDto.getEmail()), String.class));

        Mockito.verify(userService, Mockito.times(1))
                .update(Mockito.anyLong(), Mockito.any(UserDto.class));
    }
}