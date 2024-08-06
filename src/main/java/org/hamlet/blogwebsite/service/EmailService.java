package org.hamlet.blogwebsite.service;

import jakarta.mail.MessagingException;
import org.hamlet.blogwebsite.payload.request.EmailDetails;

public interface EmailService {

    void sendEmailAlert(EmailDetails emailDetails);
    void sendSimpleMailMessage(EmailDetails message, String firstName, String lastName, String link) throws MessagingException;

}
