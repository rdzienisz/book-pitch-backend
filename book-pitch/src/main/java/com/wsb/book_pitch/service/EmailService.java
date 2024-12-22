package com.wsb.book_pitch.service;

import com.sendgrid.*;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.regex.Pattern;

@Service
public class EmailService {

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");
    private final SendGrid sendGridClient;

    public EmailService(@Value("${sendgrid.api.key}") String sendGridApiKey) {
        this.sendGridClient = new SendGrid(sendGridApiKey);
    }

    public Response sendEmail(String toEmail, String subject, String body) throws IOException {
        validateEmail(toEmail);
        validateNotNull(subject, "Subject");
        validateNotNull(body, "Body");

        Email from = new Email("rdzienisz-wsb@onmail.com");
        Email to = new Email(toEmail);
        Content content = new Content("text/plain", body);
        Mail mail = new Mail(from, subject, to, content);

        Request request = new Request();
        request.setMethod(Method.POST);
        request.setEndpoint("mail/send");
        request.setBody(mail.build());

        return sendGridClient.api(request);
    }

    private void validateEmail(String email) {
        if (email == null || !EMAIL_PATTERN.matcher(email).matches()) {
            throw new IllegalArgumentException("Invalid email address: " + email);
        }
    }

    private void validateNotNull(String value, String fieldName) {
        if (value == null) {
            throw new IllegalArgumentException(fieldName + " cannot be null");
        }
    }
}