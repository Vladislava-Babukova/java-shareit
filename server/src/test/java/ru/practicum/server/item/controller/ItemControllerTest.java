package ru.practicum.server.item.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.server.exceptions.DataNotFoundException;
import ru.practicum.server.item.controller.dto.*;
import ru.practicum.server.item.controller.mapper.ItemMapper;
import ru.practicum.server.item.model.Comment;
import ru.practicum.server.item.model.Item;
import ru.practicum.server.item.service.ItemService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemControllerTest {

    @Mock
    private ItemService itemService;

    @Mock
    private ItemMapper itemMapper;

    @InjectMocks
    private ItemController itemController;


    @Test
    void create_shouldReturnItemDto() {
        int userId = 1;
        RequestItemDto requestDto = new RequestItemDto();
        Item item = new Item();
        Item createdItem = new Item();
        ItemDto expectedDto = new ItemDto();

        when(itemMapper.toItemFromRequestDto(requestDto)).thenReturn(item);
        when(itemService.create(item, userId)).thenReturn(createdItem);
        when(itemMapper.toDto(createdItem)).thenReturn(expectedDto);

        ItemDto result = itemController.create(userId, requestDto);


        assertSame(expectedDto, result);
        verify(itemMapper).toItemFromRequestDto(requestDto);
        verify(itemService).create(item, userId);
        verify(itemMapper).toDto(createdItem);
    }

    @Test
    void update_shouldReturnUpdatedItemDto() {
        int itemId = 1;
        int userId = 1;
        RequestItemDto requestDto = new RequestItemDto();
        Item item = new Item();
        Item updatedItem = new Item();
        ItemDto expectedDto = new ItemDto();
        when(itemMapper.toItemFromRequestDto(requestDto)).thenReturn(item);
        when(itemService.update(item, itemId, userId)).thenReturn(updatedItem);
        when(itemMapper.toDto(updatedItem)).thenReturn(expectedDto);


        ItemDto result = itemController.update(itemId, userId, requestDto);


        assertSame(expectedDto, result);
        verify(itemMapper).toItemFromRequestDto(requestDto);
        verify(itemService).update(item, itemId, userId);
        verify(itemMapper).toDto(updatedItem);
    }

    @Test
    void get_shouldReturnItemResponseDto() {
        int itemId = 1;
        ItemResponseDto expectedDto = new ItemResponseDto();

        when(itemService.getDto(itemId)).thenReturn(expectedDto);

        ItemResponseDto result = itemController.get(itemId);

        assertSame(expectedDto, result);
        verify(itemService).getDto(itemId);
    }

    @Test
    void getAll_shouldReturnListOfItemResponseDto() {
        int userId = 1;
        List<ItemResponseDto> expectedList = List.of(new ItemResponseDto());

        when(itemService.getAll(userId)).thenReturn(expectedList);

        List<ItemResponseDto> result = itemController.getAll(userId);

        assertSame(expectedList, result);
        verify(itemService).getAll(userId);
    }

    @Test
    void searchItems_shouldReturnListOfItemDto() {
        String text = "test";
        List<Item> foundItems = List.of(new Item());
        List<ItemDto> expectedList = List.of(new ItemDto());

        when(itemService.searchItems(text)).thenReturn(foundItems);
        when(itemMapper.toDtoList(foundItems)).thenReturn(expectedList);

        List<ItemDto> result = itemController.searchItems(text);

        assertSame(expectedList, result);
        verify(itemService).searchItems(text);
        verify(itemMapper).toDtoList(foundItems);
    }

    @Test
    void createComment_shouldReturnCommentResponseDto() {
        int userId = 1;
        int itemId = 1;
        CommentCreateDto createDto = new CommentCreateDto();
        Comment comment = new Comment();
        CommentResponseDto expectedDto = new CommentResponseDto();
        when(itemMapper.toComment(createDto)).thenReturn(comment);
        when(itemService.createComment(comment, userId, itemId)).thenReturn(comment);
        when(itemMapper.toResponseComment(comment)).thenReturn(expectedDto);

        CommentResponseDto result = itemController.createComment(userId, itemId, createDto);

        assertSame(expectedDto, result);
        verify(itemMapper).toComment(createDto);
        verify(itemService).createComment(comment, userId, itemId);
        verify(itemMapper).toResponseComment(comment);
    }


    @Test
    void create_whenUserNotFound_shouldThrowDataNotFoundException() {
        int userId = 999;
        RequestItemDto requestDto = new RequestItemDto();
        Item item = new Item();
        when(itemMapper.toItemFromRequestDto(requestDto)).thenReturn(item);
        when(itemService.create(item, userId)).thenThrow(new DataNotFoundException("User not found"));


        DataNotFoundException exception = assertThrows(DataNotFoundException.class,
                () -> itemController.create(userId, requestDto));

        assertEquals("User not found", exception.getMessage());

    }

    @Test
    void update_whenItemNotFound_shouldThrowDataNotFoundException() {
        int itemId = 999;
        int userId = 1;
        RequestItemDto requestDto = new RequestItemDto();
        Item item = new Item();
        when(itemMapper.toItemFromRequestDto(requestDto)).thenReturn(item);
        when(itemService.update(item, itemId, userId)).thenThrow(new DataNotFoundException("Item not found"));


        DataNotFoundException exception = assertThrows(DataNotFoundException.class,
                () -> itemController.update(itemId, userId, requestDto));

        assertEquals("Item not found", exception.getMessage());
    }

    @Test
    void get_whenItemNotFound_shouldThrowDataNotFoundException() {
        int itemId = 999;
        when(itemService.getDto(itemId)).thenThrow(new DataNotFoundException("Item not found"));

        DataNotFoundException exception = assertThrows(DataNotFoundException.class,
                () -> itemController.get(itemId));

        assertEquals("Item not found", exception.getMessage());
    }

    @Test
    void createComment_whenUserNotFound_shouldThrowDataNotFoundException() {
        int userId = 999;
        int itemId = 1;
        CommentCreateDto createDto = new CommentCreateDto();

        when(itemService.createComment(any(), eq(userId), eq(itemId)))
                .thenThrow(new DataNotFoundException("User not found"));

        DataNotFoundException exception = assertThrows(DataNotFoundException.class,
                () -> itemController.createComment(userId, itemId, createDto));

        assertEquals("User not found", exception.getMessage());
    }


    @Test
    void getAll_whenNoItems_shouldReturnEmptyList() {
        int userId = 1;
        when(itemService.getAll(userId)).thenReturn(List.of());

        List<ItemResponseDto> result = itemController.getAll(userId);

        assertTrue(result.isEmpty());
        verify(itemService).getAll(userId);
    }
}