package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingInfoDto;
import ru.practicum.shareit.booking.model.BookingState;

import java.util.List;

public interface BookingService {

    BookingDto booking(BookingDto bookingDto, Long userId);

    BookingInfoDto approve(Long userId, Long bookingId, Boolean approved);

    BookingInfoDto getById(Long userId, Long bookingId);

    List<BookingInfoDto> getBookingBookerByState(Long userId, BookingState state, int from, int size);

    List<BookingInfoDto> getBookingOwnerByState(Long userId, BookingState state, int from, int size);
}
