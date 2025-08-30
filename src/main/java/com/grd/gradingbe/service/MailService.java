package com.grd.gradingbe.service;

import com.grd.gradingbe.dto.enums.MailType;
import jakarta.mail.MessagingException;

public interface MailService
{
    void sendLinkEmail(MailType type, String to, String link) throws MessagingException;
}
