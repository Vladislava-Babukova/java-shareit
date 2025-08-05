package ru.practicum.shareit.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ExternalDto;

@Controller
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Slf4j
@Validated
public class RequestController {
    private final RequestClient requestClient;

    @PostMapping
    public ResponseEntity<Object> create(
            @RequestHeader("X-Sharer-User-Id") @Min(1) int userId,
            @RequestBody @Valid ExternalDto externalDto) {
        log.info("Create new request by userId={}, data: {}", userId, externalDto);
        return requestClient.createRequest(userId, externalDto);
    }

    @GetMapping
    public ResponseEntity<Object> getByUser(
            @RequestHeader("X-Sharer-User-Id") int userId) {
        log.info("Get requests for userId={}", userId);
        return requestClient.getRequestsByUser(userId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAll() {
        log.info("Get all requests");
        return requestClient.getAllRequests();
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getById(
            @PathVariable @Min(1) int requestId) {
        log.info("Get request with id={}", requestId);
        return requestClient.getRequestById(requestId);
    }
}
