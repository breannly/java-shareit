package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.exception.model.ObjectNotFoundException;
import ru.practicum.shareit.exception.model.ValidationException;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repo.CommentRepository;
import ru.practicum.shareit.item.repo.ItemRepository;
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

    @Override
    public List<ItemInfoDto> getAllById(Long userId) {
        return itemRepository.findAllById(userId).stream()
                .map(item -> ItemMapper.toItemInfoDto(
                        item,
                        bookingRepository.findLastBooking(LocalDateTime.now(), userId, item.getId()),
                        bookingRepository.findNextBooking(LocalDateTime.now(), userId, item.getId()),
                        commentRepository.findCommentsByItemId(item.getId()))
                )
                .collect(Collectors.toList());
    }

    @Override
    public ItemDto save(Long userId, ItemDto itemDto) {
        Item item = ItemMapper.toItem(itemDto);
        item.setOwner(userRepository.findById(userId).orElseThrow(()
                -> new ObjectNotFoundException("User doesn't exists")));
        return ItemMapper.toDto(itemRepository.save(item));
    }

    @Override
    public CommentInfoDto saveComment(CommentDto commentDto, Long userId, Long itemId) {
        User user = userRepository.findById(userId).orElseThrow(()
                -> new ObjectNotFoundException("User doesn't exists"));
        Item item = itemRepository.findById(itemId).orElseThrow(()
                -> new ObjectNotFoundException("Item doesn't exists"));
        if (!bookingRepository.existsBookingByItemIdAndBookerIdAndEndBefore(itemId, userId, LocalDateTime.now())) {
            throw new ValidationException("The user has not used this item yet");
        }
        Comment comment = CommentMapper.mapToComment(commentDto, user, item);
        return CommentMapper.mapToCommentInfoDto(commentRepository.save(comment));
    }

    @Override
    public ItemDto update(Long userId, Long itemId, ItemDto itemDto) {
        checkUserExists(userId);
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
    public ItemInfoDto getById(Long itemId, Long userId) {
        checkUserExists(userId);
        Item item = itemRepository.findById(itemId).orElseThrow(()
                -> new ObjectNotFoundException("Item doesn't exists"));
        Booking lastBooking = bookingRepository.findLastBooking(LocalDateTime.now(), userId, itemId);
        Booking nextBooking = bookingRepository.findNextBooking(LocalDateTime.now(), userId, itemId);
        List<Comment> comments = commentRepository.findCommentsByItemId(itemId);
        return ItemMapper.toItemInfoDto(item, lastBooking, nextBooking, comments);
    }

    @Override
    public List<ItemDto> search(String text, Long userId) {
        checkUserExists(userId);
        if (text.isBlank()) {
            return new ArrayList<>();
        } else {
            text = text.toLowerCase();
            return itemRepository.searchByKeyword(text).stream()
                    .map(ItemMapper::toDto)
                    .collect(Collectors.toList());
        }
    }

    private void checkUserExists(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new ObjectNotFoundException("User doesn't exists");
        }
    }
}
