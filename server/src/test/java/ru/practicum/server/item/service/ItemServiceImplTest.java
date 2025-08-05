package ru.practicum.server.item.service;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.server.booking.model.Booking;
import ru.practicum.server.booking.service.BookingService;
import ru.practicum.server.booking.storage.entity.BookingEntity;
import ru.practicum.server.booking.storage.mapper.BookingRepositoryMapper;
import ru.practicum.server.booking.storage.repository.BookingRepository;
import ru.practicum.server.exceptions.StorageException;
import ru.practicum.server.exceptions.ValidationException;
import ru.practicum.server.item.controller.dto.CommentResponseDto;
import ru.practicum.server.item.controller.dto.ItemResponseDto;
import ru.practicum.server.item.controller.mapper.ItemMapper;
import ru.practicum.server.item.model.Comment;
import ru.practicum.server.item.model.Item;
import ru.practicum.server.item.storage.entity.CommentEntity;
import ru.practicum.server.item.storage.entity.ItemEntity;
import ru.practicum.server.item.storage.mapper.ItemRepositoryMapper;
import ru.practicum.server.item.storage.repository.CommentRepository;
import ru.practicum.server.item.storage.repository.ItemRepository;
import ru.practicum.server.request.model.ItemRequest;
import ru.practicum.server.request.service.RequestService;
import ru.practicum.server.user.model.User;
import ru.practicum.server.user.service.UserService;
import ru.practicum.server.user.storage.entity.UserEntity;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemServiceImplTest {

    @Mock
    private UserService userService;

    @Mock
    private RequestService requestService;

    @Mock
    private ItemRepository repository;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private BookingService bookingService;

    @Mock
    private ItemMapper controllerMapper;

    @Mock
    private ItemRepositoryMapper repositoryMapper;

    @Mock
    private BookingRepositoryMapper bookingMapper;


    @InjectMocks
    private ItemServiceImpl itemService;

    private User user;
    private Item item;
    private ItemEntity itemEntity;
    private Comment comment;
    private Booking booking;
    private BookingEntity bookingEntity;
    private UserEntity userEntity;
    private CommentEntity commentEntity;


    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1);
        user.setName("Test User");

        item = new Item();
        item.setId(1);
        item.setName("Test Item");
        item.setDescription("Test Description");
        item.setAvailable(true);
        item.setOwner(user);

        itemEntity = new ItemEntity();
        itemEntity.setId(1);
        itemEntity.setName("Test Item");
        itemEntity.setDescription("Test Description");
        itemEntity.setAvailable(true);
        itemEntity.setOwner(userEntity);

        comment = new Comment();
        comment.setId(1);
        comment.setText("Test Comment");
        comment.setItem(item);
        comment.setAuthor(user);
        comment.setCreated(LocalDateTime.now());

        booking = new Booking();
        booking.setId(1);
        booking.setItem(item);
        booking.setBooker(user);
    }

    @Test
    void create_ShouldSaveItemAndReturnIt() {
        when(userService.get(anyInt())).thenReturn(user);
        when(repositoryMapper.toEntity(any(Item.class))).thenReturn(itemEntity);
        when(repositoryMapper.toItem(any(ItemEntity.class))).thenReturn(item);
        when(repository.save(any(ItemEntity.class))).thenReturn(itemEntity);

        Item createdItem = itemService.create(item, user.getId());

        assertNotNull(createdItem);
        assertEquals(item.getName(), createdItem.getName());
        verify(repository, times(1)).save(itemEntity);
    }

    @Test
    void create_ShouldThrowExceptionWhenAvailableIsNull() {
        item.setAvailable(null);

        assertThrows(ValidationException.class, () -> itemService.create(item, user.getId()));
    }

    @Test
    void create_ShouldSetRequestWhenRequestIdIsPresent() {
        ItemRequest request = new ItemRequest();
        request.setId(1);
        item.setRequest(request);

        when(userService.get(anyInt())).thenReturn(user);
        when(requestService.getRequest(anyInt())).thenReturn(request);
        when(repositoryMapper.toEntity(any(Item.class))).thenReturn(itemEntity);
        when(repositoryMapper.toItem(any(ItemEntity.class))).thenReturn(item);
        when(repository.save(any(ItemEntity.class))).thenReturn(itemEntity);

        Item createdItem = itemService.create(item, user.getId());

        assertNotNull(createdItem.getRequest());
        verify(requestService, times(1)).getRequest(request.getId());
    }

    @Test
    void update_ShouldUpdateItemAndReturnIt() {
        UserEntity ownerEntity = new UserEntity();
        ownerEntity.setId(user.getId());
        ownerEntity.setName(user.getName());
        itemEntity.setOwner(ownerEntity);

        when(repository.findById(anyInt())).thenReturn(Optional.of(itemEntity));
        when(repositoryMapper.toItem(any(ItemEntity.class))).thenReturn(item);
        when(repository.save(any(ItemEntity.class))).thenReturn(itemEntity);

        Item updatedItem = itemService.update(item, item.getId(), user.getId());

        assertNotNull(updatedItem);
        verify(repositoryMapper, times(1)).updateEntity(item, itemEntity);
    }

    @Test
    void update_ShouldThrowExceptionWhenUserIsNotOwner() {
        UserEntity ownerEntity = new UserEntity();
        ownerEntity.setId(1);
        ownerEntity.setName("Owner");
        itemEntity.setOwner(ownerEntity);

        when(repository.findById(anyInt())).thenReturn(Optional.of(itemEntity));

        assertThrows(StorageException.class,
                () -> itemService.update(item, item.getId(), 999));
    }

    @Test
    void getDto_ShouldReturnItemResponseDto() {
        when(repository.findById(anyInt())).thenReturn(Optional.of(itemEntity));
        when(repositoryMapper.toItem(any(ItemEntity.class))).thenReturn(item);
        when(controllerMapper.toResponse(any(Item.class), any(), any(), any()))
                .thenReturn(new ItemResponseDto());

        ItemResponseDto dto = itemService.getDto(item.getId());

        assertNotNull(dto);
        verify(repository, times(1)).findById(item.getId());
    }

    @Test
    void get_ShouldReturnItem() {
        when(repository.findById(anyInt())).thenReturn(Optional.of(itemEntity));
        when(repositoryMapper.toItem(any(ItemEntity.class))).thenReturn(item);

        Item foundItem = itemService.get(item.getId());

        assertNotNull(foundItem);
        assertEquals(item.getId(), foundItem.getId());
    }


    @Test
    void createComment_ShouldThrowExceptionWhenNoBookings() {
        when(userService.get(anyInt())).thenReturn(user);
        when(repository.findById(anyInt())).thenReturn(Optional.of(itemEntity));
        when(repositoryMapper.toItem(any(ItemEntity.class))).thenReturn(item);
        when(bookingRepository.findByItemIdAndBookerIdAndEndBefore(
                anyInt(),
                anyInt(),
                any(LocalDateTime.class)
        )).thenReturn(Collections.emptyList());

        when(bookingMapper.toBookingList(anyList())).thenReturn(Collections.emptyList());


        assertThrows(ValidationException.class,
                () -> itemService.createComment(comment, user.getId(), item.getId()));


        verify(bookingRepository).findByItemIdAndBookerIdAndEndBefore(
                eq(item.getId()),
                eq(user.getId()),
                any(LocalDateTime.class)
        );
    }

    @Test
    void getComments_ShouldReturnListOfCommentResponseDto() {
        when(commentRepository.findByItemId(anyInt())).thenReturn(Collections.singletonList(new CommentEntity()));
        when(repositoryMapper.toCommentList(anyList())).thenReturn(Collections.singletonList(comment));
        when(controllerMapper.toResponseCommentList(anyList()))
                .thenReturn(Collections.singletonList(new CommentResponseDto()));

        List<CommentResponseDto> comments = itemService.getComments(item.getId());

        assertFalse(comments.isEmpty());
        verify(commentRepository, times(1)).findByItemId(item.getId());
    }

    @Test
    void getAll_ShouldReturnListOfItemResponseDto() {
        when(repository.findByOwnerId(anyInt())).thenReturn(Collections.singletonList(itemEntity));
        when(repositoryMapper.toItem(any(ItemEntity.class))).thenReturn(item);
        when(controllerMapper.toResponse(any(Item.class), any(), any(), any()))
                .thenReturn(new ItemResponseDto());

        List<ItemResponseDto> items = itemService.getAll(user.getId());

        assertFalse(items.isEmpty());
        verify(repository, times(1)).findByOwnerId(user.getId());
    }

    @Test
    void searchItems_ShouldReturnEmptyListWhenTextIsBlank() {
        List<Item> items = itemService.searchItems("");

        assertTrue(items.isEmpty());
    }

    @Test
    void searchItems_ShouldReturnListOfItemsWhenTextIsNotBlank() {
        when(repository.findAvailableByNameOrDescription(anyString(), anyString()))
                .thenReturn(Collections.singletonList(itemEntity));
        when(repositoryMapper.toItemList(anyList())).thenReturn(Collections.singletonList(item));

        List<Item> items = itemService.searchItems("test");

        assertFalse(items.isEmpty());
        verify(repository, times(1))
                .findAvailableByNameOrDescription("test", "test");
    }
}