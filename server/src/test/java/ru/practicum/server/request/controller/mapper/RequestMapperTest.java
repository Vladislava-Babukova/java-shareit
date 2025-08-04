package ru.practicum.server.request.controller.mapper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.server.request.controller.dto.ExternalDto;
import ru.practicum.server.request.controller.dto.ItemRequestDto;
import ru.practicum.server.request.model.ItemRequest;
import ru.practicum.server.user.model.User;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class RequestMapperTest {

    @InjectMocks
    private RequestControllerMapperImpl requestMapper;

    @Test
    void toRequest_ShouldMapCorrectlyFromExternalDto() {

        ExternalDto externalDto = new ExternalDto();
        externalDto.setDescription("Test description");


        ItemRequest result = requestMapper.toRequest(externalDto);


        assertNotNull(result);
        assertEquals(externalDto.getDescription(), result.getDescription());

        assertEquals(0, result.getId());
        assertNull(result.getRequestor());
    }

    @Test
    void toRequest_ShouldHandleNullExternalDto() {
        assertNull(requestMapper.toRequest(null));
    }

    @Test
    void toDto_ShouldMapCorrectlyToItemRequestDto() {

        User requestor = new User();
        requestor.setId(1);
        requestor.setName("Test User");
        requestor.setEmail("user@test.com");

        LocalDateTime created = LocalDateTime.now();

        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setId(1);
        itemRequest.setDescription("Test description");
        itemRequest.setRequestor(requestor);
        itemRequest.setCreated(created);


        ItemRequestDto result = requestMapper.toDto(itemRequest);


        assertNotNull(result);
        assertEquals(itemRequest.getId(), result.getId());
        assertEquals(itemRequest.getDescription(), result.getDescription());
        assertEquals(itemRequest.getCreated(), result.getCreated());

        assertNotNull(result.getRequestor());
        assertEquals(requestor.getId(), result.getRequestor().getId());
        assertEquals(requestor.getName(), result.getRequestor().getName());
        assertEquals(requestor.getEmail(), result.getRequestor().getEmail());
    }

    @Test
    void toDto_ShouldHandleNullRequestor() {

        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setId(1);
        itemRequest.setRequestor(null);


        ItemRequestDto result = requestMapper.toDto(itemRequest);


        assertNotNull(result);
        assertNull(result.getRequestor());
    }

    @Test
    void toDto_ShouldHandleNullItemRequest() {
        assertNull(requestMapper.toDto(null));
    }

    @Test
    void toDto_ShouldMapWithMinimalFields() {

        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setId(1);

        ItemRequestDto result = requestMapper.toDto(itemRequest);


        assertNotNull(result);
        assertEquals(1, result.getId());
        assertNull(result.getDescription());
        assertNull(result.getCreated());
        assertNull(result.getRequestor());
    }

    @Test
    void toRequest_ShouldMapWithNullFields() {
        ExternalDto externalDto = new ExternalDto();

        ItemRequest result = requestMapper.toRequest(externalDto);


        assertNotNull(result);
        assertNull(result.getDescription());
        assertNull(result.getCreated());
        assertEquals(0, result.getId());
        assertNull(result.getRequestor());
    }

    @Test
    void toDto_ShouldMapWithAllNullFields() {
        ItemRequest itemRequest = new ItemRequest();

        ItemRequestDto result = requestMapper.toDto(itemRequest);

        assertNotNull(result);
        assertEquals(0, result.getId());
        assertNull(result.getDescription());
        assertNull(result.getCreated());
        assertNull(result.getRequestor());
    }
}