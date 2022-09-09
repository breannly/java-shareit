package ru.practicum.shareit.booking.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query("SELECT b FROM Booking b " +
            "WHERE b.item.owner.id = :userId AND " +
            "b.start < :now AND b.end > :now " +
            "ORDER BY b.start DESC")
    List<Booking> findBookingOwnerByCurrentState(Long userId, LocalDateTime now);

    @Query("SELECT b FROM  Booking b " +
            "WHERE b.item.id = :itemId AND b.item.owner.id = :userId AND b.status = 'APPROVED' AND b.end < :now " +
            "ORDER BY b.start")
    Booking findLastBooking(LocalDateTime now, Long userId, Long itemId);

    @Query("SELECT b FROM  Booking b " +
            "WHERE b.item.id = :itemId AND b.item.owner.id = :userId AND b.status = 'APPROVED' AND b.start > :now " +
            "ORDER BY b.start")
    Booking findNextBooking(LocalDateTime now, Long userId, Long itemId);

    @Query("SELECT b FROM Booking b " +
            "WHERE b.booker.id = :userId AND " +
            "b.start < :now AND b.end > :now " +
            "ORDER BY b.start DESC")
    List<Booking> findBookingBookerByCurrentState(Long userId, LocalDateTime now);

    List<Booking> findBookingsByBookerIdOrderByStartDesc(Long userId);

    List<Booking> findBookingsByBookerIdAndEndBeforeOrderByStartDesc(Long userId, LocalDateTime now);

    List<Booking> findBookingsByBookerIdAndEndAfterOrderByStartDesc(Long userId, LocalDateTime now);

    List<Booking> findBookingsByBookerIdAndStatusOrderByStartDesc(Long userId, BookingStatus state);

    List<Booking> findBookingsByItemOwnerIdAndStatusOrderByStartDesc(Long userId, BookingStatus state);

    List<Booking> findBookingsByItemOwnerIdOrderByStartDesc(Long userId);

    List<Booking> findBookingsByItemOwnerIdAndEndBeforeOrderByStartDesc(Long userId, LocalDateTime now);

    List<Booking> findBookingsByItemOwnerIdAndEndAfterOrderByStartDesc(Long userId, LocalDateTime now);

    boolean existsBookingByItemIdAndBookerIdAndEndBefore(Long itemId, Long userId, LocalDateTime now);
}
