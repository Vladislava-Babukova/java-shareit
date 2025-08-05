package ru.practicum.server.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.server.exceptions.DataNotFoundException;
import ru.practicum.server.item.storage.entity.ItemEntity;
import ru.practicum.server.item.storage.repository.ItemRepository;
import ru.practicum.server.request.controller.dto.GetRequestDto;
import ru.practicum.server.request.controller.dto.ItemByRequestDto;
import ru.practicum.server.request.model.ItemRequest;
import ru.practicum.server.request.storage.entity.RequestEntity;
import ru.practicum.server.request.storage.mapper.RequestRepositoryMapper;
import ru.practicum.server.request.storage.repository.RequestRepository;
import ru.practicum.server.user.service.UserService;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class RequestServiceImpl implements RequestService {
    private final UserService userService;
    private final RequestRepository repository;
    private final RequestRepositoryMapper mapper;
    private final ItemRepository itemRepository;

    @Override
    public ItemRequest create(int userId, ItemRequest request) {
        request.setCreated(LocalDateTime.now());
        request.setRequestor(userService.get(userId));
        return mapper.toRequest(repository.save(mapper.toEntity(request)));
    }

    @Override
    public List<GetRequestDto> getByUser(int userId) {
        List<RequestEntity> userRequests = repository.findAllByRequestorIdOrderByCreatedDesc(userId);

        List<Integer> requestIds = userRequests.stream()
                .map(RequestEntity::getId)
                .collect(Collectors.toList());

        List<ItemEntity> itemsForRequests = itemRepository.findByRequestIdIn(requestIds);

        Map<Integer, List<ItemEntity>> itemsByRequestId = itemsForRequests.stream()
                .collect(Collectors.groupingBy(item -> item.getRequest().getId()));

        return userRequests.stream()
                .map(request -> mapper.mapToGetRequestDto(
                        request,
                        itemsByRequestId.getOrDefault(request.getId(), Collections.emptyList())
                ))
                .collect(Collectors.toList());
    }

    @Override
    public List<GetRequestDto> getAll() {
        List<RequestEntity> userRequests = repository.findAllByOrderByCreatedDesc();
        List<Integer> requestIds = userRequests.stream()
                .map(RequestEntity::getId)
                .collect(Collectors.toList());

        List<ItemEntity> itemsForRequests = itemRepository.findByRequestIdIn(requestIds);

        Map<Integer, List<ItemEntity>> itemsByRequestId = itemsForRequests.stream()
                .collect(Collectors.groupingBy(item -> item.getRequest().getId()));

        return userRequests.stream()
                .map(request -> mapper.mapToGetRequestDto(
                        request,
                        itemsByRequestId.getOrDefault(request.getId(), Collections.emptyList())
                ))
                .collect(Collectors.toList());
    }

    @Override
    public GetRequestDto get(int requestId) {
        RequestEntity request = repository.findById(requestId).orElseThrow(() -> new DataNotFoundException("Запись не найдена"));
        List<ItemEntity> entityItems = itemRepository.findByRequestId(request.getId());
        List<ItemByRequestDto> items = mapper.toItemByRequestDtoList(entityItems);
        GetRequestDto newRequest = mapper.toGetRequestDto(request);
        newRequest.setItems(items);
        return newRequest;
    }

    public ItemRequest getRequest(int requestId) {
        RequestEntity request = repository.findById(requestId)
                .orElseThrow(() -> new DataNotFoundException("Запись не найдена"));
        return mapper.toRequest(request);
    }

}
