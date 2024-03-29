package gabia.cronMonitoring.integration;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import gabia.cronMonitoring.controller.TeamController;
import gabia.cronMonitoring.dto.TeamDTO;
import gabia.cronMonitoring.dto.TeamUserDTO;
import gabia.cronMonitoring.dto.TeamUserDTO.Response;
import gabia.cronMonitoring.entity.Enum.AuthType;
import gabia.cronMonitoring.entity.Enum.UserRole;
import gabia.cronMonitoring.entity.Team;
import gabia.cronMonitoring.entity.TeamUser;
import gabia.cronMonitoring.entity.User;
import gabia.cronMonitoring.repository.TeamRepository;
import gabia.cronMonitoring.repository.TeamUserRepository;
import gabia.cronMonitoring.repository.UserRepository;
import gabia.cronMonitoring.service.TeamService;
import gabia.cronMonitoring.util.CronMonitorUtil;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest(properties = "spring.profiles.active:test")
@AutoConfigureMockMvc
@WithMockUser(roles = "USER")
public class TeamIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    TeamController teamController;

    @Autowired
    TeamService teamService;

    @Autowired
    TeamRepository teamRepository;

    @Autowired
    TeamUserRepository teamUserRepository;

    @Autowired
    UserRepository userRepository;

    @Test
    @Transactional
    public void 팀_목록_조회_성공() throws Exception {
        User user = User.builder().name("윤현우").email("yhw@gabia.com").password("1234")
            .account("yhw").role(UserRole.ROLE_USER).build();
        userRepository.save(user);

        for (int i = 0; i < 10; i++) {
            Team team = Team.builder().name("teamName" + i).account("account" + i).build();
            teamRepository.save(team);
            teamUserRepository.save(
                TeamUser.builder().team(team).user(user).authority(AuthType.UserManager).build());
        }

        //jsonPath를 이용한 검증
        String returnJson = mockMvc.perform(get("/teams"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(10)))
            .andExpect(jsonPath("$.[0].teamAccount").value("account0"))
            .andDo(print())
            .andReturn().getResponse().getContentAsString();

        //객체화해 검증 추구 구현
        //List<TeamDTO> result = CronMonitorUtil.jsonStrToObj(returnJson, List.class);

    }

    @Test
    @Transactional
    public void 팀_조회_성공() throws Exception {
        User user = User.builder().name("윤현우").email("yhw@gabia.com").password("1234")
            .account("yhw").role(UserRole.ROLE_USER).build();
        userRepository.save(user);

        for (int i = 0; i < 10; i++) {
            Team team = Team.builder().name("teamName" + i).account("account" + i).build();
            teamRepository.save(team);
            teamUserRepository.save(
                TeamUser.builder().team(team).user(user).authority(AuthType.UserManager).build());
        }

        //jsonPath를 이용한 검증
        String returnJson = mockMvc.perform(get("/teams/{teamId}", "account2"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.teamAccount").value("account2"))
            .andDo(print())
            .andReturn().getResponse().getContentAsString();

        //객체화해 검증 추구 구현
        //List<TeamDTO> result = CronMonitorUtil.jsonStrToObj(returnJson, List.class);

    }

    @Test
    @Transactional
    public void 팀_생성_성공() throws Exception {
        User user = User.builder().name("윤현우").email("yhw@gabia.com").password("1234")
            .account("yhw").role(UserRole.ROLE_USER).build();
        userRepository.save(user);

        TeamDTO.Request teamDTORequest = new TeamDTO.Request();
        teamDTORequest.setTeamAccount("team1");
        teamDTORequest.setName("cronmonitoring");
        teamDTORequest.setUserAccount("yhw");
        String requestJson = CronMonitorUtil.objToJson(teamDTORequest);

        //jsonPath를 이용한 검증
        String returnJson = mockMvc.perform(post("/teams/")
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestJson))
            .andExpect(status().isOk())
            .andDo(print())
            .andReturn().getResponse().getContentAsString();
    }

    @Test
    @Transactional
    public void 팀_수정_성공() throws Exception {
        User user = User.builder().name("윤현우").email("yhw@gabia.com").password("1234")
            .account("yhw").role(UserRole.ROLE_USER).build();
        userRepository.save(user);

        Team team = Team.builder().account("team1").name("cronmonitor").build();
        teamRepository.save(team);

        TeamUser teamUser =
            TeamUser.builder().user(user).team(team).authority(AuthType.UserManager).build();
        teamUserRepository.save(teamUser);

        TeamDTO.Request teamDTORequest = new TeamDTO.Request();
        teamDTORequest.setTeamAccount("team1");
        teamDTORequest.setName("webhook");
        teamDTORequest.setUserAccount("yhw");
        String requestJson = CronMonitorUtil.objToJson(teamDTORequest);

        //jsonPath를 이용한 검증
        String returnJson = mockMvc.perform(patch("/teams/{teamId}", team.getAccount())
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestJson))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("webhook"))
            .andDo(print())
            .andReturn().getResponse().getContentAsString();
    }

    @Test
    @Transactional
    public void 팀_삭제_성공() throws Exception {
        User user1 = User.builder().name("윤현우").email("yhw@gabia.com").password("1234")
            .account("yhw").role(UserRole.ROLE_USER).build();
        User user2 = User.builder().name("김기정").email("kkj@gabia.com").password("1234")
            .account("kkj").role(UserRole.ROLE_USER).build();
        User user3 = User.builder().name("주영준").email("jyj@gabia.com").password("1234")
            .account("jyj").role(UserRole.ROLE_USER).build();

        userRepository.save(user1);
        userRepository.save(user2);
        userRepository.save(user3);

        Team team = Team.builder().account("team1").name("cronmonitor").build();
        teamRepository.save(team);

        TeamUser teamUser1 =
            TeamUser.builder().user(user1).team(team).authority(AuthType.UserManager).build();
        TeamUser teamUser2 =
            TeamUser.builder().user(user2).team(team).authority(AuthType.UserManager).build();
        TeamUser teamUser3 =
            TeamUser.builder().user(user3).team(team).authority(AuthType.UserManager).build();
        teamUserRepository.save(teamUser1);
        teamUserRepository.save(teamUser2);
        teamUserRepository.save(teamUser3);

        TeamDTO.Request teamDTORequest = new TeamDTO.Request();
        teamDTORequest.setTeamAccount("team1");
        teamDTORequest.setName("cronmonitor");
        teamDTORequest.setUserAccount("yhw");

        String requestJson = CronMonitorUtil.objToJson(teamDTORequest);

        //jsonPath를 이용한 검증
        String returnJson = mockMvc.perform(delete("/teams/{teamId}", team.getAccount())
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestJson))
            .andExpect(status().isOk())
            .andDo(print())
            .andReturn().getResponse().getContentAsString();
    }

    @Test
    @Transactional
    public void 팀원찾기_성공() throws Exception {
        //given
        User user1 = User.builder().name("윤현우").email("yhw@gabia.com").password("1234")
            .account("yhw").role(UserRole.ROLE_USER).build();
        User user2 = User.builder().name("김기정").email("kkj@gabia.com").password("1234")
            .account("kkj").role(UserRole.ROLE_USER).build();
        User user3 = User.builder().name("주영준").email("jyj@gabia.com").password("1234")
            .account("jyj").role(UserRole.ROLE_USER).build();

        userRepository.save(user1);
        userRepository.save(user2);
        userRepository.save(user3);

        Team team = Team.builder().account("team1").name("cronmonitor").build();
        teamRepository.save(team);

        TeamUser teamUser1 =
            TeamUser.builder().user(user1).team(team).authority(AuthType.UserManager).build();
        TeamUser teamUser2 =
            TeamUser.builder().user(user2).team(team).authority(AuthType.UserManager).build();
        TeamUser teamUser3 =
            TeamUser.builder().user(user3).team(team).authority(AuthType.UserManager).build();
        teamUserRepository.save(teamUser1);
        teamUserRepository.save(teamUser2);
        teamUserRepository.save(teamUser3);

        //when
        mockMvc.perform(get("/teams/{teamId}/users", team.getAccount()))
            //then
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.[0].userAccount").value("yhw"))
            .andReturn();
    }


    @Test
    @Transactional
    public void 팀원_등록_성공() throws Exception {
        //given
        User user1 = User.builder().name("윤현우").email("yhw@gabia.com").password("1234")
            .account("yhw").role(UserRole.ROLE_USER).build();
        userRepository.save(user1);
        Team team = Team.builder().account("team1").name("cronmonitor").build();
        teamRepository.save(team);

        TeamUserDTO.Request requestMember = TeamUserDTO.Request.builder().teamAccount("team1")
            .userAccount("yhw").build();
        String requestJson = CronMonitorUtil.objToJson(requestMember);

        //when
        String returnJson = mockMvc.perform(post("/teams/{teamId}/users", team.getAccount())
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestJson))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect((jsonPath("$.userAccount").value("yhw")))
            .andReturn().getResponse().getContentAsString();
    }

    @Test
    @Transactional
    public void 팀원권한수정_성공() throws Exception {
        User user1 = User.builder().name("윤현우").email("yhw@gabia.com").password("1234")
            .account("yhw").role(UserRole.ROLE_USER).build();
        userRepository.save(user1);
        Team team = Team.builder().account("team1").name("cronmonitor").build();
        teamRepository.save(team);
        TeamUser teamUser1 =
            TeamUser.builder().user(user1).team(team).authority(AuthType.User).build();
        teamUserRepository.save(teamUser1);

        TeamUserDTO.Request requestMember = TeamUserDTO.Request.builder().teamAccount("team1")
            .userAccount("yhw").authType(AuthType.UserManager).build();


        String requestJson = CronMonitorUtil.objToJson(requestMember);

        mockMvc.perform(patch("/teams/{teamId}/users", "team1")
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestJson))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect((jsonPath("$.authType").value(AuthType.UserManager.toString())));
    }

    @Test
    @Transactional
    public void 팀원삭제_성공() throws Exception {
        User user1 = User.builder().name("윤현우").email("yhw@gabia.com").password("1234")
            .account("yhw").role(UserRole.ROLE_USER).build();
        User user2 = User.builder().name("김기정").email("kkj@gabia.com").password("1234")
            .account("kkj").role(UserRole.ROLE_USER).build();
        User user3 = User.builder().name("주영준").email("jyj@gabia.com").password("1234")
            .account("jyj").role(UserRole.ROLE_USER).build();

        userRepository.save(user1);
        userRepository.save(user2);
        userRepository.save(user3);
        Team team = Team.builder().account("team1").name("cronmonitor").build();
        teamRepository.save(team);
        TeamUser teamUser1 =
            TeamUser.builder().user(user1).team(team).authority(AuthType.UserManager).build();
        TeamUser teamUser2 =
            TeamUser.builder().user(user2).team(team).authority(AuthType.UserManager).build();
        TeamUser teamUser3 =
            TeamUser.builder().user(user3).team(team).authority(AuthType.UserManager).build();
        teamUserRepository.save(teamUser1);
        teamUserRepository.save(teamUser2);
        teamUserRepository.save(teamUser3);


        TeamUserDTO.Request requestMember = TeamUserDTO.Request.builder().teamAccount("team1")
            .userAccount("yhw").authType(AuthType.UserManager).build();
        String requestJson = CronMonitorUtil.objToJson(requestMember);
        mockMvc.perform(delete("/teams/{teamId}/users/{userId}", "team1", "user1")
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestJson))
            .andDo(print())
            .andExpect(status().isOk());
        Assertions.assertThat(teamService.findMembers("team1").size()).isEqualTo(2);

    }


}
