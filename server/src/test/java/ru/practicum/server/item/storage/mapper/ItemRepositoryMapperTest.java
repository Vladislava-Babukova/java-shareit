package ru.practicum.server.item.storage.mapper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.server.item.model.Comment;
import ru.practicum.server.item.model.Item;
import ru.practicum.server.item.storage.entity.CommentEntity;
import ru.practicum.server.item.storage.entity.ItemEntity;
import ru.practicum.server.request.model.ItemRequest;
import ru.practicum.server.request.storage.entity.RequestEntity;
import ru.practicum.server.user.model.User;
import ru.practicum.server.user.storage.entity.UserEntity;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ItemRepositoryMapperTest {

    @InjectMocks
    private ItemRepositoryMapperImpl itemRepositoryMapper;

    @Test
    void toItem_ShouldMapCorrectly() {
        UserEntity ownerEntity = new UserEntity();
        ownerEntity.setId(1);
        ownerEntity.setName("Owner");

        RequestEntity requestEntity = new RequestEntity();
        requestEntity.setId(1);

        ItemEntity itemEntity = new ItemEntity();
        itemEntity.setId(1);
        itemEntity.setName("Test Item");
        itemEntity.setDescription("Test Description");
        itemEntity.setAvailable(true);
        itemEntity.setOwner(ownerEntity);
        itemEntity.setRequest(requestEntity);

        Item result = itemRepositoryMapper.toItem(itemEntity);

        assertNotNull(result);
        assertEquals(itemEntity.getId(), result.getId());
        assertEquals(itemEntity.getName(), result.getName());
        assertEquals(itemEntity.getDescription(), result.getDescription());
        assertEquals(itemEntity.getAvailable(), result.getAvailable());

        assertNotNull(result.getOwner());
        assertEquals(ownerEntity.getId(), result.getOwner().getId());
        assertEquals(ownerEntity.getName(), result.getOwner().getName());

        assertNotNull(result.getRequest());
        assertEquals(requestEntity.getId(), result.getRequest().getId());
    }

    @Test
    void toEntity_ShouldMapCorrectly() {
        User owner = new User();
        owner.setId(1);
        owner.setName("Owner");

        ItemRequest request = new ItemRequest();
        request.setId(1);

        Item item = new Item();
        item.setId(1);
        item.setName("Test Item");
        item.setDescription("Test Description");
        item.setAvailable(true);
        item.setOwner(owner);
        item.setRequest(request);

        ItemEntity result = itemRepositoryMapper.toEntity(item);

        assertNotNull(result);
        assertEquals(item.getId(), result.getId());
        assertEquals(item.getName(), result.getName());
        assertEquals(item.getDescription(), result.getDescription());
        assertEquals(item.getAvailable(), result.getAvailable());

        assertNotNull(result.getOwner());
        assertEquals(owner.getId(), result.getOwner().getId());
        assertEquals(owner.getName(), result.getOwner().getName());

        assertNotNull(result.getRequest());
        assertEquals(request.getId(), result.getRequest().getId());
    }

    @Test
    void updateEntity_ShouldUpdateOnlyAllowedFields() {
        Item item = new Item();
        item.setName("Updated Name");
        item.setDescription("Updated Description");
        item.setAvailable(false);

        ItemEntity entity = new ItemEntity();
        entity.setId(1);
        entity.setName("Original Name");
        entity.setDescription("Original Description");
        entity.setAvailable(true);

        UserEntity owner = new UserEntity();
        owner.setId(1);
        entity.setOwner(owner);

        RequestEntity request = new RequestEntity();
        request.setId(1);
        entity.setRequest(request);


        itemRepositoryMapper.updateEntity(item, entity);


        assertEquals(1L, entity.getId());
        assertEquals("Updated Name", entity.getName());
        assertEquals("Updated Description", entity.getDescription());
        assertFalse(entity.getAvailable());

        assertNotNull(entity.getOwner());
        assertEquals(1L, entity.getOwner().getId());
        assertNotNull(entity.getRequest());
        assertEquals(1L, entity.getRequest().getId());
    }

    @Test
    void toItemList_ShouldMapListCorrectly() {
        ItemEntity entity1 = new ItemEntity();
        entity1.setId(1);

        ItemEntity entity2 = new ItemEntity();
        entity2.setId(2);

        List<ItemEntity> entities = List.of(entity1, entity2);

        List<Item> result = itemRepositoryMapper.toItemList(entities);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(entity1.getId(), result.get(0).getId());
        assertEquals(entity2.getId(), result.get(1).getId());
    }

    @Test
    void toComment_ShouldMapCorrectly() {

        UserEntity authorEntity = new UserEntity();
        authorEntity.setId(1);
        authorEntity.setName("Author");

        ItemEntity itemEntity = new ItemEntity();
        itemEntity.setId(1);

        CommentEntity commentEntity = new CommentEntity();
        commentEntity.setId(1);
        commentEntity.setText("Test comment");
        commentEntity.setAuthor(authorEntity);
        commentEntity.setItem(itemEntity);
        commentEntity.setCreated(LocalDateTime.now());

        Comment result = itemRepositoryMapper.toComment(commentEntity);

        assertNotNull(result);
        assertEquals(commentEntity.getId(), result.getId());
        assertEquals(commentEntity.getText(), result.getText());
        assertEquals(commentEntity.getCreated(), result.getCreated());

        assertNotNull(result.getAuthor());
        assertEquals(authorEntity.getId(), result.getAuthor().getId());
        assertEquals(authorEntity.getName(), result.getAuthor().getName());

        assertNotNull(result.getItem());
        assertEquals(itemEntity.getId(), result.getItem().getId());
    }

    @Test
    void toCommentEntity_ShouldMapCorrectly() {

        User author = new User();
        author.setId(1);
        author.setName("Author");

        Item item = new Item();
        item.setId(1);

        Comment comment = new Comment();
        comment.setId(1);
        comment.setText("Test comment");
        comment.setAuthor(author);
        comment.setItem(item);
        comment.setCreated(LocalDateTime.now());


        CommentEntity result = itemRepositoryMapper.toCommentEntity(comment);


        assertNotNull(result);
        assertEquals(comment.getId(), result.getId());
        assertEquals(comment.getText(), result.getText());
        assertEquals(comment.getCreated(), result.getCreated());

        assertNotNull(result.getAuthor());
        assertEquals(author.getId(), result.getAuthor().getId());
        assertEquals(author.getName(), result.getAuthor().getName());

        assertNotNull(result.getItem());
        assertEquals(item.getId(), result.getItem().getId());
    }

    @Test
    void updateCommentEntity_ShouldUpdateOnlyAllowedFields() {

        Comment comment = new Comment();
        comment.setText("Updated text");
        comment.setCreated(LocalDateTime.now().plusDays(1));

        CommentEntity entity = new CommentEntity();
        entity.setId(1);
        entity.setText("Original text");
        entity.setCreated(LocalDateTime.now());

        UserEntity author = new UserEntity();
        author.setId(1);
        entity.setAuthor(author);

        ItemEntity item = new ItemEntity();
        item.setId(1);
        entity.setItem(item);


        itemRepositoryMapper.updateCommentEntity(comment, entity);


        assertEquals(1L, entity.getId());
        assertEquals("Updated text", entity.getText());
        assertEquals(comment.getCreated(), entity.getCreated());

        assertNotNull(entity.getAuthor());
        assertEquals(1L, entity.getAuthor().getId());
        assertNotNull(entity.getItem());
        assertEquals(1L, entity.getItem().getId());
    }

    @Test
    void toCommentList_ShouldMapListCorrectly() {

        CommentEntity entity1 = new CommentEntity();
        entity1.setId(1);

        CommentEntity entity2 = new CommentEntity();
        entity2.setId(2);

        List<CommentEntity> entities = List.of(entity1, entity2);


        List<Comment> result = itemRepositoryMapper.toCommentList(entities);


        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(entity1.getId(), result.get(0).getId());
        assertEquals(entity2.getId(), result.get(1).getId());
    }

    @Test
    void toItem_ShouldReturnNullWhenInputIsNull() {
        assertNull(itemRepositoryMapper.toItem(null));
    }

    @Test
    void toEntity_ShouldReturnNullWhenInputIsNull() {
        assertNull(itemRepositoryMapper.toEntity(null));
    }

    @Test
    void updateEntity_ShouldDoNothingWhenSourceIsNull() {
        ItemEntity entity = new ItemEntity();
        entity.setId(1);

        itemRepositoryMapper.updateEntity(null, entity);

        assertEquals(1L, entity.getId());
    }

    @Test
    void toItemList_ShouldReturnNullWhenInputIsNull() {
        assertNull(itemRepositoryMapper.toItemList(null));
    }

    @Test
    void toComment_ShouldReturnNullWhenInputIsNull() {
        assertNull(itemRepositoryMapper.toComment(null));
    }

    @Test
    void toCommentEntity_ShouldReturnNullWhenInputIsNull() {
        assertNull(itemRepositoryMapper.toCommentEntity(null));
    }

    @Test
    void updateCommentEntity_ShouldDoNothingWhenSourceIsNull() {
        CommentEntity entity = new CommentEntity();
        entity.setId(1);

        itemRepositoryMapper.updateCommentEntity(null, entity);

        assertEquals(1L, entity.getId());
    }

    @Test
    void toCommentList_ShouldReturnNullWhenInputIsNull() {
        assertNull(itemRepositoryMapper.toCommentList(null));
    }
}