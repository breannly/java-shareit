package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.shareit.booking.model.BookingState;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookingInfoDto {

    private Long id;
    private BookingState status;
    private LocalDateTime start;
    private LocalDateTime end;
    private UserInfoDto booker;
    private ItemInfoDto item;

    public static class UserInfoDto {

        private final Long id;

        public UserInfoDto(Long id) {
            this.id = id;
        }

        public Long getId() {
            return id;
        }
    }

    public static class ItemInfoDto {

        private final Long id;
        private final String name;

        public ItemInfoDto(Long id, String name) {
            this.id = id;
            this.name = name;
        }

        public Long getId() {
            return id;
        }

        public String getName() {
            return name;
        }
    }
}
