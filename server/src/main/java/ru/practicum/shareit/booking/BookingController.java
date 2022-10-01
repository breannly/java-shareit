package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingInfoDto;
import ru.practicum.shareit.booking.model.BookingState;

import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    public BookingDto booking(@RequestBody BookingDto bookingDto,
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
                                                        @RequestParam(value = "state") BookingState state,
                                                        @RequestParam(value = "from") int from,
                                                        @RequestParam(value = "size") int size) {
        return bookingService.getBookingBookerByState(userId, state, from, size);

    }

    @GetMapping("/owner")
    public List<BookingInfoDto> getBookingOwnerByState(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                       @RequestParam(value = "state") BookingState state,
                                                       @RequestParam(value = "from") int from,
                                                       @RequestParam(value = "size") int size) {
        return bookingService.getBookingOwnerByState(userId, state, from, size);
    }
}
