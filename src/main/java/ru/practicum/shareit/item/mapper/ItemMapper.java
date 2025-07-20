package ru.practicum.shareit.item.mapper;

import org.mapstruct.Mapper;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.RequestItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ItemMapper {

    Item toItem(RequestItemDto requestItemDto);

    ItemDto toDto(Item item);

    Item toItem(ItemDto itemDto);

    List<ItemDto> toDtoList(List<Item> items);
}