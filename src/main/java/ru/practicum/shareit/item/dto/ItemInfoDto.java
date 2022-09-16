package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.shareit.booking.model.Booking;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ItemInfoDto {

    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private BookingInfoDto lastBooking;
    private BookingInfoDto nextBooking;
    private List<CommentInfoDto> comments;

    public static class BookingInfoDto {

        private final Long id;
        private final Long bookerId;

        private BookingInfoDto(Long id, Long bookerId) {
            this.id = id;
            this.bookerId = bookerId;
        }

        public static BookingInfoDto create(Booking booking) {
            if (booking == null) {
                return null;
            }
            return new BookingInfoDto(
                    booking.getId(),
                    booking.getBooker().getId());
        }

        public Long getId() {
            return id;
        }

        public Long getBookerId() {
            return bookerId;
        }
    }
}
