package gabia.cronMonitoring.exception.notice.handler;

import gabia.cronMonitoring.dto.NoticeDTO;
import gabia.cronMonitoring.dto.NoticeSubscriptionDTO;
import gabia.cronMonitoring.exception.notice.NoticeNotFoundException;
import gabia.cronMonitoring.exception.notice.noticestatus.AlreadyExistNoticeStatusException;
import gabia.cronMonitoring.exception.notice.usernotice.AlreadyExistNoticeSubscriptionException;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Log4j2
public class NoticeControllerExceptionHandler {

    @ExceptionHandler(AlreadyExistNoticeSubscriptionException.class)
    public ResponseEntity<NoticeSubscriptionDTO.ErrorResponse> existUserNotice(
        AlreadyExistNoticeSubscriptionException e) {
        log.error(e.getMessage());
        return new ResponseEntity<>(new NoticeSubscriptionDTO.ErrorResponse(e.getMessage()),
            HttpStatus.CONFLICT);
    }

    @ExceptionHandler(NoticeNotFoundException.class)
    public ResponseEntity<NoticeDTO.ErrorResponse> noNotice(
        NoticeNotFoundException e) {
        log.error(e.getMessage());
        return new ResponseEntity<>(new NoticeDTO.ErrorResponse(e.getMessage()),
            HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AlreadyExistNoticeStatusException.class)
    public ResponseEntity<NoticeDTO.Response> existNoticeStatus(
        AlreadyExistNoticeStatusException e) {
        log.error(e.getMessage());
        return new ResponseEntity<>(NoticeDTO.Response.from(e.getNotice(), true), HttpStatus.OK);
    }
}
