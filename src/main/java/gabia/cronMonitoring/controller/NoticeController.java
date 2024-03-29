package gabia.cronMonitoring.controller;

import gabia.cronMonitoring.dto.NoticeDTO;
import gabia.cronMonitoring.dto.NoticeDTO.Response;
import gabia.cronMonitoring.dto.NoticeSubscriptionDTO;
import gabia.cronMonitoring.service.NoticeService;
import gabia.cronMonitoring.util.ValidUUID;
import java.util.List;
import java.util.UUID;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Log4j2
@CrossOrigin(origins = "*")
public class NoticeController {

    private final NoticeService noticeService;

    @GetMapping(path = "/notifications/users/{userId}")
    public ResponseEntity<List<NoticeSubscriptionDTO.Response>> getNoticeSubscription(
        @NotEmpty @PathVariable(value = "userId") String userId,
        @PageableDefault(size = 10) Pageable pageable) {

        List<NoticeSubscriptionDTO.Response> noticeSubscriptionByPage = noticeService
            .findNoticeSubscriptionByPage(userId, pageable);

        log.info("Success get notice subscription list (userId: {}, page_num: {})", userId,
            pageable.getPageNumber());

        return new ResponseEntity<>(noticeSubscriptionByPage, HttpStatus.OK);
    }

    @PostMapping(path = "/notifications/users/{userId}")
    public ResponseEntity<NoticeSubscriptionDTO.Response> postNoticeSubscription(
        @NotEmpty @PathVariable(value = "userId") String userId,
        @RequestBody @Valid NoticeSubscriptionDTO.Request request) {

        NoticeSubscriptionDTO.Response response = noticeService
            .addNoticeSubscription(userId, request);

        log.info("Success make notice subscription (userId: {}"
            + ",cronJobId: {})", userId, request.getCronJobId().toString());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping(path = "/notifications/users/{userId}/crons/{cronJobId}")
    public ResponseEntity<HttpStatus> deleteNoticeSubscription(
        @NotEmpty @PathVariable(value = "userId") String userId,
        @ValidUUID @PathVariable(value = "cronJobId") UUID cronJobId) {

        noticeService.removeNoticeSubscription(userId, cronJobId);

        log.info("Success delete notice subscription (userId: {}, cronJobId)", userId, cronJobId);

        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping(path = "/notifications/users/{userId}/notice")
    public ResponseEntity<List<NoticeDTO.Response>> getNoticeList(
        @NotEmpty @PathVariable(value = "userId") String userId) {

        List<Response> allNotice = noticeService.findAllNotice(userId);

        log.info("Success get notice list (userId: {})", userId);

        return new ResponseEntity<>(allNotice, HttpStatus.OK);
    }

    @GetMapping(path = "/notifications/users/{userId}/notice/{notId}")
    public ResponseEntity<NoticeDTO.Response> getNotice(
        @NotEmpty @PathVariable(value = "userId") String userId,
        @PathVariable(value = "notId") Long notId) {

        Response response = noticeService.selectNotice(userId, notId);

        log.info("Success selecct notice (userId: {}, notId: {})", userId, notId);

        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    @PostMapping(path = "/notifications/notice")
    public ResponseEntity<NoticeDTO.Response> postNotice(
        @RequestBody @Valid NoticeDTO.Request request) {

        Response notice = noticeService.createNotice(request);

        log.info("Success make notice (cronJobId: {})", request.getCronJobId());

        return new ResponseEntity<>(notice, HttpStatus.OK);

    }
}
