package gabia.cronMonitoring.controller;

import gabia.cronMonitoring.dto.CronServerDTO;
import gabia.cronMonitoring.service.CronServerService;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Log4j2
public class CronServerController {

    private final CronServerService cronServerService;

    @GetMapping(value = "/cron-servers")
    @ResponseBody
    public ResponseEntity<List<CronServerDTO>> getCronServers() {
        List<CronServerDTO> cronServers = cronServerService.getCronServers();
        ResponseEntity responseEntity = new ResponseEntity(cronServers, HttpStatus.OK);

        log.info("Success get cron server list");

        return responseEntity;
    }

    @PostMapping(value = "/cron-servers")
    @ResponseBody
    public ResponseEntity<CronServerDTO> postCronServers(@Valid @RequestBody CronServerDTO dto) {
        CronServerDTO cronServerDTO = cronServerService.addCronServer(dto.getServerIp());
        ResponseEntity responseEntity = new ResponseEntity(cronServerDTO, HttpStatus.CREATED);

        log.info("Success make cron server (serverIp: {})", dto.getServerIp());

        return responseEntity;
    }

    @PatchMapping("/cron-servers/{serverIp}")
    @ResponseBody
    public ResponseEntity<CronServerDTO> patchCronServer(
        @Valid @PathVariable(name = "serverIp") String serverIp,
        @RequestBody CronServerDTO cronServerDTO) {
        CronServerDTO cronServer = cronServerService
            .updateCronServer(serverIp, cronServerDTO.getServerIp());
        ResponseEntity responseEntity = new ResponseEntity(cronServer, HttpStatus.OK);

        log.info("Success update cron server (serverIp: {})", serverIp);

        return responseEntity;
    }

    @DeleteMapping(value = "/cron-servers/{serverIp}")
    @ResponseBody
    public ResponseEntity deleteCronServer(@Valid @PathVariable(name = "serverIp") String serverIp) {
        cronServerService.deleteCronServer(serverIp);
        ResponseEntity responseEntity = new ResponseEntity(HttpStatus.NO_CONTENT);

        log.info("Success delete server (serverIp: {})", serverIp);

        return responseEntity;
    }
}