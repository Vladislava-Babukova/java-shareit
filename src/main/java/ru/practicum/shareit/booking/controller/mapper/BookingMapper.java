package ru.practicum.shareit.booking.controller.mapper;

import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.booking.controller.dto.BookingCreateDto;
import ru.practicum.shareit.booking.controller.dto.BookingResponseDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.controller.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.controller.mapper.UserMapper;

import java.util.List;

@Mapper(componentModel = "spring",
        implementationName = "bookingControllerMapperImpl",
        uses = {ItemMapper.class, UserMapper.class})
public interface BookingMapper {
    @Mapping(target = "item", source = "itemId")
    Booking toBooking(BookingCreateDto createDto, @Context ItemService itemService);

    @Mapping(target = "booker", source = "booker")
    @Mapping(target = "item", source = "item")
    BookingResponseDto toResponseDto(Booking booking);

    List<BookingResponseDto> toResponseList(List<Booking> bookingList);

    default Item mapItemId(int itemId, @Context ItemService itemService) {
        return itemService.get(itemId);
    }
}