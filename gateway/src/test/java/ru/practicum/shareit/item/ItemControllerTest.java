package ru.practicum.shareit.item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import ru.practicum.shareit.item.dto.CommentCreateDto;
import ru.practicum.shareit.item.dto.RequestItemDto;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ItemControllerTest {

    @Mock
    private ItemClient itemClient;

    @InjectMocks
    private ItemController itemController;

    private final int userId = 1;
    private final int itemId = 10;
    private final String text = "test";
    private RequestItemDto requestItemDto;
    private CommentCreateDto commentCreateDto;

    @BeforeEach
    void setUp() {
        requestItemDto = new RequestItemDto();
        requestItemDto.setName("Item Name");
        requestItemDto.setDescription("Item Description");
        requestItemDto.setAvailable(true);

        commentCreateDto = new CommentCreateDto();
        commentCreateDto.setText("Test comment");
    }

    @Test
    void createItem_ShouldInvokeClientAndReturnResponse() {
        ResponseEntity<Object> expectedResponse = ResponseEntity.ok().build();
        when(itemClient.createItem(userId, requestItemDto)).thenReturn(expectedResponse);

        ResponseEntity<Object> response = itemController.createItem(userId, requestItemDto);

        assertThat(response).isEqualTo(expectedResponse);
        verify(itemClient).createItem(userId, requestItemDto);
    }

    @Test
    void updateItem_ShouldInvokeClientAndReturnResponse() {
        ResponseEntity<Object> expectedResponse = ResponseEntity.ok().build();
        when(itemClient.updateItem(itemId, userId, requestItemDto)).thenReturn(expectedResponse);

        ResponseEntity<Object> response = itemController.updateItem(itemId, userId, requestItemDto);

        assertThat(response).isEqualTo(expectedResponse);
        verify(itemClient).updateItem(itemId, userId, requestItemDto);
    }

    @Test
    void getItem_ShouldInvokeClientAndReturnResponse() {
        ResponseEntity<Object> expectedResponse = ResponseEntity.ok().build();
        when(itemClient.getItem(itemId)).thenReturn(expectedResponse);

        ResponseEntity<Object> response = itemController.getItem(itemId);

        assertThat(response).isEqualTo(expectedResponse);
        verify(itemClient).getItem(itemId);
    }

    @Test
    void getAllItems_ShouldInvokeClientAndReturnResponse() {
        ResponseEntity<Object> expectedResponse = ResponseEntity.ok().build();
        when(itemClient.getAllItems(userId)).thenReturn(expectedResponse);

        ResponseEntity<Object> response = itemController.getAllItems(userId);

        assertThat(response).isEqualTo(expectedResponse);
        verify(itemClient).getAllItems(userId);
    }

    @Test
    void searchItems_ShouldInvokeClientAndReturnResponse() {
        ResponseEntity<Object> expectedResponse = ResponseEntity.ok().build();
        when(itemClient.searchItems(text)).thenReturn(expectedResponse);

        ResponseEntity<Object> response = itemController.searchItems(text);

        assertThat(response).isEqualTo(expectedResponse);
        verify(itemClient).searchItems(text);
    }

    @Test
    void createComment_ShouldInvokeClientAndReturnResponse() {
        ResponseEntity<Object> expectedResponse = ResponseEntity.ok().build();
        when(itemClient.createComment(itemId, userId, commentCreateDto)).thenReturn(expectedResponse);

        ResponseEntity<Object> response = itemController.createComment(itemId, userId, commentCreateDto);

        assertThat(response).isEqualTo(expectedResponse);
        verify(itemClient).createComment(itemId, userId, commentCreateDto);
    }

    @Test
    void createItem_ShouldNotThrowException_WhenUserIdIsZero() {
        int invalidUserId = 0;
        ResponseEntity<Object> expectedResponse = ResponseEntity.ok().build();
        when(itemClient.createItem(invalidUserId, requestItemDto)).thenReturn(expectedResponse);

        ResponseEntity<Object> response = itemController.createItem(invalidUserId, requestItemDto);

        assertThat(response).isEqualTo(expectedResponse);
    }

    @Test
    void updateItem_ShouldNotThrowException_WhenItemIdIsNegative() {
        int invalidItemId = -1;
        ResponseEntity<Object> expectedResponse = ResponseEntity.ok().build();
        when(itemClient.updateItem(invalidItemId, userId, requestItemDto)).thenReturn(expectedResponse);

        ResponseEntity<Object> response = itemController.updateItem(invalidItemId, userId, requestItemDto);

        assertThat(response).isEqualTo(expectedResponse);
    }
}