package gabia.cronMonitoring.exception.cron.handler;

import gabia.cronMonitoring.dto.CronProcessDto;
import gabia.cronMonitoring.dto.CronProcessDto.ErrorResponse;
import gabia.cronMonitoring.dto.TeamCronJobDTO;
import gabia.cronMonitoring.dto.UserCronJobDTO;
import gabia.cronMonitoring.exception.cron.process.CronJobNotFoundException;
import gabia.cronMonitoring.exception.cron.process.CronProcessNotFoundException;
import gabia.cronMonitoring.exception.cron.team.TeamNotFoundException;
import gabia.cronMonitoring.exception.cron.user.UserNotFoundException;
import gabia.cronMonitoring.exception.teamcronjob.AlreadyExistTeamCronJobException;
import gabia.cronMonitoring.exception.usercronjob.AlreadyExistUserCronJobException;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Log4j2
public class ControllerExceptionHandler {

    @ExceptionHandler(CronJobNotFoundException.class)
    public ResponseEntity<ErrorResponse> noCronJob(CronJobNotFoundException e) {
        log.error(e.getMessage());
        return new ResponseEntity<>(new CronProcessDto.ErrorResponse(e.getMessage()),
            HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CronProcessNotFoundException.class)
    public ResponseEntity<CronProcessDto.ErrorResponse> noCronProcess(
        CronProcessNotFoundException e) {
        log.error(e.getMessage());
        return new ResponseEntity<>(new CronProcessDto.ErrorResponse(e.getMessage()),
            HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<UserCronJobDTO.ErrorResponse> noUser(UserNotFoundException e) {
        log.error(e.getMessage());
        return new ResponseEntity<>(new UserCronJobDTO.ErrorResponse(e.getMessage()),
            HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(TeamNotFoundException.class)
    public ResponseEntity<TeamCronJobDTO.ErrorResponse> noTeam(TeamNotFoundException e) {
        log.error(e.getMessage());
        return new ResponseEntity<>(new TeamCronJobDTO.ErrorResponse(e.getMessage()),
            HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AlreadyExistTeamCronJobException.class)
    public ResponseEntity<TeamCronJobDTO.ErrorResponse> existTeamCronJob(
        AlreadyExistTeamCronJobException e) {
        log.error(e.getMessage());
        return new ResponseEntity<>(new TeamCronJobDTO.ErrorResponse(e.getMessage()),
            HttpStatus.CONFLICT);
    }

    @ExceptionHandler(AlreadyExistUserCronJobException.class)
    public ResponseEntity<UserCronJobDTO.ErrorResponse> existUserCronJob(
        AlreadyExistUserCronJobException e) {
        log.error(e.getMessage());
        return new ResponseEntity<>(new UserCronJobDTO.ErrorResponse(e.getMessage()),
            HttpStatus.CONFLICT);
    }
}
