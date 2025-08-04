package ru.practicum.server.booking.controller.mapper;

import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.server.booking.controller.dto.BookingCreateDto;
import ru.practicum.server.booking.controller.dto.BookingResponseDto;
import ru.practicum.server.booking.model.Booking;
import ru.practicum.server.item.controller.mapper.ItemMapper;
import ru.practicum.server.item.model.Item;
import ru.practicum.server.item.service.ItemService;
import ru.practicum.server.user.controller.mapper.UserMapper;

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