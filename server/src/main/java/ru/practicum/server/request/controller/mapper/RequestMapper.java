package ru.practicum.server.request.controller.mapper;

import org.mapstruct.Mapper;
import ru.practicum.server.request.controller.dto.ExternalDto;
import ru.practicum.server.request.controller.dto.ItemRequestDto;
import ru.practicum.server.request.model.ItemRequest;

@Mapper(componentModel = "spring", implementationName = "requestControllerMapperImpl")
public interface RequestMapper {
    ItemRequest toRequest(ExternalDto externalDto);

    ItemRequestDto toDto(ItemRequest itemRequest);

}
