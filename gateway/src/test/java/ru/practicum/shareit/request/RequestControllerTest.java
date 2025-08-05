package ru.practicum.shareit.request;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import ru.practicum.shareit.request.dto.ExternalDto;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RequestControllerTest {

    @Mock
    private RequestClient requestClient;

    @InjectMocks
    private RequestController requestController;

    private final int userId = 1;
    private final int requestId = 10;
    private ExternalDto externalDto;

    @BeforeEach
    void setUp() {
        externalDto = new ExternalDto();
        externalDto.setDescription("Test request description");
    }

    @Test
    void create_ShouldInvokeClientAndReturnResponse() {
        ResponseEntity<Object> expectedResponse = ResponseEntity.ok().build();
        when(requestClient.createRequest(userId, externalDto)).thenReturn(expectedResponse);

        ResponseEntity<Object> response = requestController.create(userId, externalDto);

        assertThat(response).isEqualTo(expectedResponse);
        verify(requestClient).createRequest(userId, externalDto);
    }

    @Test
    void getByUser_ShouldInvokeClientAndReturnResponse() {
        ResponseEntity<Object> expectedResponse = ResponseEntity.ok().build();
        when(requestClient.getRequestsByUser(userId)).thenReturn(expectedResponse);

        ResponseEntity<Object> response = requestController.getByUser(userId);

        assertThat(response).isEqualTo(expectedResponse);
        verify(requestClient).getRequestsByUser(userId);
    }

    @Test
    void getAll_ShouldInvokeClientAndReturnResponse() {
        ResponseEntity<Object> expectedResponse = ResponseEntity.ok().build();
        when(requestClient.getAllRequests()).thenReturn(expectedResponse);

        ResponseEntity<Object> response = requestController.getAll();

        assertThat(response).isEqualTo(expectedResponse);
        verify(requestClient).getAllRequests();
    }

    @Test
    void getById_ShouldInvokeClientAndReturnResponse() {
        ResponseEntity<Object> expectedResponse = ResponseEntity.ok().build();
        when(requestClient.getRequestById(requestId)).thenReturn(expectedResponse);

        ResponseEntity<Object> response = requestController.getById(requestId);

        assertThat(response).isEqualTo(expectedResponse);
        verify(requestClient).getRequestById(requestId);
    }

    @Test
    void create_ShouldNotThrowException_WhenUserIdIsZero() {
        int invalidUserId = 0;
        ResponseEntity<Object> expectedResponse = ResponseEntity.ok().build();
        when(requestClient.createRequest(invalidUserId, externalDto)).thenReturn(expectedResponse);

        assertDoesNotThrow(() -> requestController.create(invalidUserId, externalDto));
    }

    @Test
    void getById_ShouldNotThrowException_WhenRequestIdIsNegative() {
        int invalidRequestId = -1;
        ResponseEntity<Object> expectedResponse = ResponseEntity.ok().build();
        when(requestClient.getRequestById(invalidRequestId)).thenReturn(expectedResponse);

        assertDoesNotThrow(() -> requestController.getById(invalidRequestId));
    }
}