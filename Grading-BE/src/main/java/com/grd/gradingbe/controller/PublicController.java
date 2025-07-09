package com.grd.gradingbe.controller;

import com.grd.gradingbe.dto.enums.MailType;
import com.grd.gradingbe.service.MailService;
import jakarta.mail.MessagingException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api/public")
public class PublicController {

    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> health() {
        return ResponseEntity.ok(Map.of(
                "status", "UP",
                "timestamp", LocalDateTime.now(),
                "message", "Application is running"
        ));
    }

    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("Public endpoint works!");
    }

    //Mail sending testing block, remove after completed testing
    private final MailService mailService;
    public PublicController(MailService mailService) {
        this.mailService = mailService;
    }
    @GetMapping("/mail/test")
    public ResponseEntity<String> testMailSending() throws MessagingException {
        mailService.sendLinkEmail(MailType.REGISTRATION,
                "phanthanhloi92017@gmail.com",
                "test",
                "https://www.youtube.com/watch?v=dQw4w9WgXcQ");
        return ResponseEntity.ok("Check you email!");
    }
}
