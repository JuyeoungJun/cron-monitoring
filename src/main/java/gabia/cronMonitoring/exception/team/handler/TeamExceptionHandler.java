package gabia.cronMonitoring.exception.team.handler;

import gabia.cronMonitoring.dto.TeamDTO;
import gabia.cronMonitoring.dto.TeamDTO.ErrorResponse;
import gabia.cronMonitoring.exception.team.TeamNotFoundException;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Log4j2
public class TeamExceptionHandler {

    @ExceptionHandler(TeamNotFoundException.class)
    public ResponseEntity<ErrorResponse> noTeam(TeamNotFoundException e) {
        log.error(e.getMessage());
        return new ResponseEntity<>(new TeamDTO.ErrorResponse(e.getMessage()),
            HttpStatus.NOT_FOUND);
    }
}
