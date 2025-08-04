package ru.practicum.server.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.server.booking.controller.State;
import ru.practicum.server.booking.model.Booking;
import ru.practicum.server.booking.service.BookingService;
import ru.practicum.server.booking.storage.mapper.BookingRepositoryMapper;
import ru.practicum.server.booking.storage.repository.BookingRepository;
import ru.practicum.server.exceptions.DataNotFoundException;
import ru.practicum.server.exceptions.StorageException;
import ru.practicum.server.exceptions.ValidationException;
import ru.practicum.server.item.controller.dto.CommentResponseDto;
import ru.practicum.server.item.controller.dto.ForTimeBookingDto;
import ru.practicum.server.item.controller.dto.ItemResponseDto;
import ru.practicum.server.item.controller.mapper.ItemMapper;
import ru.practicum.server.item.model.Comment;
import ru.practicum.server.item.model.Item;
import ru.practicum.server.item.storage.entity.ItemEntity;
import ru.practicum.server.item.storage.mapper.ItemRepositoryMapper;
import ru.practicum.server.item.storage.repository.CommentRepository;
import ru.practicum.server.item.storage.repository.ItemRepository;
import ru.practicum.server.request.model.ItemRequest;
import ru.practicum.server.request.service.RequestService;
import ru.practicum.server.user.model.User;
import ru.practicum.server.user.service.UserService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository repository;
    private final ItemRepositoryMapper mapper;
    private final UserService userService;
    private final BookingRepository bookingRepository;
    private final ItemMapper controllerMapper;
    private final CommentRepository commentRepository;
    private final BookingRepositoryMapper bookingMapper;
    private final BookingService bookingService;
    private final RequestService requestService;

    @Override
    public Item create(Item item, int userId) {
        User user = userService.get(userId);
        if (item.getRequest() != null) {
            ItemRequest request = requestService.getRequest(item.getRequest().getId());
            item.setRequest(request);
        }
        if (item.getAvailable() == null) {
            throw new ValidationException("доступность должна быть обозначена");
        }
        item.setOwner(user);
        ItemEntity entity = mapper.toEntity(item);

        try {
            repository.save(entity);
            return mapper.toItem(entity);
        } catch (Exception e) {
            throw new StorageException("Произошла ошибка при создании item");
        }
    }


    @Override
    public Item update(Item item, int itemId, int userId) {
        ItemEntity entity = repository.findById(itemId).orElseThrow(() -> new DataNotFoundException("Не удалось найти item"));
        if (entity.getOwner().getId() != userId) {
            throw new StorageException("Изменять item может только его создатель");
        }
        mapper.updateEntity(item, entity);
        return mapper.toItem(repository.save(entity));
    }

    @Override
    public ItemResponseDto getDto(int itemId) {
        Item item = repository.findById(itemId).map(mapper::toItem)
                .orElseThrow(() -> new DataNotFoundException("Не удалось найти item"));

        ItemResponseDto responseDto = controllerMapper.toResponse(item,
                getPastBookings(itemId), getFutureBookings(itemId),
                getComments(itemId));
        return responseDto;
    }

    @Override
    public Item get(int itemId) {
        return repository.findById(itemId).map(mapper::toItem)
                .orElseThrow(() -> new DataNotFoundException("Не удалось найти item"));
    }

    @Override
    public Comment createComment(Comment comment, int userId, int itemId) {
        comment.setItem(get(itemId));
        comment.setAuthor(userService.get(userId));
        List<Booking> bookings = bookingMapper.toBookingList(bookingRepository.findByItemIdAndBookerIdAndEndBefore(
                itemId, userId, LocalDateTime.now()));
        if (bookings.isEmpty()) {
            throw new ValidationException("Вы не можете оставить отзыв на эту вещь");
        }
        comment.setCreated(LocalDateTime.now());
        return mapper.toComment(commentRepository.save(mapper.toCommentEntity(comment)));
    }

    public List<CommentResponseDto> getComments(int itemId) {
        List<Comment> comments = mapper.toCommentList(commentRepository.findByItemId(itemId));
        return controllerMapper.toResponseCommentList(comments);
    }


    public ForTimeBookingDto getPastBookings(int ownerId) {
        List<Booking> pastBookings = bookingService.getAllBookings(ownerId, State.PAST);
        if (pastBookings.isEmpty()) {
            return null;
        }
        return controllerMapper.toTimeDto(pastBookings.getFirst());
    }

    public ForTimeBookingDto getFutureBookings(int ownerId) {
        List<Booking> futureBookings = bookingService.getAllBookings(ownerId, State.FUTURE);
        if (futureBookings.isEmpty()) {
            return null;
        }
        return controllerMapper.toTimeDto(futureBookings.getLast());
    }

    @Override
    public List<ItemResponseDto> getAll(int userId) {
        LocalDateTime now = LocalDateTime.now();

        return repository.findByOwnerId(userId).stream()
                .map(mapper::toItem)
                .map(item -> controllerMapper.toResponse(item, getPastBookings(item.getId()),
                        getFutureBookings(item.getId()), getComments(item.getId())))
                .collect(Collectors.toList());
    }

    @Override
    public List<Item> searchItems(String text) {
        if ((text == null) || text.isBlank()) {
            return new ArrayList<Item>();
        }
        List<ItemEntity> entities = repository
                .findAvailableByNameOrDescription(text, text);
        return mapper.toItemList(entities);
    }
}
