package gabia.cronMonitoring.exception.webhook.handler;

import gabia.cronMonitoring.exception.webhook.ExistingWebhookException;
import gabia.cronMonitoring.exception.webhook.NoticeSubscriptionNotFoundException;
import gabia.cronMonitoring.exception.webhook.WebhookNotFoundException;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Log4j2
public class WebhookControllerExceptionHandler {

    @ExceptionHandler(ExistingWebhookException.class)
    public ResponseEntity handle(ExistingWebhookException e) {
        log.error(e.getMessage());
        return new ResponseEntity(e.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(NoticeSubscriptionNotFoundException.class)
    public ResponseEntity handle(NoticeSubscriptionNotFoundException e) {
        log.error(e.getMessage());
        return new ResponseEntity(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(WebhookNotFoundException.class)
    public ResponseEntity handle(WebhookNotFoundException e) {
        log.error(e.getMessage());
        return new ResponseEntity(e.getMessage(), HttpStatus.NOT_FOUND);
    }
}
