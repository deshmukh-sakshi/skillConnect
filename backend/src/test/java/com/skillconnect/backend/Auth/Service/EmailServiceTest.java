package com.skillconnect.backend.Auth.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.test.util.ReflectionTestUtils;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmailServiceTest {

    @Mock
    private JavaMailSender mailSender;

    @Mock
    private SpringTemplateEngine templateEngine;

    @Mock
    private MimeMessage mimeMessage;

    @InjectMocks
    private EmailService emailService;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(emailService, "fromAddress", "noreply@skillconnect.com");
        ReflectionTestUtils.setField(emailService, "frontendUrl", "http://localhost:5173");
    }

    @Test
    void sendPasswordResetEmail_success_sendsEmailWithCorrectContent() throws MessagingException {
        String recipientEmail = "user@test.com";
        String resetToken = "test-token-123";
        String userName = "Test User";
        String expectedResetUrl = "http://localhost:5173/auth/reset-password?token=test-token-123";
        String emailContent = "<html><body>Reset your password</body></html>";

        when(templateEngine.process(eq("email/password-reset-email"), any(Context.class)))
                .thenReturn(emailContent);
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
        doNothing().when(mailSender).send(any(MimeMessage.class));

        emailService.sendPasswordResetEmail(recipientEmail, resetToken, userName);

        ArgumentCaptor<Context> contextCaptor = ArgumentCaptor.forClass(Context.class);
        verify(templateEngine).process(eq("email/password-reset-email"), contextCaptor.capture());
        
        Context capturedContext = contextCaptor.getValue();
        assertEquals(userName, capturedContext.getVariable("userName"));
        assertEquals(expectedResetUrl, capturedContext.getVariable("resetUrl"));
        assertEquals("http://localhost:5173", capturedContext.getVariable("frontendUrl"));
        assertNotNull(capturedContext.getVariable("currentDate"));

        verify(mailSender).createMimeMessage();
        verify(mailSender).send(mimeMessage);
    }

    @Test
    void sendPasswordResetEmail_withDifferentToken_generatesCorrectUrl() {
        String recipientEmail = "another@test.com";
        String resetToken = "different-token-456";
        String userName = "Another User";
        String expectedResetUrl = "http://localhost:5173/auth/reset-password?token=different-token-456";

        when(templateEngine.process(eq("email/password-reset-email"), any(Context.class)))
                .thenReturn("<html>content</html>");
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
        doNothing().when(mailSender).send(any(MimeMessage.class));

        emailService.sendPasswordResetEmail(recipientEmail, resetToken, userName);

        ArgumentCaptor<Context> contextCaptor = ArgumentCaptor.forClass(Context.class);
        verify(templateEngine).process(eq("email/password-reset-email"), contextCaptor.capture());
        
        Context capturedContext = contextCaptor.getValue();
        assertEquals(expectedResetUrl, capturedContext.getVariable("resetUrl"));
        assertEquals("Another User", capturedContext.getVariable("userName"));
    }

    @Test
    void sendPasswordResetEmail_setsCorrectEmailTemplate() {
        String recipientEmail = "user@test.com";
        String resetToken = "token";
        String userName = "User";

        when(templateEngine.process(eq("email/password-reset-email"), any(Context.class)))
                .thenReturn("<html>content</html>");
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
        doNothing().when(mailSender).send(any(MimeMessage.class));

        emailService.sendPasswordResetEmail(recipientEmail, resetToken, userName);

        verify(templateEngine).process(eq("email/password-reset-email"), any(Context.class));
    }
}
