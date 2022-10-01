package ru.practicum.shareit.booking;

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
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingInfoDto;
import ru.practicum.shareit.booking.model.BookingState;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookingController.class)
@AutoConfigureMockMvc
class BookingControllerTest {

    @MockBean
    BookingService bookingService;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;

    private static final String SHAREIT_HEADER = "X-Sharer-User-Id";

    @Test
    void bookingTest() throws Exception {
        BookingDto bookingDto = new BookingDto(null,
                1L,
                LocalDateTime.now().plusHours(1),
                LocalDateTime.now().plusHours(1));

        Mockito.when(bookingService.booking(Mockito.any(BookingDto.class), Mockito.anyLong()))
                .thenReturn(bookingDto);

        mockMvc.perform(post("/bookings")
                        .header(SHAREIT_HEADER, 1L)
                        .content(mapper.writeValueAsString(bookingDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.itemId", Matchers.is(bookingDto.getItemId()), Long.class));
    }

    @Test
    void approveTest() throws Exception {
        BookingInfoDto bookingInfoDto = new BookingInfoDto(1L,
                BookingState.APPROVED,
                LocalDateTime.now(),
                LocalDateTime.now(),
                new BookingInfoDto.UserInfoDto(1L),
                new BookingInfoDto.ItemInfoDto(1L, "test"));

        Mockito.when(bookingService.approve(Mockito.anyLong(), Mockito.anyLong(), Mockito.anyBoolean()))
                .thenReturn(bookingInfoDto);

        mockMvc.perform(patch("/bookings/1?approved=true")
                        .header(SHAREIT_HEADER, 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Matchers.is(bookingInfoDto.getId()), Long.class))
                .andExpect(jsonPath("$.status",
                        Matchers.is(bookingInfoDto.getStatus().toString()),
                        Boolean.class))
                .andExpect(jsonPath("$.booker.id",
                        Matchers.is(bookingInfoDto.getBooker().getId()),
                        Long.class))
                .andExpect(jsonPath("$.item.id",
                Matchers.is(bookingInfoDto.getItem().getId()),
                Long.class));
    }
}