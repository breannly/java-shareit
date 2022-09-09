package ru.practicum.shareit.item.dto;

import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class CommentMapper {

    public static Comment mapToComment(CommentDto commentDto, User user, Item item) {
        return new Comment(
                commentDto.getId(),
                commentDto.getText(),
                user,
                item,
                LocalDateTime.now()
        );
    }

    public static CommentInfoDto mapToCommentInfoDto(Comment comment) {
        return new CommentInfoDto(
                comment.getId(),
                comment.getText(),
                comment.getAuthor().getName(),
                comment.getCreated());
    }

    public static List<CommentInfoDto> mapToCommentInfoDto(List<Comment> comments) {
        return comments
                .stream()
                .map(CommentMapper::mapToCommentInfoDto)
                .collect(Collectors.toList());
    }
}
