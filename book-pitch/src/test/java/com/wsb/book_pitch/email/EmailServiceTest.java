package com.wsb.book_pitch.email;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.sendgrid.*;
import com.wsb.book_pitch.service.EmailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;

class EmailServiceTest {

    @Mock
    private SendGrid sendGridClient;

    @InjectMocks
    private EmailService emailService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        emailService = new EmailService("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");
    }

    @Test
    void shouldReturn202WhenEmailIsSentSuccessfully() throws IOException {
        // Arrange
        String toEmail = "test@example.com";
        String subject = "Test Subject";
        String body = "Test Body";
        Response response = new Response();
        response.setStatusCode(202);

        when(sendGridClient.api(any(Request.class))).thenReturn(response);

        // Act
        Response actualResponse = emailService.sendEmail(toEmail, subject, body);

        // Assert
        assertEquals(202, actualResponse.getStatusCode(), "Expected status code 202");
    }

    @Test
    void shouldHandleInvalidEmailGracefully() {
        // Arrange
        String toEmail = "invalid-email";
        String subject = "Test Subject";
        String body = "Test Body";

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            emailService.sendEmail(toEmail, subject, body);
        });
    }

    @Test
    void shouldThrowExceptionWhenEmailIsNull() {
        // Arrange
        String subject = "Test Subject";
        String body = "Test Body";

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            emailService.sendEmail(null, subject, body);
        });
    }

    @Test
    void shouldThrowExceptionWhenSubjectIsNull() {
        // Arrange
        String toEmail = "test@example.com";
        String body = "Test Body";

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            emailService.sendEmail(toEmail, null, body);
        });
    }

    @Test
    void shouldThrowExceptionWhenBodyIsNull() {
        // Arrange
        String toEmail = "test@example.com";
        String subject = "Test Subject";

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            emailService.sendEmail(toEmail, subject, null);
        });
    }
}