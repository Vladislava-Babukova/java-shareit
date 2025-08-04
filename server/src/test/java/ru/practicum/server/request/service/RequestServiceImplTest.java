package ru.practicum.server.request.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.server.exceptions.DataNotFoundException;
import ru.practicum.server.item.storage.entity.ItemEntity;
import ru.practicum.server.item.storage.repository.ItemRepository;
import ru.practicum.server.request.controller.dto.GetRequestDto;
import ru.practicum.server.request.controller.dto.ItemByRequestDto;
import ru.practicum.server.request.controller.mapper.RequestMapper;
import ru.practicum.server.request.model.ItemRequest;
import ru.practicum.server.request.storage.entity.RequestEntity;
import ru.practicum.server.request.storage.mapper.RequestRepositoryMapper;
import ru.practicum.server.request.storage.repository.RequestRepository;
import ru.practicum.server.user.model.User;
import ru.practicum.server.user.service.UserService;
import ru.practicum.server.user.storage.entity.UserEntity;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RequestServiceImplTest {

    @Mock
    private UserService userService;

    @Mock
    private RequestRepository repository;

    @Mock
    private RequestMapper mapper;

    @Mock
    private RequestRepositoryMapper repositoryMapper;

    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private RequestServiceImpl requestService;

    private User user;
    private ItemRequest itemRequest;
    private RequestEntity requestEntity;
    private ItemEntity itemEntity;
    private UserEntity userEntity;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1);
        user.setName("Test User");
        user.setEmail("test@example.com");

        itemRequest = new ItemRequest();
        itemRequest.setId(1);
        itemRequest.setDescription("Test request");
        itemRequest.setRequestor(user);
        itemRequest.setCreated(LocalDateTime.now());

        requestEntity = new RequestEntity();
        requestEntity.setId(1);
        requestEntity.setDescription("Test request");
        requestEntity.setRequestor(userEntity);
        requestEntity.setCreated(LocalDateTime.now());

        itemEntity = new ItemEntity();
        itemEntity.setId(1);
        itemEntity.setName("Test Item");
        itemEntity.setDescription("Test Description");
        itemEntity.setAvailable(true);
        itemEntity.setOwner(userEntity);
        itemEntity.setRequest(requestEntity);
    }

    @Test
    void create_ShouldSaveRequestAndReturnIt() {
        when(userService.get(anyInt())).thenReturn(user);
        when(repositoryMapper.toEntity(any(ItemRequest.class))).thenReturn(requestEntity);
        when(repository.save(any(RequestEntity.class))).thenReturn(requestEntity);
        when(repositoryMapper.toRequest(any(RequestEntity.class))).thenReturn(itemRequest);

        ItemRequest createdRequest = requestService.create(user.getId(), itemRequest);

        assertNotNull(createdRequest);
        assertEquals(itemRequest.getDescription(), createdRequest.getDescription());
        verify(repository, times(1)).save(requestEntity);
    }

    @Test
    void getByUser_ShouldReturnListOfRequestsWithItems() {
        List<RequestEntity> userRequests = List.of(requestEntity);
        List<ItemEntity> itemsForRequests = List.of(itemEntity);
        GetRequestDto expectedDto = new GetRequestDto();
        expectedDto.setId(1);
        expectedDto.setDescription("Test request");
        expectedDto.setItems(List.of(new ItemByRequestDto()));
        when(repository.findAllByRequestorIdOrderByCreatedDesc(anyInt())).thenReturn(userRequests);
        when(itemRepository.findByRequestIdIn(anyList())).thenReturn(itemsForRequests);
        when(repositoryMapper.mapToGetRequestDto(any(RequestEntity.class), anyList())).thenReturn(expectedDto);

        List<GetRequestDto> result = requestService.getByUser(user.getId());


        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(expectedDto.getId(), result.get(0).getId());
        verify(itemRepository, times(1)).findByRequestIdIn(anyList());
    }

    @Test
    void getByUser_ShouldReturnEmptyListWhenNoRequests() {
        when(repository.findAllByRequestorIdOrderByCreatedDesc(anyInt()))
                .thenReturn(Collections.emptyList());

        List<GetRequestDto> result = requestService.getByUser(user.getId());


        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(itemRepository, times(1)).findByRequestIdIn(Collections.emptyList());
    }

    @Test
    void getAll_ShouldReturnAllRequestsWithItems() {
        List<RequestEntity> allRequests = List.of(requestEntity);
        List<ItemEntity> itemsForRequests = List.of(itemEntity);
        GetRequestDto expectedDto = new GetRequestDto();
        expectedDto.setId(1);
        expectedDto.setDescription("Test request");
        expectedDto.setItems(List.of(new ItemByRequestDto()));

        when(repository.findAllByOrderByCreatedDesc()).thenReturn(allRequests);
        when(itemRepository.findByRequestIdIn(anyList())).thenReturn(itemsForRequests);
        when(repositoryMapper.mapToGetRequestDto(any(RequestEntity.class), anyList()))
                .thenReturn(expectedDto);

        List<GetRequestDto> result = requestService.getAll();

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(expectedDto.getId(), result.get(0).getId());
        verify(itemRepository, times(1)).findByRequestIdIn(anyList());
    }

    @Test
    void get_ShouldReturnRequestWithItems() {
        GetRequestDto expectedDto = new GetRequestDto();
        expectedDto.setId(1);
        expectedDto.setDescription("Test request");
        expectedDto.setItems(List.of(new ItemByRequestDto()));

        when(repository.findById(anyInt())).thenReturn(Optional.of(requestEntity));
        when(itemRepository.findByRequestId(anyInt())).thenReturn(List.of(itemEntity));
        when(repositoryMapper.toItemByRequestDtoList(anyList())).thenReturn(List.of(new ItemByRequestDto()));
        when(repositoryMapper.toGetRequestDto(any(RequestEntity.class))).thenReturn(expectedDto);

        GetRequestDto result = requestService.get(requestEntity.getId());

        assertNotNull(result);
        assertEquals(expectedDto.getId(), result.getId());
        assertFalse(result.getItems().isEmpty());
        verify(itemRepository, times(1)).findByRequestId(anyInt());
    }

    @Test
    void get_ShouldThrowExceptionWhenRequestNotFound() {
        when(repository.findById(anyInt())).thenReturn(Optional.empty());

        assertThrows(DataNotFoundException.class, () -> requestService.get(1));
    }

    @Test
    void getRequest_ShouldReturnRequest() {
        when(repository.findById(anyInt())).thenReturn(Optional.of(requestEntity));
        when(repositoryMapper.toRequest(any(RequestEntity.class))).thenReturn(itemRequest);

        ItemRequest result = requestService.getRequest(requestEntity.getId());

        assertNotNull(result);
        assertEquals(itemRequest.getId(), result.getId());
    }

    @Test
    void getRequest_ShouldThrowExceptionWhenRequestNotFound() {
        when(repository.findById(anyInt())).thenReturn(Optional.empty());

        assertThrows(DataNotFoundException.class, () -> requestService.getRequest(1));
    }
}