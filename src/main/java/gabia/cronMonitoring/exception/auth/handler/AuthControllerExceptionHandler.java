package gabia.cronMonitoring.exception.auth.handler;

import gabia.cronMonitoring.exception.auth.InvalidTokenException;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Log4j2
public class AuthControllerExceptionHandler {

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity handle(InvalidTokenException e) {
        log.error(e.getMessage());
        return new ResponseEntity(e.getMessage(), HttpStatus.UNAUTHORIZED);
    }
}
