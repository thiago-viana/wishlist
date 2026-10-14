package com.thiago.wishlist.infrastructure.web.handler;

import com.thiago.wishlist.domain.exception.BusinessException;
import com.thiago.wishlist.domain.exception.NotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.NoHandlerFoundException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Nested
    @DisplayName("handleBusinessException")
    class HandleBusinessException {

        @Test
        void returnsBadRequestWithExceptionMessage() {
            BusinessException ex = new BusinessException("Product already in list");

            ResponseEntity<String> response = handler.handleBusinessException(ex);

            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
            assertEquals("Product already in list", response.getBody());
        }

        @Test
        void returnsBadRequestWithDifferentMessage() {
            BusinessException ex = new BusinessException("Wishlist limit reached");

            ResponseEntity<String> response = handler.handleBusinessException(ex);

            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
            assertEquals("Wishlist limit reached", response.getBody());
        }
    }

    @Nested
    @DisplayName("handleNotFoundException")
    class HandleNotFoundException {

        @Test
        void returnsNotFoundWithExceptionMessage() {
            NotFoundException ex = new NotFoundException("Wishlist not found");

            ResponseEntity<String> response = handler.handleNotFoundException(ex);

            assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
            assertEquals("Wishlist not found", response.getBody());
        }

        @Test
        void returnsNotFoundWhenProductNotFound() {
            NotFoundException ex = new NotFoundException("Product not found in wishlist");

            ResponseEntity<String> response = handler.handleNotFoundException(ex);

            assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
            assertEquals("Product not found in wishlist", response.getBody());
        }
    }

    @Nested
    @DisplayName("handleIllegalArgumentException")
    class HandleIllegalArgumentException {

        @Test
        void returnsBadRequestWithFormattedMessage() {
            IllegalArgumentException ex = new IllegalArgumentException("productId cannot be null or blank");

            ResponseEntity<String> response = handler.handleIllegalArgumentException(ex);

            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
            assertTrue(response.getBody().contains("Invalid argument:"));
            assertTrue(response.getBody().contains("productId cannot be null or blank"));
        }

        @Test
        void returnsBadRequestWithCustomIdMessage() {
            IllegalArgumentException ex = new IllegalArgumentException("customerId cannot be null or blank");

            ResponseEntity<String> response = handler.handleIllegalArgumentException(ex);

            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
            assertEquals("Invalid argument: customerId cannot be null or blank", response.getBody());
        }
    }

    @Nested
    @DisplayName("handleNoHandlerFoundException")
    class HandleNoHandlerFoundException {

        @Test
        void returnsBadRequestWithFixedMessage() throws Exception {
            NoHandlerFoundException ex = new NoHandlerFoundException("GET", "/invalid-endpoint", null);

            ResponseEntity<String> response = handler.handleNoHandlerFoundException(ex);

            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
            assertEquals("THe required parameter is not in the path.", response.getBody());
        }

        @Test
        void alwaysReturnsFixedMessageRegardlessOfException() throws Exception {
            NoHandlerFoundException ex = new NoHandlerFoundException("POST", "/some/path", null);

            ResponseEntity<String> response = handler.handleNoHandlerFoundException(ex);

            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
            assertEquals("THe required parameter is not in the path.", response.getBody());
        }
    }
}
