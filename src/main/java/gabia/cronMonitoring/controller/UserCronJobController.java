package gabia.cronMonitoring.controller;

import gabia.cronMonitoring.dto.UserCronJobDTO;
import gabia.cronMonitoring.dto.UserCronJobDTO.Response;
import gabia.cronMonitoring.service.UserCronJobService;
import gabia.cronMonitoring.util.ValidUUID;
import java.util.List;
import java.util.UUID;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping(path = "/cron-read-auths/users/{userId}/crons")
@Log4j2
@CrossOrigin(origins = "*")
public class UserCronJobController {

    private final UserCronJobService userCronJobService;

    @GetMapping(path = "/")
    public ResponseEntity<List<UserCronJobDTO.Response>> getUserCronJob(
        @NotEmpty @PathVariable(value = "userId") String userId,
        @PageableDefault(size = 10) Pageable pageable) {

        List<Response> userCronJobList = userCronJobService
            .findUserCronJobByPage(userId, pageable);

        log.info("Success get user cron job list (userId: {}, page_num: {})", userId,
            pageable.getPageNumber());

        return new ResponseEntity<>(userCronJobList, HttpStatus.OK);

    }

    @PostMapping(path = "/")
    public ResponseEntity<UserCronJobDTO.Response> postUserCronJob(
        @NotEmpty @PathVariable(value = "userId") String userId,
        @RequestBody @Valid UserCronJobDTO.Request request) {

        Response response = userCronJobService.addUserCronJob(userId, request);

        log.info("Success make user cron job (userId: {}, cronJobId: {})", userId,
            request.getCronJobId().toString());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping(path = "/{cronJobId}")
    public ResponseEntity<HttpStatus> deleteUserCronJob(
        @NotEmpty @PathVariable(value = "userId") String userId,
        @ValidUUID @PathVariable(value = "cronJobId") UUID cronJobId) {

        userCronJobService.removeUserCronJob(userId, cronJobId);

        log.info("Success delete user cron job (userId: {}, cronJobId: {})", userId,
            cronJobId.toString());

        return new ResponseEntity(HttpStatus.OK);

    }
}
