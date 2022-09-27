package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingInfoDto;
import ru.practicum.shareit.common.Create;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    public BookingDto booking(@Validated({Create.class}) @RequestBody BookingDto bookingDto,
                              @RequestHeader("X-Sharer-User-Id") Long userId) {
        return bookingService.booking(bookingDto, userId);
    }

    @PatchMapping("/{bookingId}")
    public BookingInfoDto approve(@RequestHeader("X-Sharer-User-Id") Long userId,
                                  @PathVariable Long bookingId,
                                  @RequestParam("approved") Boolean approved) {
        return bookingService.approve(userId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    public BookingInfoDto getById(@RequestHeader("X-Sharer-User-Id") Long userId,
                                  @PathVariable Long bookingId) {
        return bookingService.getById(userId, bookingId);
    }

    @GetMapping()
    public List<BookingInfoDto> getBookingBookerByState(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                        @RequestParam(defaultValue = "ALL") String state,
                                                        @RequestParam(value = "from", defaultValue = "0")
                                                        @PositiveOrZero int from,
                                                        @RequestParam(value = "size", defaultValue = "10")
                                                        @Positive int size) {
        return bookingService.getBookingBookerByState(userId, state, from, size);

    }

    @GetMapping("/owner")
    public List<BookingInfoDto> getBookingOwnerByState(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                       @RequestParam(defaultValue = "ALL") String state,
                                                       @RequestParam(value = "from", defaultValue = "0")
                                                       @PositiveOrZero int from,
                                                       @RequestParam(value = "size", defaultValue = "10")
                                                       @Positive int size) {
        return bookingService.getBookingOwnerByState(userId, state, from, size);
    }
}
