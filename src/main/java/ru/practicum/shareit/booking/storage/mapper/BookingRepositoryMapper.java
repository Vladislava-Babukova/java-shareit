package ru.practicum.shareit.booking.storage.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.storage.entity.BookingEntity;
import ru.practicum.shareit.item.storage.mapper.ItemRepositoryMapper;
import ru.practicum.shareit.user.storage.mapper.UserRepositoryMapper;

import java.util.List;

@Mapper(componentModel = "spring",
        implementationName = "bookingRepositoryMapperImpl",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = {UserRepositoryMapper.class, ItemRepositoryMapper.class})
public interface BookingRepositoryMapper {

    @Mapping(target = "booker", source = "booker")
    @Mapping(target = "item", source = "item")
    Booking toBooking(BookingEntity entity);

    @Mapping(target = "booker", source = "booker")
    @Mapping(target = "item", source = "item")
    BookingEntity toEntity(Booking booking);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "booker", ignore = true)
    @Mapping(target = "item", ignore = true)
    void updateEntity(Booking booking, @MappingTarget BookingEntity entity);

    List<Booking> toBookingList(List<BookingEntity> entityList);
}
