package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingInfoDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repo.BookingRepository;
import ru.practicum.shareit.exception.model.ObjectNotFoundException;
import ru.practicum.shareit.exception.model.ValidationException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repo.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repo.UserRepository;

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
            throw new ValidationException("Booking time is incorrect");
        }
        if (item.getAvailable().equals(false)) {
            throw new ValidationException("Item is not available");
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
                -> new ObjectNotFoundException("Booking info not found"));
        User user = userRepository.findById(userId).orElseThrow(()
                -> new ObjectNotFoundException("User doesn't exists"));
        Item item = itemRepository.findById(booking.getItem().getId()).orElseThrow(()
                -> new ObjectNotFoundException("Item doesn't exists"));
        if (!item.getOwner().equals(user)) {
            throw new ObjectNotFoundException("User's booking information was not found");
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
    public List<BookingInfoDto> getBookingBookerByState(Long userId, String state) {
        userRepository.findById(userId).orElseThrow(()
                -> new ObjectNotFoundException("User doesn't exists"));
        BookingStatus bookingStatus = BookingStatus.checkEnum(state);
        switch (bookingStatus) {
            case ALL:
                return bookingRepository.findBookingsByBookerIdOrderByStartDesc(userId)
                        .stream()
                        .map(BookingMapper::toBookingInfoDto)
                        .collect(Collectors.toList());
            case WAITING:
            case REJECTED:
                return bookingRepository.findBookingsByBookerIdAndStatusOrderByStartDesc(userId, bookingStatus)
                        .stream()
                        .map(BookingMapper::toBookingInfoDto)
                        .collect(Collectors.toList());
            case PAST:
                return bookingRepository.findBookingsByBookerIdAndEndBeforeOrderByStartDesc(userId, LocalDateTime.now())
                        .stream()
                        .map(BookingMapper::toBookingInfoDto)
                        .collect(Collectors.toList());
            case CURRENT:
                return bookingRepository.findBookingBookerByCurrentState(userId, LocalDateTime.now())
                        .stream()
                        .map(BookingMapper::toBookingInfoDto)
                        .collect(Collectors.toList());
            case FUTURE:
                return bookingRepository.findBookingsByBookerIdAndEndAfterOrderByStartDesc(userId, LocalDateTime.now())
                        .stream()
                        .map(BookingMapper::toBookingInfoDto)
                        .collect(Collectors.toList());
            default:
                throw new ValidationException("Unknown state: UNSUPPORTED_STATUS");
        }
    }

    @Override
    public List<BookingInfoDto> getBookingOwnerByState(Long userId, String state) {
        userRepository.findById(userId).orElseThrow(()
                -> new ObjectNotFoundException("User doesn't exists"));
        BookingStatus bookingStatus = BookingStatus.checkEnum(state);
        switch (bookingStatus) {
            case ALL:
                return bookingRepository.findBookingsByItemOwnerIdOrderByStartDesc(userId)
                        .stream()
                        .map(BookingMapper::toBookingInfoDto)
                        .collect(Collectors.toList());
            case WAITING:
            case REJECTED:
                return bookingRepository.findBookingsByItemOwnerIdAndStatusOrderByStartDesc(userId, bookingStatus)
                        .stream()
                        .map(BookingMapper::toBookingInfoDto)
                        .collect(Collectors.toList());
            case PAST:
                return bookingRepository.findBookingsByItemOwnerIdAndEndBeforeOrderByStartDesc(userId,
                                LocalDateTime.now())
                        .stream()
                        .map(BookingMapper::toBookingInfoDto)
                        .collect(Collectors.toList());
            case CURRENT:
                return bookingRepository.findBookingOwnerByCurrentState(userId, LocalDateTime.now())
                        .stream()
                        .map(BookingMapper::toBookingInfoDto)
                        .collect(Collectors.toList());
            case FUTURE:
                return bookingRepository.findBookingsByItemOwnerIdAndEndAfterOrderByStartDesc(userId,
                                LocalDateTime.now())
                        .stream()
                        .map(BookingMapper::toBookingInfoDto)
                        .collect(Collectors.toList());
            default:
                throw new ValidationException("Unknown state: UNSUPPORTED_STATUS");
        }
    }
}
