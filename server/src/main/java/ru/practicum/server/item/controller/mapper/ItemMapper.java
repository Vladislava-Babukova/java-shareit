package ru.practicum.server.item.controller.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.server.booking.model.Booking;
import ru.practicum.server.item.controller.dto.*;
import ru.practicum.server.item.model.Comment;
import ru.practicum.server.item.model.Item;
import ru.practicum.server.request.model.ItemRequest;

import java.util.List;

@Mapper(componentModel = "spring", implementationName = "itemControllerMapperImpl")
public interface ItemMapper {

    @Mapping(target = "request", source = "requestId")
    Item toItemFromRequestDto(RequestItemDto dto);

    default ItemRequest mapRequestIdToEmptyRequest(Integer requestId) {
        if (requestId == null) {
            return null;
        }
        ItemRequest request = new ItemRequest();
        request.setId(requestId);
        return request;
    }

    @Mapping(target = "request", source = "request.id")
    ItemDto toDto(Item item);

    @Mapping(target = "request.id", source = "request")
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