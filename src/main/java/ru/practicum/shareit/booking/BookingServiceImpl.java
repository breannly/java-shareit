package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingInfoDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.exception.ObjectNotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repo.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final BookingRepository bookingRepository;

    @Override
    public BookingDto booking(BookingDto bookingDto, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(()
                -> new ObjectNotFoundException("User doesn't exists"));
        Item item = itemRepository.findById(bookingDto.getItemId()).orElseThrow(()
                -> new ObjectNotFoundException("Item doesn't exists"));
        if (bookingDto.getStart().isAfter(bookingDto.getEnd())) {
            throw new ValidationException("Validation Failed");
        }
        if (item.getAvailable().equals(false)) {
            throw new ValidationException("Validation Failed");
        }
        if (item.getOwner().equals(user)) {
            throw new ObjectNotFoundException("Item has another owner");
        }
        Booking booking = BookingMapper.toBooking(bookingDto, user, item, BookingStatus.WAITING);
        return BookingMapper.toBookingDto(bookingRepository.save(booking));
    }

    @Override
    public BookingInfoDto approve(Long userId, Long bookingId, Boolean approved) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(()
                -> new ObjectNotFoundException("Booking not found"));
        User user = userRepository.findById(userId).orElseThrow(()
                -> new ObjectNotFoundException("User not found"));
        Item item = itemRepository.findById(booking.getItem().getId()).orElseThrow(()
                -> new ObjectNotFoundException("Item not found"));
        if (!item.getOwner().equals(user)) {
            throw new ObjectNotFoundException("User's booking information not found");
        }
        updateBookingStatus(booking, approved);
        return BookingMapper.toBookingInfoDto(bookingRepository.save(booking));
    }

    private void updateBookingStatus(Booking booking, Boolean approved) {
        if (approved && !booking.getStatus().equals(BookingStatus.APPROVED)) {
            booking.setStatus(BookingStatus.APPROVED);
        } else if (!approved && !booking.getStatus().equals(BookingStatus.REJECTED)) {
            booking.setStatus(BookingStatus.REJECTED);
        } else {
            throw new ValidationException("Status cannot be changed");
        }
    }

    @Override
    public BookingInfoDto getById(Long userId, Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(()
                -> new ObjectNotFoundException("Booking info not found"));
        User user = userRepository.findById(booking.getBooker().getId()).orElseThrow(()
                -> new ObjectNotFoundException("User doesn't exists"));
        Item item = itemRepository.findById(booking.getItem().getId()).orElseThrow(()
                -> new ObjectNotFoundException("Item doesn't exists"));
        if (!(user.getId().equals(userId) ||
                item.getOwner().getId().equals(userId))) {
            throw new ObjectNotFoundException("User's booking information was not found");
        }
        return BookingMapper.toBookingInfoDto(booking);
    }

    @Override
    public List<BookingInfoDto> getBookingBookerByState(Long userId, String state, int from, int size) {
        userRepository.findById(userId).orElseThrow(()
                -> new ObjectNotFoundException("User doesn't exists"));
        BookingStatus bookingStatus = BookingStatus.checkEnum(state);
        Pageable pageable = PageRequest.of(from / size, size);
        switch (bookingStatus) {
            case ALL:
                return bookingRepository.findBookingsByBookerIdOrderByStartDesc(userId, pageable)
                        .stream()
                        .map(BookingMapper::toBookingInfoDto)
                        .collect(Collectors.toList());
            case WAITING:
            case REJECTED:
                return bookingRepository.findBookingsByBookerIdAndStatusOrderByStartDesc(
                        userId, bookingStatus, pageable)
                        .stream()
                        .map(BookingMapper::toBookingInfoDto)
                        .collect(Collectors.toList());
            case PAST:
                return bookingRepository.findBookingsByBookerIdAndEndBeforeOrderByStartDesc(
                        userId, LocalDateTime.now(), pageable)
                        .stream()
                        .map(BookingMapper::toBookingInfoDto)
                        .collect(Collectors.toList());
            case CURRENT:
                return bookingRepository.findBookingBookerByCurrentState(userId, LocalDateTime.now(), pageable)
                        .stream()
                        .map(BookingMapper::toBookingInfoDto)
                        .collect(Collectors.toList());
            case FUTURE:
                return bookingRepository.findBookingsByBookerIdAndEndAfterOrderByStartDesc(
                        userId, LocalDateTime.now(), pageable)
                        .stream()
                        .map(BookingMapper::toBookingInfoDto)
                        .collect(Collectors.toList());
            default:
                throw new ValidationException("Unknown state: UNSUPPORTED_STATUS");
        }
    }

    @Override
    public List<BookingInfoDto> getBookingOwnerByState(Long userId, String state, int from, int size) {
        userRepository.findById(userId).orElseThrow(()
                -> new ObjectNotFoundException("User doesn't exists"));
        Pageable pageable = PageRequest.of(from / size, size);
        BookingStatus bookingStatus = BookingStatus.checkEnum(state);
        switch (bookingStatus) {
            case ALL:
                return bookingRepository.findBookingsByItemOwnerIdOrderByStartDesc(userId, pageable)
                        .stream()
                        .map(BookingMapper::toBookingInfoDto)
                        .collect(Collectors.toList());
            case WAITING:
            case REJECTED:
                return bookingRepository.findBookingsByItemOwnerIdAndStatusOrderByStartDesc(
                        userId, bookingStatus, pageable)
                        .stream()
                        .map(BookingMapper::toBookingInfoDto)
                        .collect(Collectors.toList());
            case PAST:
                return bookingRepository.findBookingsByItemOwnerIdAndEndBeforeOrderByStartDesc(
                        userId, LocalDateTime.now(), pageable)
                        .stream()
                        .map(BookingMapper::toBookingInfoDto)
                        .collect(Collectors.toList());
            case CURRENT:
                return bookingRepository.findBookingOwnerByCurrentState(userId, LocalDateTime.now(), pageable)
                        .stream()
                        .map(BookingMapper::toBookingInfoDto)
                        .collect(Collectors.toList());
            case FUTURE:
                return bookingRepository.findBookingsByItemOwnerIdAndEndAfterOrderByStartDesc(
                        userId, LocalDateTime.now(), pageable)
                        .stream()
                        .map(BookingMapper::toBookingInfoDto)
                        .collect(Collectors.toList());
            default:
                throw new ValidationException("Unknown state: UNSUPPORTED_STATUS");
        }
    }
}
