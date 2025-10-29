package com.pdfocus.application.feedback.service;

import com.pdfocus.core.models.Feedback;
import com.pdfocus.infra.email.EmailFeedbackService;
import org.springframework.stereotype.Service;

@Service
public class FeedbackEmailService {

    private final EmailFeedbackService emailFeedbackService;

    public FeedbackEmailService(EmailFeedbackService emailFeedbackService) {
        this.emailFeedbackService = emailFeedbackService;
    }

    public void enviarEmailFeedback(Feedback feedback) {
        emailFeedbackService.enviarEmailFeedback(feedback);
    }
}