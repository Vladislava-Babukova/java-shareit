package ru.practicum.shareit.item.controller.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.controller.dto.*;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

@Mapper(componentModel = "spring", implementationName = "itemControllerMapperImpl")
public interface ItemMapper {

    Item toItem(RequestItemDto requestItemDto);

    ItemDto toDto(Item item);

    Item toItem(ItemDto itemDto);

    List<ItemDto> toDtoList(List<Item> items);

    BookingItemDto toBookingItemDto(Item item);

    @Mapping(target = "id", source = "item.id")
    ItemResponseDto toResponse(Item item, ForTimeBookingDto lastBooking, ForTimeBookingDto nextBooking,
                               List<CommentResponseDto> comments);

    Comment toComment(CommentCreateDto createComment);

    @Mapping(target = "authorName", source = "author.name")
    CommentResponseDto toResponseComment(Comment comment);

    List<CommentResponseDto> toResponseCommentList(List<Comment> comments);

    @Mapping(target = "bookerId", source = "booker.id")
    ForTimeBookingDto toTimeDto(Booking booking);

}