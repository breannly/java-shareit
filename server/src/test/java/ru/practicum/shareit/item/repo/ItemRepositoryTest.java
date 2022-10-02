package ru.practicum.shareit.item.repo;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.util.List;

@DataJpaTest
class ItemRepositoryTest {

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    UserRepository userRepository;

    User user1;
    User user2;
    Item item1;
    Item item2;
    Item item3;

    @BeforeEach
    void beforeEach() {
        user1 = userRepository.save(new User(null, "test1", "test1@mail.ru"));
        user2 = userRepository.save(new User(null, "test2", "test2@mail.ru"));

        item1 = itemRepository
                .save(new Item(null, user1, "Lorem", "dolor sit amet", true,null));
        item2 = itemRepository
                .save(new Item(null, user2, "dolor", "Aenean massa", true,null));
        item3 = itemRepository
                .save(new Item(null, user2, "dolor sit amet", "Lorem", true,null));
    }

    @Test
    void shouldReturnItemListWhereConsistKeyword() {
        List<Item> results = itemRepository.searchByKeyword("sit", Pageable.unpaged());

        Assertions.assertNotNull(results);
        Assertions.assertEquals(2, results.size());
    }

    @Test
    void shouldReturnEmptyItemListWhenNotExistItemsWithKeyword() {
        List<Item> results = itemRepository.searchByKeyword("test", Pageable.unpaged());

        Assertions.assertNotNull(results);
        Assertions.assertEquals(0, results.size());
    }

    @Test
    void shouldReturnItemListByUserId() {
        List<Item> results = itemRepository.findAllById(user2.getId(), Pageable.unpaged());

        Assertions.assertNotNull(results);
        Assertions.assertEquals(2, results.size());
    }


    @AfterEach
    void afterEach() {
        itemRepository.deleteAll();
        userRepository.deleteAll();
    }

}