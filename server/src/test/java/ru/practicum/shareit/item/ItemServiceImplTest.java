package ru.practicum.shareit.item;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.exception.ObjectNotFoundException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentInfoDto;
import ru.practicum.shareit.item.dto.ItemDto;
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
import java.util.Optional;

class ItemServiceImplTest {

    ItemService itemService;
    ItemRepository itemRepository;
    UserRepository userRepository;
    BookingRepository bookingRepository;
    CommentRepository commentRepository;
    ItemRequestRepository itemRequestRepository;

    @BeforeEach
    void beforeEach() {
        userRepository = Mockito.mock(UserRepository.class);
        itemRepository = Mockito.mock(ItemRepository.class);
        bookingRepository = Mockito.mock(BookingRepository.class);
        commentRepository = Mockito.mock(CommentRepository.class);
        itemRequestRepository = Mockito.mock(ItemRequestRepository.class);
        itemService = new ItemServiceImpl(
                itemRepository,
                userRepository,
                bookingRepository,
                commentRepository,
                itemRequestRepository
        );
    }

    @Test
    void shouldSaveItemWithRightParameters() {
        User user = new User(1L, "test", "test@gmail.com");
        Item item = new Item(1L, user, "test", "test", true, null);
        ItemDto itemDto = new ItemDto(null, "test", "test", true, null);

        Mockito.when(userRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(user));

        Mockito.when(itemRepository.save(Mockito.any(Item.class)))
                .thenReturn(item);

        ItemDto foundItem = itemService.saveItem(user.getId(), itemDto);

        Assertions.assertNotNull(foundItem);
        Assertions.assertEquals(item.getId(), foundItem.getId());
        Assertions.assertEquals(itemDto.getName(), foundItem.getName());
        Assertions.assertEquals(itemDto.getDescription(), foundItem.getDescription());
        Assertions.assertEquals(itemDto.getAvailable(), foundItem.getAvailable());
        Assertions.assertEquals(itemDto.getRequestId(), foundItem.getRequestId());
    }

    @Test
    void shouldThrowExceptionUsingSaveWhenWrongUserId() {
        ItemDto itemDto = new ItemDto(null, "test", "test", true, null);

        Mockito.when(userRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.empty());

        Exception exception = Assertions.assertThrows(ObjectNotFoundException.class,
                () -> itemService.saveItem(1L, itemDto));

        Assertions.assertEquals("User not found", exception.getMessage());
    }

    @Test
    void shouldSaveItemWithRequest() {
        User user1 = new User(1L, "test1", "test1@gmail.com");
        User user2 = new User(2L, "test2", "test2@gmail.com");
        ItemRequest itemRequest =
                new ItemRequest(1L, "test", LocalDateTime.now(), user2, new ArrayList<>());
        Item item = new Item(1L, user1, "test", "test", true, itemRequest);
        ItemDto itemDto = new ItemDto(null, "test", "test", true, 1L);

        Mockito.when(userRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(user1));

        Mockito.when(itemRequestRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(itemRequest));

        Mockito.when(itemRepository.save(Mockito.any(Item.class)))
                .thenReturn(item);

        ItemDto foundItem = itemService.saveItem(user1.getId(), itemDto);

        Assertions.assertNotNull(foundItem);
        Assertions.assertEquals(item.getId(), foundItem.getId());
        Assertions.assertEquals(itemDto.getName(), foundItem.getName());
        Assertions.assertEquals(itemDto.getDescription(), foundItem.getDescription());
        Assertions.assertEquals(itemDto.getAvailable(), foundItem.getAvailable());
        Assertions.assertEquals(itemDto.getRequestId(), foundItem.getRequestId());
    }

    @Test
    void shouldThrowExceptionUsingSaveWhenWrongRequestId() {
        User user = new User(1L, "test", "test@gmail.com");
        ItemDto itemDto = new ItemDto(null, "test", "test", true, 1L);

        Mockito.when(userRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(user));

        Mockito.when(itemRequestRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.empty());

        Exception exception = Assertions.assertThrows(ObjectNotFoundException.class,
                () -> itemService.saveItem(1L, itemDto));

        Assertions.assertEquals("Request not found", exception.getMessage());
    }

    @Test
    void shouldSaveCommentWithRightParameters() {
        CommentDto commentDto = new CommentDto(null, "test");
        CommentInfoDto commentInfoDto = new CommentInfoDto(1L, "test", "test", LocalDateTime.now());
        User user = new User(1L, "test", "test@gmail.com");
        Item item = new Item(1L, user, "test", "test", true, null);
        Comment comment = new Comment(1L, "test", user, item, LocalDateTime.now());

        Mockito.when(userRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(user));

        Mockito.when(itemRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(item));

        Mockito.when(commentRepository.save(Mockito.any(Comment.class)))
                .thenReturn(comment);

        Mockito.when(bookingRepository.existsBookingByItemIdAndBookerIdAndEndBefore(
                        Mockito.anyLong(),
                        Mockito.anyLong(),
                        Mockito.any(LocalDateTime.class)))
                .thenReturn(true);

        CommentInfoDto foundComment = itemService.saveComment(commentDto, 1L, 1L);

        Assertions.assertNotNull(foundComment);
        Assertions.assertEquals(commentInfoDto.getId(), foundComment.getId());
        Assertions.assertEquals(commentInfoDto.getText(), foundComment.getText());
        Assertions.assertEquals(commentInfoDto.getAuthorName(), foundComment.getAuthorName());
    }

    @Test
    void shouldUpdateItemWithRightParameters() {
        User user = new User(1L, "test", "test@gmail.com");
        Item item = new Item(1L, user, "test", "test", true, null);
        Item itemUpdate =
                new Item(1L, user, "testUpdate", "testUpdate", true, null);
        ItemDto itemDto = new ItemDto(null, "testUpdate", "testUpdate", true, null);

        Mockito.when(userRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(user));

        Mockito.when(itemRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(item));

        Mockito.when(itemRepository.save(Mockito.any(Item.class)))
                .thenReturn(itemUpdate);

        ItemDto foundItem = itemService.updateItem(user.getId(), item.getId(), itemDto);

        Assertions.assertNotNull(foundItem);
        Assertions.assertEquals(item.getId(), foundItem.getId());
        Assertions.assertEquals(itemDto.getName(), foundItem.getName());
        Assertions.assertEquals(itemDto.getDescription(), foundItem.getDescription());
        Assertions.assertEquals(itemDto.getAvailable(), foundItem.getAvailable());
        Assertions.assertEquals(itemDto.getRequestId(), foundItem.getRequestId());
    }

    @Test
    void shouldReturnEmptyListUsingSearch() {
        User user = new User(1L, "test", "test@gmail.com");
        String text = "";

        Mockito.when(userRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(user));

        List<ItemDto> results = itemService.searchItems(text, user.getId(), 0, 10);

        Assertions.assertEquals(0, results.size());
    }
}