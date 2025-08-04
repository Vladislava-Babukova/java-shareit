package ru.practicum.server.user.storage.mapper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.server.user.model.User;
import ru.practicum.server.user.storage.entity.UserEntity;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserRepositoryMapperTest {

    @InjectMocks
    private UserRepositoryMapperImpl userRepositoryMapper;

    @Test
    void toUser_ShouldMapAllFieldsCorrectly() {

        UserEntity userEntity = new UserEntity();
        userEntity.setId(1);
        userEntity.setName("Test User");
        userEntity.setEmail("user@test.com");


        User result = userRepositoryMapper.toUser(userEntity);


        assertNotNull(result);
        assertEquals(userEntity.getId(), result.getId());
        assertEquals(userEntity.getName(), result.getName());
        assertEquals(userEntity.getEmail(), result.getEmail());
    }

    @Test
    void toUser_ShouldReturnNullWhenInputIsNull() {
        assertNull(userRepositoryMapper.toUser(null));
    }

    @Test
    void toEntity_ShouldMapAllFieldsCorrectly() {
        User user = new User();
        user.setId(1);
        user.setName("Test User");
        user.setEmail("user@test.com");

        UserEntity result = userRepositoryMapper.toEntity(user);

        assertNotNull(result);
        assertEquals(user.getId(), result.getId());
        assertEquals(user.getName(), result.getName());
        assertEquals(user.getEmail(), result.getEmail());
    }

    @Test
    void toEntity_ShouldReturnNullWhenInputIsNull() {
        assertNull(userRepositoryMapper.toEntity(null));
    }

    @Test
    void updateEntity_ShouldUpdateOnlyAllowedFields() {

        User user = new User();
        user.setId(2);
        user.setName("Updated Name");
        user.setEmail("updated@test.com");

        UserEntity entity = new UserEntity();
        entity.setId(1);
        entity.setName("Original Name");
        entity.setEmail("original@test.com");

        userRepositoryMapper.updateEntity(user, entity);

        assertEquals(1, entity.getId());
        assertEquals("Updated Name", entity.getName());
        assertEquals("updated@test.com", entity.getEmail());
    }

    @Test
    void updateEntity_ShouldHandlePartialUpdate() {

        User user = new User();
        user.setEmail("updated@test.com");

        UserEntity entity = new UserEntity();
        entity.setId(1);
        entity.setName("Original Name");
        entity.setEmail("original@test.com");

        userRepositoryMapper.updateEntity(user, entity);

        assertEquals(1, entity.getId());
        assertEquals("Original Name", entity.getName());
        assertEquals("updated@test.com", entity.getEmail());
    }

    @Test
    void updateEntity_ShouldDoNothingWhenSourceIsNull() {
        UserEntity entity = new UserEntity();
        entity.setId(1);
        entity.setName("Original Name");
        entity.setEmail("original@test.com");

        userRepositoryMapper.updateEntity(null, entity);

        assertEquals(1, entity.getId());
        assertEquals("Original Name", entity.getName());
        assertEquals("original@test.com", entity.getEmail());
    }

    @Test
    void updateEntity_ShouldHandleNullFieldsInSource() {
        User user = new User();

        UserEntity entity = new UserEntity();
        entity.setId(1);
        entity.setName("Original Name");
        entity.setEmail("original@test.com");

        userRepositoryMapper.updateEntity(user, entity);

        assertEquals(1, entity.getId());
        assertEquals("Original Name", entity.getName());
        assertEquals("original@test.com", entity.getEmail());
    }
}