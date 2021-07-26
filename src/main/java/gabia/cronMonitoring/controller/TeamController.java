package gabia.cronMonitoring.controller;

import gabia.cronMonitoring.dto.TeamDTO;
import gabia.cronMonitoring.dto.TeamUserDTO;
import gabia.cronMonitoring.service.TeamService;
import java.util.List;
import javax.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Log4j2
public class TeamController {

    private final TeamService teamService;

    @GetMapping(value = "/teams/{teamId}")
    public ResponseEntity<TeamDTO.Response> getTeam(
        @NotBlank @PathVariable(name = "teamId") String teamId) {
        TeamDTO.Response response = teamService.findTeam(teamId);

        log.info("Success get team (teamId: {})", teamId);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(value = "/teams")
    public ResponseEntity<List<TeamDTO.Response>> getTeamList() {
        List<TeamDTO.Response> teamList = teamService.findTeamAll();

        log.info("Success get team list");

        return new ResponseEntity<>(teamList, HttpStatus.OK);
    }

    @PostMapping(value = "/teams")
    public ResponseEntity<TeamDTO.Response> createTeam(@RequestBody TeamDTO.Request request) {
        TeamDTO.Response response = teamService.createTeam(request, request.getUserAccount());

        log.info("Success make team (teamId: {})", request.getTeamAccount());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PatchMapping(value = "/teams/{teamId}")
    public ResponseEntity<TeamDTO.Response> changeTeamName(
        @PathVariable(name = "teamId") @NotNull String teamId,
        @RequestBody TeamDTO.Request request) {
        //request.setTeamAccount(teamId); // 바디 or pathparam의 teamId중 뭘 쓸지 선택 필요
        TeamDTO.Response response = teamService.changeTeam(request, request.getUserAccount());

        log.info("Success update team (teamId: {})", teamId);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping(value = "/teams/{teamId}")
    public ResponseEntity deleteTeam(@PathVariable(name = "teamId") @NotNull String teamId,
        @RequestBody TeamDTO.Request request) {
        teamService.deleteTeam(teamId, request.getUserAccount());

        log.info("Success delete team (teamId: {})", teamId);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value = "/teams/{teamId}/users")
    public ResponseEntity findMembers(@PathVariable(name = "teamId") @NotNull String teamId) {
        List<TeamUserDTO.Response> members = teamService.findMembers(teamId);

        log.info("Success get team members (teamId: {})", teamId);

        return new ResponseEntity(members, HttpStatus.OK);
    }

    @PostMapping(value = "/teams/{teamId}/users")
    public ResponseEntity addMember(@PathVariable(name = "teamId") @NotNull String teamId,
        @RequestBody TeamUserDTO.Request request) {
        TeamUserDTO.Response response = teamService.addMember(request);

        log.info("Success add team memeber (teamId: {}, userId: {})", teamId,
            request.getUserAccount());

        return new ResponseEntity(response, HttpStatus.OK);
    }

    @PatchMapping(value = "/teams/{teamId}/users")
    public ResponseEntity changeMemberAuth(@PathVariable(name = "teamId") @NotNull String teamId,
        @RequestBody TeamUserDTO.Request request) {
        TeamUserDTO.Response response = teamService.changeMemberAuthType(request);

        log.info("Success update team memeber (teamId: {}, userId: {})", teamId,
            request.getUserAccount());

        return new ResponseEntity(response, HttpStatus.OK);
    }

    @DeleteMapping(value = "/teams/{teamId}/users/{userId}")
    public ResponseEntity deleteMember(@PathVariable(name = "teamId") @NotNull String teamId,
        @PathVariable(name = "userId") @NotNull String userId,
        @RequestBody TeamUserDTO.Request request) {
        teamService.deleteMember(request);

        log.info("Success delete team memeber (teamId: {}, userId: {})", teamId,userId);

        return new ResponseEntity(HttpStatus.OK);
    }

}
