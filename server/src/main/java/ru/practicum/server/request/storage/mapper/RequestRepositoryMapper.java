package ru.practicum.server.request.storage.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.server.item.storage.entity.ItemEntity;
import ru.practicum.server.request.controller.dto.GetRequestDto;
import ru.practicum.server.request.controller.dto.ItemByRequestDto;
import ru.practicum.server.request.model.ItemRequest;
import ru.practicum.server.request.storage.entity.RequestEntity;

import java.util.List;

@Mapper(componentModel = "spring", implementationName = "requestStorageMapperImpl")
public interface RequestRepositoryMapper {
    ItemRequest toRequest(RequestEntity requestEntity);

    RequestEntity toEntity(ItemRequest itemRequest);

    GetRequestDto toGetRequestDto(RequestEntity requestEntity);

    @Mapping(target = "owner", source = "owner.id")
    ItemByRequestDto toItemByRequestDto(ItemEntity itemEntity);

    GetRequestDto mapToGetRequestDto(RequestEntity request, List<ItemEntity> items);

    List<ItemByRequestDto> toItemByRequestDtoList(List<ItemEntity> itemEntity);
}
