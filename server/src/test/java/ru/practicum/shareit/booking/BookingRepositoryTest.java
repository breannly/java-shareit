package ru.practicum.shareit.booking;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Rollback;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repo.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@DataJpaTest
@Rollback(value = false)
class BookingRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    BookingRepository bookingRepository;

    User user1;
    User user2;
    User user3;
    Item item1;
    Booking booking1;
    Booking booking2;

    @BeforeEach
    void beforeEach() {
        user1 = userRepository.save(new User(null, "test1", "test1@mail.ru"));
        user2 = userRepository.save(new User(null, "test2", "test2@mail.ru"));
        user3 = userRepository.save(new User(null, "test3", "test3@mail.ru"));
        item1 = itemRepository
                .save(new Item(null, user2, "test", "test", true, null));
        booking1 = bookingRepository
                .save(new Booking(null,
                        LocalDateTime.now(),
                        LocalDateTime.now(),
                        BookingState.APPROVED,
                        user1,
                        item1));
        booking2 = bookingRepository
                .save(new Booking(null,
                        LocalDateTime.now(),
                        LocalDateTime.now(),
                        BookingState.APPROVED,
                        user3,
                        item1));
    }

    @Test
    void shouldReturnCurrentBookingsListByOwnerId() {
        booking1.setStart(booking1.getStart().minusHours(1));
        booking1.setEnd(booking1.getEnd().plusHours(1));
        bookingRepository.save(booking1);

        List<Booking> results = bookingRepository
                .findBookingOwnerByCurrentState(user2.getId(), LocalDateTime.now(), Pageable.unpaged());

        Assertions.assertNotNull(results);
        Assertions.assertEquals(1, results.size());
    }

    @Test
    void shouldReturnEmptyBookingsListByWrongOwnerId() {
        booking1.setStart(booking1.getStart().minusHours(1));
        booking1.setEnd(booking1.getEnd().plusHours(1));
        bookingRepository.save(booking1);

        List<Booking> results = bookingRepository
                .findBookingOwnerByCurrentState(user1.getId(), LocalDateTime.now(), Pageable.unpaged());

        Assertions.assertNotNull(results);
        Assertions.assertEquals(0, results.size());
    }

    @Test
    void shouldReturnCurrentBookingsListByBookerId() {
        booking1.setStart(booking1.getStart().minusHours(1));
        booking1.setEnd(booking1.getEnd().plusHours(1));
        bookingRepository.save(booking1);

        List<Booking> results = bookingRepository
                .findBookingBookerByCurrentState(user1.getId(), LocalDateTime.now(), Pageable.unpaged());

        Assertions.assertNotNull(results);
        Assertions.assertEquals(1, results.size());
    }

    @Test
    void shouldReturnEmptyBookingsListByWrongBookerId() {
        booking1.setStart(booking1.getStart().minusHours(1));
        booking1.setEnd(booking1.getEnd().plusHours(1));
        bookingRepository.save(booking1);

        List<Booking> results = bookingRepository
                .findBookingBookerByCurrentState(user2.getId(), LocalDateTime.now(), Pageable.unpaged());

        Assertions.assertNotNull(results);
        Assertions.assertEquals(0, results.size());
    }

    @Test
    void shouldReturnLastBooking() {
        booking2.setEnd(booking2.getEnd().plusHours(1));
        bookingRepository.save(booking2);

        Booking lastBooking = bookingRepository.findLastBooking(LocalDateTime.now(), user2.getId(), item1.getId());

        Assertions.assertNotNull(lastBooking);
        Assertions.assertEquals(lastBooking.getBooker().getName(), user1.getName());
    }

    @Test
    void shouldReturnNextBooking() {
        booking2.setStart(booking2.getStart().plusHours(1));
        booking2.setEnd(booking2.getEnd().plusHours(2));
        bookingRepository.save(booking2);

        Booking nextBooking = bookingRepository.findNextBooking(LocalDateTime.now(), user2.getId(), item1.getId());

        Assertions.assertNotNull(nextBooking);
        Assertions.assertEquals(nextBooking.getBooker().getName(), user3.getName());
    }

    @AfterEach
    void afterEach() {
        bookingRepository.deleteAll();
        itemRepository.deleteAll();
        userRepository.deleteAll();
    }
}