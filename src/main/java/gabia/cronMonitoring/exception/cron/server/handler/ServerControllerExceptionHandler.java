package gabia.cronMonitoring.exception.cron.server.handler;

import gabia.cronMonitoring.exception.cron.server.AlreadyRegisteredServerException;
import gabia.cronMonitoring.exception.cron.server.CronServerNotFoundException;
import gabia.cronMonitoring.exception.cron.server.NotExistingServerException;
import gabia.cronMonitoring.exception.cron.server.NotValidIPException;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Log4j2
public class ServerControllerExceptionHandler {

    @ExceptionHandler(AlreadyRegisteredServerException.class)
    public ResponseEntity handle(AlreadyRegisteredServerException e) {
        log.error(e.getMessage());
        return new ResponseEntity(e.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(CronServerNotFoundException.class)
    public ResponseEntity handle(CronServerNotFoundException e) {
        log.error(e.getMessage());
        return new ResponseEntity(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NotExistingServerException.class)
    public ResponseEntity handle(NotExistingServerException e) {
        log.error(e.getMessage());
        return new ResponseEntity(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NotValidIPException.class)
    public ResponseEntity handle(NotValidIPException e) {
        log.error(e.getMessage());
        return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
