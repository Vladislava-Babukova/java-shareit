package ru.practicum.server.request.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.server.exceptions.DataNotFoundException;
import ru.practicum.server.exceptions.ValidationException;
import ru.practicum.server.request.controller.dto.ExternalDto;
import ru.practicum.server.request.controller.dto.GetRequestDto;
import ru.practicum.server.request.controller.dto.ItemRequestDto;
import ru.practicum.server.request.controller.mapper.RequestMapper;
import ru.practicum.server.request.model.ItemRequest;
import ru.practicum.server.request.service.RequestService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemRequestControllerTest {

    @Mock
    private RequestService service;

    @Mock
    private RequestMapper mapper;

    @InjectMocks
    private ItemRequestController controller;

    @Test
    void create_shouldReturnItemRequestDto() {
        int userId = 1;
        ExternalDto externalDto = new ExternalDto();
        ItemRequest request = new ItemRequest();
        ItemRequestDto expectedDto = new ItemRequestDto();

        when(mapper.toRequest(externalDto)).thenReturn(request);
        when(service.create(userId, request)).thenReturn(request);
        when(mapper.toDto(request)).thenReturn(expectedDto);

        ItemRequestDto result = controller.create(userId, externalDto);

        assertSame(expectedDto, result);
        verify(mapper).toRequest(externalDto);
        verify(service).create(userId, request);
        verify(mapper).toDto(request);
    }

    @Test
    void getByUser_shouldReturnListOfGetRequestDto() {
        int userId = 1;
        List<GetRequestDto> expectedList = List.of(new GetRequestDto());
        when(service.getByUser(userId)).thenReturn(expectedList);

        List<GetRequestDto> result = controller.getByUser(userId);

        assertSame(expectedList, result);
        verify(service).getByUser(userId);
    }

    @Test
    void getAll_shouldReturnListOfGetRequestDto() {
        List<GetRequestDto> expectedList = List.of(new GetRequestDto());
        when(service.getAll()).thenReturn(expectedList);

        List<GetRequestDto> result = controller.getAll();

        assertSame(expectedList, result);
        verify(service).getAll();
    }

    @Test
    void getById_shouldReturnGetRequestDto() {
        int requestId = 1;
        GetRequestDto expectedDto = new GetRequestDto();
        when(service.get(requestId)).thenReturn(expectedDto);

        GetRequestDto result = controller.getById(requestId);

        assertSame(expectedDto, result);
        verify(service).get(requestId);
    }


    @Test
    void create_whenInvalidData_shouldThrowValidationException() {
        int userId = 1;
        ExternalDto invalidDto = new ExternalDto();

        when(mapper.toRequest(invalidDto)).thenThrow(new ValidationException("Invalid data"));

        ValidationException exception = assertThrows(ValidationException.class,
                () -> controller.create(userId, invalidDto));

        assertEquals("Invalid data", exception.getMessage());
        verify(mapper).toRequest(invalidDto);
        verifyNoInteractions(service);
    }

    @Test
    void create_whenUserNotFound_shouldThrowDataNotFoundException() {
        int userId = 999;
        ExternalDto externalDto = new ExternalDto();
        ItemRequest request = new ItemRequest();

        when(mapper.toRequest(externalDto)).thenReturn(request);
        when(service.create(userId, request)).thenThrow(new DataNotFoundException("User not found"));

        DataNotFoundException exception = assertThrows(DataNotFoundException.class,
                () -> controller.create(userId, externalDto));

        assertEquals("User not found", exception.getMessage());
        verify(service).create(userId, request);
    }

    @Test
    void getByUser_whenUserNotFound_shouldThrowDataNotFoundException() {
        int userId = 999;
        when(service.getByUser(userId)).thenThrow(new DataNotFoundException("User not found"));

        DataNotFoundException exception = assertThrows(DataNotFoundException.class,
                () -> controller.getByUser(userId));

        assertEquals("User not found", exception.getMessage());
        verify(service).getByUser(userId);
    }

    @Test
    void getById_whenRequestNotFound_shouldThrowDataNotFoundException() {
        int requestId = 999;
        when(service.get(requestId)).thenThrow(new DataNotFoundException("Request not found"));

        DataNotFoundException exception = assertThrows(DataNotFoundException.class,
                () -> controller.getById(requestId));

        assertEquals("Request not found", exception.getMessage());
        verify(service).get(requestId);
    }


    @Test
    void getByUser_whenNoRequests_shouldReturnEmptyList() {
        int userId = 1;
        when(service.getByUser(userId)).thenReturn(List.of());

        List<GetRequestDto> result = controller.getByUser(userId);

        assertTrue(result.isEmpty());
        verify(service).getByUser(userId);
    }

    @Test
    void getAll_whenNoRequests_shouldReturnEmptyList() {
        when(service.getAll()).thenReturn(List.of());

        List<GetRequestDto> result = controller.getAll();

        assertTrue(result.isEmpty());
        verify(service).getAll();
    }
}