package org.hamlet.blogwebsite.service;

import org.hamlet.blogwebsite.payload.request.EmailDetails;

public interface EmailService {

    void sendEmailAlert(EmailDetails emailDetails);
}
