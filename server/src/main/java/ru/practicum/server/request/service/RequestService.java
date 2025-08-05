package ru.practicum.server.request.service;

import ru.practicum.server.request.controller.dto.GetRequestDto;
import ru.practicum.server.request.model.ItemRequest;

import java.util.List;

public interface RequestService {
    ItemRequest create(int userId, ItemRequest request);

    List<GetRequestDto> getByUser(int userId);

    List<GetRequestDto> getAll();

    GetRequestDto get(int requestId);

    public ItemRequest getRequest(int requestId);
}
