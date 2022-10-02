package ru.practicum.shareit.request;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repo.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@DataJpaTest
class ItemRequestRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    ItemRequestRepository itemRequestRepository;

    User user1;
    User user2;
    ItemRequest request1;
    ItemRequest request2;
    ItemRequest request3;
    Item item;

    @BeforeEach
    void beforeEach() {
        user1 = userRepository.save(new User(null, "test1", "test1@mail.ru"));
        user2 = userRepository.save(new User(null, "test2", "test2@mail.ru"));

        request1 = itemRequestRepository
                .save(new ItemRequest(null, "test1", LocalDateTime.now(), user1, null));
        request2 = itemRequestRepository
                .save(new ItemRequest(null, "test2", LocalDateTime.now(), user1, null));
        request3 = itemRequestRepository
                .save(new ItemRequest(null, "test3", LocalDateTime.now(), user2, null));

        item = itemRepository
                .save(new Item(null, user2, "test", "test", true, request1));
    }

    @AfterEach
    void afterEach() {
        itemRepository.deleteAll();
        itemRequestRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void shouldFindRequestsByRequestorId() {
        List<ItemRequest> requests = itemRequestRepository.findItemRequestsByRequestorId(user1.getId());

        Assertions.assertFalse(requests.isEmpty());
        Assertions.assertEquals(2, requests.size());
    }

    @Test
    void shouldFindAvailableRequestsForUser() {
        List<ItemRequest> requests = itemRequestRepository.findRequests(user1.getId(), Pageable.unpaged());

        Assertions.assertFalse(requests.isEmpty());
        Assertions.assertEquals(1, requests.size());
        Assertions.assertEquals(request3.getId(), requests.get(0).getId());
    }
}