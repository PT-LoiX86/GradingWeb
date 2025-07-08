package com.grd.gradingbe.service;

import com.grd.gradingbe.enums.MailType;
import jakarta.mail.MessagingException;

public interface MailService
{
    void sendLinkEmail(MailType type, String to, String subject, String link) throws MessagingException;
}
