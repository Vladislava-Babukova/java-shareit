package ru.practicum.shareit.request;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.request.dto.ExternalDto;

@Service
public class RequestClient extends BaseClient {
    public RequestClient(RestTemplate restTemplate) {
        super(restTemplate);
    }

    public ResponseEntity<Object> createRequest(int userId, ExternalDto externalDto) {
        return post("", userId, externalDto);
    }

    public ResponseEntity<Object> getRequestsByUser(int userId) {
        return get("", userId);
    }

    public ResponseEntity<Object> getAllRequests() {
        return get("/all");
    }

    public ResponseEntity<Object> getRequestById(int requestId) {
        return get("/" + requestId);
    }
}