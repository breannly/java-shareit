package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.exception.ObjectNotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repo.CommentRepository;
import ru.practicum.shareit.item.repo.ItemRepository;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.ItemRequestRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;
    private final ItemRequestRepository itemRequestRepository;

    @Override
    public List<ItemInfoDto> getItems(Long userId, int from, int size) {
        Pageable pageable = PageRequest.of(from / size, size);
        return itemRepository.findAllById(userId, pageable)
                .stream()
                .map(item -> ItemMapper.toItemInfoDto(
                        item,
                        bookingRepository.findLastBooking(LocalDateTime.now(), userId, item.getId()),
                        bookingRepository.findNextBooking(LocalDateTime.now(), userId, item.getId()),
                        commentRepository.findCommentsByItemId(item.getId()))
                )
                .collect(Collectors.toList());
    }

    @Override
    public ItemDto saveItem(Long userId, ItemDto itemDto) {
        User user = userRepository.findById(userId).orElseThrow(()
                -> new ObjectNotFoundException("User not found"));
        ItemRequest request = null;
        if (itemDto.getRequestId() != null) {
            request = itemRequestRepository.findById(itemDto.getRequestId()).orElseThrow(()
                    -> new ObjectNotFoundException("Request not found"));
        }
        Item item = ItemMapper.toItem(itemDto, user, request);
        return ItemMapper.toDto(itemRepository.save(item));
    }

    @Override
    public CommentInfoDto saveComment(CommentDto commentDto, Long userId, Long itemId) {
        User user = userRepository.findById(userId).orElseThrow(()
                -> new ObjectNotFoundException("User doesn't exists"));
        Item item = itemRepository.findById(itemId).orElseThrow(()
                -> new ObjectNotFoundException("Item doesn't exists"));
        if (!bookingRepository.existsBookingByItemIdAndBookerIdAndEndBefore(itemId, userId, LocalDateTime.now())) {
            throw new ValidationException("Validation Failed");
        }
        Comment comment = CommentMapper.mapToComment(commentDto, user, item);
        return CommentMapper.mapToCommentInfoDto(commentRepository.save(comment));
    }

    @Override
    public ItemDto updateItem(Long userId, Long itemId, ItemDto itemDto) {
        userRepository.findById(userId).orElseThrow(()
                -> new ObjectNotFoundException("User not found"));
        Item updatedItem = itemRepository.findById(itemId).orElseThrow(()
                -> new ObjectNotFoundException("Item doesn't exists"));
        checkItemOwner(updatedItem.getOwner().getId(), userId);
        fillItem(updatedItem, itemDto);
        return ItemMapper.toDto(itemRepository.save(updatedItem));
    }

    private void checkItemOwner(Long ownerId, Long userId) {
        if (!ownerId.equals(userId)) {
            throw new ObjectNotFoundException("Item has another owner");
        }
    }

    private void fillItem(Item item, ItemDto itemDto) {
        String name = itemDto.getName();
        if (name != null) {
            item.setName(name);
        }
        String description = itemDto.getDescription();
        if (description != null) {
            item.setDescription(description);
        }
        Boolean available = itemDto.getAvailable();
        if (available != null) {
            item.setAvailable(available);
        }
    }

    @Override
    public ItemInfoDto getItem(Long itemId, Long userId) {
        userRepository.findById(userId).orElseThrow(()
                -> new ObjectNotFoundException("User not found"));
        Item item = itemRepository.findById(itemId).orElseThrow(()
                -> new ObjectNotFoundException("Item doesn't exists"));
        Booking lastBooking = bookingRepository.findLastBooking(LocalDateTime.now(), userId, itemId);
        Booking nextBooking = bookingRepository.findNextBooking(LocalDateTime.now(), userId, itemId);
        List<Comment> comments = commentRepository.findCommentsByItemId(itemId);
        return ItemMapper.toItemInfoDto(item, lastBooking, nextBooking, comments);
    }

    @Override
    public List<ItemDto> searchItems(String text, Long userId, int from, int size) {
        userRepository.findById(userId).orElseThrow(()
                -> new ObjectNotFoundException("User not found"));
        Pageable pageable = PageRequest.of(from / size, size);
        if (text.isBlank()) {
            return new ArrayList<>();
        } else {
            text = text.toLowerCase();
            return itemRepository.searchByKeyword(text, pageable).stream()
                    .map(ItemMapper::toDto)
                    .collect(Collectors.toList());
        }
    }
}
