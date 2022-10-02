package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingInfoDto;
import ru.practicum.shareit.booking.model.BookingState;

import java.util.List;

public interface BookingService {

    BookingDto bookItem(BookingDto bookingDto, Long userId);

    BookingInfoDto approveBooking(Long userId, Long bookingId, Boolean approved);

    BookingInfoDto getBooking(Long userId, Long bookingId);

    List<BookingInfoDto> getBookings(Long userId, BookingState state, int from, int size);

    List<BookingInfoDto> getOwnerBookings(Long userId, BookingState state, int from, int size);
}
