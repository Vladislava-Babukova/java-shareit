package ru.practicum.server.item.controller.mapper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.server.booking.model.Booking;
import ru.practicum.server.item.controller.dto.*;
import ru.practicum.server.item.model.Comment;
import ru.practicum.server.item.model.Item;
import ru.practicum.server.request.model.ItemRequest;
import ru.practicum.server.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ItemMapperTest {

    @InjectMocks
    private ItemControllerMapperImpl itemMapper;

    @Test
    void toItemFromRequestDto_ShouldMapCorrectly() {
        int requestId = 1;
        RequestItemDto dto = new RequestItemDto();
        dto.setName("Test Item");
        dto.setDescription("Test Description");
        dto.setAvailable(true);
        dto.setRequestId(requestId);

        Item result = itemMapper.toItemFromRequestDto(dto);

        assertNotNull(result);
        assertEquals(dto.getName(), result.getName());
        assertEquals(dto.getDescription(), result.getDescription());
        assertEquals(dto.getAvailable(), result.getAvailable());
        assertNotNull(result.getRequest());
        assertEquals(requestId, result.getRequest().getId());
    }

    @Test
    void toItemFromRequestDto_ShouldHandleNullRequestId() {
        RequestItemDto dto = new RequestItemDto();
        dto.setRequestId(null);

        Item result = itemMapper.toItemFromRequestDto(dto);

        assertNull(result.getRequest());
    }

    @Test
    void toDto_ShouldMapCorrectly() {
        int requestId = 1;
        Item item = new Item();
        item.setId(1);
        item.setName("Test Item");
        item.setDescription("Test Description");
        item.setAvailable(true);

        ItemRequest request = new ItemRequest();
        request.setId(requestId);
        item.setRequest(request);

        ItemDto result = itemMapper.toDto(item);

        assertNotNull(result);
        assertEquals(item.getId(), result.getId());
        assertEquals(item.getName(), result.getName());
        assertEquals(item.getDescription(), result.getDescription());
        assertEquals(item.getAvailable(), result.getAvailable());
        assertEquals(requestId, result.getRequest());
    }

    @Test
    void toItem_ShouldMapCorrectly() {
        int requestId = 1;
        ItemDto dto = new ItemDto();
        dto.setId(1);
        dto.setName("Test Item");
        dto.setDescription("Test Description");
        dto.setAvailable(true);
        dto.setRequest(requestId);

        Item result = itemMapper.toItem(dto);

        assertNotNull(result);
        assertEquals(dto.getId(), result.getId());
        assertEquals(dto.getName(), result.getName());
        assertEquals(dto.getDescription(), result.getDescription());
        assertEquals(dto.getAvailable(), result.getAvailable());
        assertNotNull(result.getRequest());
        assertEquals(requestId, result.getRequest().getId());
    }

    @Test
    void toDtoList_ShouldMapListCorrectly() {
        Item item1 = new Item();
        item1.setId(1);

        Item item2 = new Item();
        item2.setId(2);

        List<Item> items = List.of(item1, item2);

        List<ItemDto> result = itemMapper.toDtoList(items);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(item1.getId(), result.get(0).getId());
        assertEquals(item2.getId(), result.get(1).getId());
    }

    @Test
    void toBookingItemDto_ShouldMapCorrectly() {
        Item item = new Item();
        item.setId(1);
        item.setName("Test Item");

        BookingItemDto result = itemMapper.toBookingItemDto(item);

        assertNotNull(result);
        assertEquals(item.getId(), result.getId());
        assertEquals(item.getName(), result.getName());
    }

    @Test
    void toResponse_ShouldMapCorrectlyWithBookings() {
        Item item = new Item();
        item.setId(1);
        item.setName("Test Item");

        LocalDateTime now = LocalDateTime.now();
        ForTimeBookingDto lastBooking = new ForTimeBookingDto(1, 101, now.minusDays(2), now.minusDays(1));
        ForTimeBookingDto nextBooking = new ForTimeBookingDto(2, 102, now.plusDays(1), now.plusDays(2));

        CommentResponseDto comment = new CommentResponseDto(1, "Test text", "Author", now);
        List<CommentResponseDto> comments = List.of(comment);

        ItemResponseDto result = itemMapper.toResponse(item, lastBooking, nextBooking, comments);

        assertNotNull(result);
        assertEquals(item.getId(), result.getId());
        assertEquals(item.getName(), result.getName());

        assertNotNull(result.getLastBooking());
        assertEquals(lastBooking.getId(), result.getLastBooking().getId());
        assertEquals(lastBooking.getBookerId(), result.getLastBooking().getBookerId());
        assertEquals(lastBooking.getStart(), result.getLastBooking().getStart());
        assertEquals(lastBooking.getEnd(), result.getLastBooking().getEnd());

        assertNotNull(result.getNextBooking());
        assertEquals(nextBooking.getId(), result.getNextBooking().getId());
        assertEquals(nextBooking.getBookerId(), result.getNextBooking().getBookerId());
        assertEquals(nextBooking.getStart(), result.getNextBooking().getStart());
        assertEquals(nextBooking.getEnd(), result.getNextBooking().getEnd());

        assertEquals(1, result.getComments().size());
        assertEquals(comment, result.getComments().get(0));
    }

    @Test
    void toComment_ShouldMapCorrectly() {

        CommentCreateDto dto = new CommentCreateDto();
        dto.setText("Test comment");


        Comment result = itemMapper.toComment(dto);


        assertNotNull(result);
        assertEquals(dto.getText(), result.getText());
        assertNull(result.getAuthor());
    }

    @Test
    void toResponseComment_ShouldMapCorrectly() {

        User author = new User();
        author.setName("Test User");

        Comment comment = new Comment();
        comment.setId(1);
        comment.setText("Test text");
        comment.setCreated(LocalDateTime.now());
        comment.setAuthor(author);


        CommentResponseDto result = itemMapper.toResponseComment(comment);

        assertNotNull(result);
        assertEquals(comment.getId(), result.getId());
        assertEquals(comment.getText(), result.getText());
        assertEquals(author.getName(), result.getAuthorName());
        assertEquals(comment.getCreated(), result.getCreated());
    }

    @Test
    void toResponseCommentList_ShouldMapListCorrectly() {
        Comment comment1 = new Comment();
        comment1.setId(1);
        Comment comment2 = new Comment();
        comment2.setId(2);
        List<Comment> comments = List.of(comment1, comment2);

        List<CommentResponseDto> result = itemMapper.toResponseCommentList(comments);

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void toTimeDto_ShouldMapCorrectly() {
        User booker = new User();
        booker.setId(1);

        Booking booking = new Booking();
        booking.setId(1);
        booking.setBooker(booker);

        ForTimeBookingDto result = itemMapper.toTimeDto(booking);

        assertNotNull(result);
        assertEquals(booking.getId(), result.getId());
        assertEquals(booker.getId(), result.getBookerId());
    }

    @Test
    void toItemFromRequestDto_ShouldReturnNullWhenInputIsNull() {
        assertNull(itemMapper.toItemFromRequestDto(null));
    }

    @Test
    void toDto_ShouldReturnNullWhenInputIsNull() {
        assertNull(itemMapper.toDto(null));
    }

    @Test
    void toItem_ShouldReturnNullWhenInputIsNull() {
        assertNull(itemMapper.toItem(null));
    }

    @Test
    void toDtoList_ShouldReturnNullWhenInputIsNull() {
        assertNull(itemMapper.toDtoList(null));
    }

    @Test
    void toBookingItemDto_ShouldReturnNullWhenInputIsNull() {
        assertNull(itemMapper.toBookingItemDto(null));
    }

    @Test
    void toComment_ShouldReturnNullWhenInputIsNull() {
        assertNull(itemMapper.toComment(null));
    }

    @Test
    void toResponseComment_ShouldReturnNullWhenInputIsNull() {
        assertNull(itemMapper.toResponseComment(null));
    }

    @Test
    void toResponseCommentList_ShouldReturnNullWhenInputIsNull() {
        assertNull(itemMapper.toResponseCommentList(null));
    }

    @Test
    void toTimeDto_ShouldReturnNullWhenInputIsNull() {
        assertNull(itemMapper.toTimeDto(null));
    }
}