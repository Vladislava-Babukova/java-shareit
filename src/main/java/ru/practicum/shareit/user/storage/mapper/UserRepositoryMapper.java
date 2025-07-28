package ru.practicum.shareit.user.storage.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.entity.UserEntity;

@Mapper(componentModel = "spring",
        implementationName = "userRepositoryMapperImpl",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserRepositoryMapper {

    User toUser(UserEntity entity);

    UserEntity toEntity(User user);

    @Mapping(target = "id", ignore = true)
    void updateEntity(User user, @MappingTarget UserEntity entity);
}
