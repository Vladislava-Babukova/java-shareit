package ru.practicum.shareit.item.storage.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.entity.CommentEntity;
import ru.practicum.shareit.item.storage.entity.ItemEntity;

import java.util.List;

@Mapper(componentModel = "spring",
        implementationName = "ItemRepositoryMapperImpl",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ItemRepositoryMapper {

    Item toItem(ItemEntity itemEntity);


    ItemEntity toEntity(Item item);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "owner", ignore = true)
    void updateEntity(Item item, @MappingTarget ItemEntity entity);

    List<Item> toItemList(List<ItemEntity> entities);

    Comment toComment(CommentEntity entity);

    CommentEntity toCommentEntity(Comment comment);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "item", ignore = true)
    @Mapping(target = "author", ignore = true)
    void updateCommentEntity(Comment comment, @MappingTarget CommentEntity entity);

    List<Comment> toCommentList(List<CommentEntity> entity);
}
