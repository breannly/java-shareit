package ru.practicum.shareit.item.repo;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@DataJpaTest
class CommentRepositoryTest {

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    CommentRepository commentRepository;

    User user1;
    User user2;
    Item item1;
    Item item2;
    Comment comment1;

    @BeforeEach
    void beforeEach() {
        user1 = userRepository.save(new User(null, "test1", "test1@mail.ru"));
        user2 = userRepository.save(new User(null, "test2", "test2@mail.ru"));
        item1 = itemRepository
                .save(new Item(null, user2, "test", "test", true,null));
        item2 = itemRepository
                .save(new Item(null, user1, "test", "test", true,null));
        comment1 = commentRepository.save(new Comment(null, "text", user1, item1, LocalDateTime.now()));
    }

    @Test
    void shouldReturnCommentListByItemId() {
        List<Comment> results = commentRepository.findCommentsByItemId(item1.getId());

        Assertions.assertNotNull(results);
        Assertions.assertEquals(1, results.size());
    }

    @Test
    void shouldReturnEmptyListByItemId() {
        List<Comment> results = commentRepository.findCommentsByItemId(item2.getId());

        Assertions.assertNotNull(results);
        Assertions.assertEquals(0, results.size());
    }

    @AfterEach
    void afterEach() {
        commentRepository.deleteAll();
        itemRepository.deleteAll();
        userRepository.deleteAll();
    }
}