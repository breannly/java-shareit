package ru.practicum.shareit.request;

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
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestInfoDto;
import ru.practicum.shareit.request.dto.RequestInfoDto;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemRequestController.class)
@AutoConfigureMockMvc
class ItemRequestControllerTest {

    @MockBean
    ItemRequestService itemRequestService;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;

    private static final String SHAREIT_HEADER = "X-Sharer-User-Id";

    @Test
    void addRequestTest() throws Exception {
        ItemRequestDto itemRequestDto = new ItemRequestDto("test");
        RequestInfoDto requestInfoDto = new RequestInfoDto(1L, "test", LocalDateTime.now());

        Mockito.when(itemRequestService.addRequest(Mockito.any(Long.class), Mockito.any(ItemRequestDto.class)))
                .thenReturn(requestInfoDto);

        mockMvc.perform(post("/requests")
                        .header(SHAREIT_HEADER, 1L)
                        .content(mapper.writeValueAsString(itemRequestDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Matchers.is(requestInfoDto.getId()), Long.class))
                .andExpect(jsonPath("$.description",
                        Matchers.is(requestInfoDto.getDescription()),
                        String.class));

        Mockito.verify(itemRequestService, Mockito.times(1))
                .addRequest(Mockito.any(Long.class), Mockito.any(ItemRequestDto.class));
    }

    @Test
    void getRequestByUserTest() throws Exception {
        ItemRequestInfoDto request = new ItemRequestInfoDto(1L,
                "test",
                LocalDateTime.now(),
                new ArrayList<>()
        );

        List<ItemRequestInfoDto> requests = List.of(request);

        Mockito.when(itemRequestService.getRequestsByUser(Mockito.any(Long.class)))
                .thenReturn(requests);

        mockMvc.perform(get("/requests")
                        .header(SHAREIT_HEADER, 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(1)))
                .andExpect(jsonPath("$.[0].id", Matchers.is(request.getId()), Long.class))
                .andExpect(jsonPath("$.[0].description",
                        Matchers.is(request.getDescription()),
                        String.class));

        Mockito.verify(itemRequestService, Mockito.times(1))
                .getRequestsByUser(Mockito.any(Long.class));
    }

    @Test
    void getRequestById() throws Exception {
        ItemRequestInfoDto requestInfoDto = new ItemRequestInfoDto(
                1L,
                "test",
                LocalDateTime.now(),
                new ArrayList<>()
        );

        Mockito.when(itemRequestService.getRequestById(Mockito.anyLong(), Mockito.anyLong()))
                .thenReturn(requestInfoDto);

        mockMvc.perform(get("/requests/1")
                        .header(SHAREIT_HEADER, 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Matchers.is(requestInfoDto.getId()), Long.class))
                .andExpect(jsonPath("$.description",
                        Matchers.is(requestInfoDto.getDescription()),
                        String.class));

        Mockito.verify(itemRequestService, Mockito.times(1))
                .getRequestById(Mockito.anyLong(), Mockito.anyLong());
    }
}