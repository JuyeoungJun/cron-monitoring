package gabia.cronMonitoring.exception.user.handler;

import gabia.cronMonitoring.exception.user.ExistingInputException;
import gabia.cronMonitoring.exception.user.InputNotFoundException;
import gabia.cronMonitoring.exception.user.NotValidEmailException;
import gabia.cronMonitoring.exception.user.UserNotFoundException;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Log4j2
public class UserControllerExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity handle(UserNotFoundException e) {
        log.error(e.getMessage());
        return new ResponseEntity(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ExistingInputException.class)
    public ResponseEntity handle(ExistingInputException e) {
        log.error(e.getMessage());
        return new ResponseEntity(e.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(InputNotFoundException.class)
    public ResponseEntity handle(InputNotFoundException e) {
        log.error(e.getMessage());
        return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotValidEmailException.class)
    public ResponseEntity handle(NotValidEmailException e) {
        log.error(e.getMessage());
        return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
