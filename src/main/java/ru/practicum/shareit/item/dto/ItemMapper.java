package ru.practicum.shareit.item.dto;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

@Component
public class ItemMapper {

    public static ItemDto toDto(Item item) {
        ItemDto itemDto = new ItemDto();
        itemDto.setId(item.getId());
        itemDto.setName(item.getName());
        itemDto.setAvailable(item.getAvailable());
        itemDto.setDescription(item.getDescription());

        return itemDto;
    }

    public static ItemInfoDto toItemInfoDto(Item item,
                                            Booking lastBooking,
                                            Booking nextBooking,
                                            List<Comment> comments) {
        return new ItemInfoDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                ItemInfoDto.BookingInfoDto.create(lastBooking),
                ItemInfoDto.BookingInfoDto.create(nextBooking),
                CommentMapper.mapToCommentInfoDto(comments)
        );
    }

    public static Item toItem(ItemDto itemDto) {
        Item item = new Item();
        item.setName(itemDto.getName());
        item.setAvailable(itemDto.getAvailable());
        item.setDescription(itemDto.getDescription());

        return item;
    }
}
