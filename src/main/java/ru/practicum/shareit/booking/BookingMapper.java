package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingInfoDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

public class BookingMapper {

    public static BookingDto toBookingDto(Booking booking) {
        return new BookingDto(
                booking.getId(),
                booking.getItem().getId(),
                booking.getStart(),
                booking.getEnd()
        );
    }

    public static BookingInfoDto toBookingInfoDto(Booking booking) {
        return new BookingInfoDto(
                booking.getId(),
                booking.getStatus(),
                booking.getStart(),
                booking.getEnd(),
                new BookingInfoDto.UserInfoDto(
                        booking.getBooker().getId()
                ),
                new BookingInfoDto.ItemInfoDto(
                        booking.getItem().getId(),
                        booking.getItem().getName()
                )
        );
    }

    public static Booking toBooking(BookingDto bookingDto, User booker, Item item, BookingStatus status) {
        Booking booking = new Booking();
        booking.setStart(bookingDto.getStart());
        booking.setEnd(bookingDto.getEnd());
        booking.setBooker(booker);
        booking.setItem(item);
        booking.setStatus(status);
        return booking;
    }
}
