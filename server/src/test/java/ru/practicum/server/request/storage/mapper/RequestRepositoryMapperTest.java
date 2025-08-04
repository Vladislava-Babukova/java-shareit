package ru.practicum.server.request.storage.mapper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.server.item.storage.entity.ItemEntity;
import ru.practicum.server.request.controller.dto.GetRequestDto;
import ru.practicum.server.request.controller.dto.ItemByRequestDto;
import ru.practicum.server.request.model.ItemRequest;
import ru.practicum.server.request.storage.entity.RequestEntity;
import ru.practicum.server.user.model.User;
import ru.practicum.server.user.storage.entity.UserEntity;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
class RequestRepositoryMapperTest {

    @InjectMocks
    private RequestStorageMapperImpl requestRepositoryMapper;

    @Test
    void toRequest_ShouldMapCorrectlyFromEntity() {

        UserEntity requestorEntity = new UserEntity();
        requestorEntity.setId(1);
        requestorEntity.setName("Requestor");
        requestorEntity.setEmail("requestor@test.com");

        RequestEntity requestEntity = new RequestEntity();
        requestEntity.setId(1);
        requestEntity.setDescription("Test description");
        requestEntity.setRequestor(requestorEntity);
        requestEntity.setCreated(LocalDateTime.now());


        ItemRequest result = requestRepositoryMapper.toRequest(requestEntity);


        assertNotNull(result);
        assertEquals(requestEntity.getId(), result.getId());
        assertEquals(requestEntity.getDescription(), result.getDescription());
        assertEquals(requestEntity.getCreated(), result.getCreated());

        assertNotNull(result.getRequestor());
        assertEquals(requestorEntity.getId(), result.getRequestor().getId());
        assertEquals(requestorEntity.getName(), result.getRequestor().getName());
        assertEquals(requestorEntity.getEmail(), result.getRequestor().getEmail());
    }

    @Test
    void toEntity_ShouldMapCorrectlyFromItemRequest() {

        User requestor = new User();
        requestor.setId(1);
        requestor.setName("Requestor");
        requestor.setEmail("requestor@test.com");

        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setId(1);
        itemRequest.setDescription("Test description");
        itemRequest.setRequestor(requestor);
        itemRequest.setCreated(LocalDateTime.now());


        RequestEntity result = requestRepositoryMapper.toEntity(itemRequest);


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
    void toGetRequestDto_ShouldMapCorrectly() {

        UserEntity requestorEntity = new UserEntity();
        requestorEntity.setId(1);

        RequestEntity requestEntity = new RequestEntity();
        requestEntity.setId(1);
        requestEntity.setDescription("Test description");
        requestEntity.setRequestor(requestorEntity);
        requestEntity.setCreated(LocalDateTime.now());


        GetRequestDto result = requestRepositoryMapper.toGetRequestDto(requestEntity);


        assertNotNull(result);
        assertEquals(requestEntity.getId(), result.getId());
        assertEquals(requestEntity.getDescription(), result.getDescription());
        assertEquals(requestEntity.getCreated(), result.getCreated());


        assertDoesNotThrow(() -> result.getItems());

        if (result.getItems() != null) {
            assertTrue(result.getItems().isEmpty());
        }
    }

    @Test
    void toItemByRequestDto_ShouldMapCorrectlyWithOwnerId() {

        UserEntity ownerEntity = new UserEntity();
        ownerEntity.setId(1);

        ItemEntity itemEntity = new ItemEntity();
        itemEntity.setId(1);
        itemEntity.setName("Test item");
        itemEntity.setDescription("Test description");
        itemEntity.setAvailable(true);
        itemEntity.setOwner(ownerEntity);
        itemEntity.setRequest(new RequestEntity());


        ItemByRequestDto result = requestRepositoryMapper.toItemByRequestDto(itemEntity);


        assertNotNull(result);
        assertEquals(itemEntity.getId(), result.getId());
        assertEquals(itemEntity.getName(), result.getName());
        assertEquals(ownerEntity.getId(), result.getOwner());
    }

    @Test
    void mapToGetRequestDto_ShouldCombineRequestAndItems() {
        UserEntity requestorEntity = new UserEntity();
        requestorEntity.setId(1);

        RequestEntity requestEntity = new RequestEntity();
        requestEntity.setId(1);
        requestEntity.setRequestor(requestorEntity);

        UserEntity ownerEntity = new UserEntity();
        ownerEntity.setId(2);

        ItemEntity itemEntity = new ItemEntity();
        itemEntity.setId(1);
        itemEntity.setOwner(ownerEntity);
        itemEntity.setRequest(requestEntity);

        List<ItemEntity> items = List.of(itemEntity);


        GetRequestDto result = requestRepositoryMapper.mapToGetRequestDto(requestEntity, items);


        assertNotNull(result);
        assertEquals(requestEntity.getId(), result.getId());
        assertEquals(1, result.getItems().size());

        ItemByRequestDto itemDto = result.getItems().get(0);
        assertEquals(itemEntity.getId(), itemDto.getId());
        assertEquals(ownerEntity.getId(), itemDto.getOwner());

    }

    @Test
    void toItemByRequestDtoList_ShouldMapListWithOwnerIds() {

        UserEntity ownerEntity = new UserEntity();
        ownerEntity.setId(1);

        RequestEntity requestEntity = new RequestEntity();
        requestEntity.setId(1);

        ItemEntity item1 = new ItemEntity();
        item1.setId(1);
        item1.setOwner(ownerEntity);
        item1.setRequest(requestEntity);

        ItemEntity item2 = new ItemEntity();
        item2.setId(2);
        item2.setOwner(ownerEntity);
        item2.setRequest(requestEntity);

        List<ItemEntity> items = List.of(item1, item2);

        List<ItemByRequestDto> result = requestRepositoryMapper.toItemByRequestDtoList(items);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(item1.getId(), result.get(0).getId());
        assertEquals(item2.getId(), result.get(1).getId());
        assertEquals(ownerEntity.getId(), result.get(0).getOwner());
    }

    @Test
    void toRequest_ShouldHandleNullEntity() {
        assertNull(requestRepositoryMapper.toRequest(null));
    }

    @Test
    void toEntity_ShouldHandleNullRequest() {
        assertNull(requestRepositoryMapper.toEntity(null));
    }

    @Test
    void toGetRequestDto_ShouldHandleNullEntity() {
        assertNull(requestRepositoryMapper.toGetRequestDto(null));
    }

    @Test
    void toItemByRequestDto_ShouldHandleNullItem() {
        assertNull(requestRepositoryMapper.toItemByRequestDto(null));
    }

    @Test
    void mapToGetRequestDto_ShouldHandleNullItemsList() {

        RequestEntity requestEntity = new RequestEntity();
        requestEntity.setId(1);


        GetRequestDto result = requestRepositoryMapper.mapToGetRequestDto(requestEntity, null);


        assertNotNull(result);


        assertEquals(requestEntity.getId(), result.getId());


        assertDoesNotThrow(() -> result.getItems());


        if (result.getItems() != null) {
            assertTrue(result.getItems().isEmpty());
        }
    }

    @Test
    void toItemByRequestDtoList_ShouldHandleNullList() {
        assertNull(requestRepositoryMapper.toItemByRequestDtoList(null));
    }

    @Test
    void toItemByRequestDto_ShouldHandleItemWithoutOwner() {
        ItemEntity itemEntity = new ItemEntity();
        itemEntity.setId(1);
        itemEntity.setOwner(null);

        ItemByRequestDto result = requestRepositoryMapper.toItemByRequestDto(itemEntity);

        assertNotNull(result);
        assertEquals(0, result.getOwner());
    }
}