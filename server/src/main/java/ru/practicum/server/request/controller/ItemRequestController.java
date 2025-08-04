package ru.practicum.server.request.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.server.request.controller.dto.ExternalDto;
import ru.practicum.server.request.controller.dto.GetRequestDto;
import ru.practicum.server.request.controller.dto.ItemRequestDto;
import ru.practicum.server.request.controller.mapper.RequestMapper;
import ru.practicum.server.request.model.ItemRequest;
import ru.practicum.server.request.service.RequestService;

import java.util.List;

/**
 * TODO Sprint add-item-requests.
 */
@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Slf4j
public class ItemRequestController {
    private final RequestMapper mapper;
    private final RequestService service;

    @PostMapping
    public ItemRequestDto create(
            @RequestHeader("X-Sharer-User-Id") int userId,
            @RequestBody ExternalDto externalDto) {

        ItemRequest request = mapper.toRequest(externalDto);
        return mapper.toDto(service.create(userId, request));
    }


    @GetMapping
    public List<GetRequestDto> getByUser(
            @RequestHeader("X-Sharer-User-Id") int userId) {
        return service.getByUser(userId);
    }

    @GetMapping("/all")
    public List<GetRequestDto> getAll() {
        return service.getAll();
    }


    @GetMapping("/{requestId}")
    public GetRequestDto getById(
            @PathVariable int requestId
    ) {
        return service.get(requestId);
    }
}
