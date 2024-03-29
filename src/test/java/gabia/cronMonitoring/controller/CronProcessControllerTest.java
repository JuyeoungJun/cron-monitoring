package gabia.cronMonitoring.controller;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

import com.fasterxml.jackson.databind.ObjectMapper;
import gabia.cronMonitoring.dto.CronLogDto;
import gabia.cronMonitoring.dto.CronProcessDto.Request;
import gabia.cronMonitoring.dto.CronProcessDto.Response;
import gabia.cronMonitoring.service.CronProcessService;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(MockitoJUnitRunner.class)
@WebMvcTest(CronProcessController.class)
public class CronProcessControllerTest {

    @Autowired
    private MockMvc mvc;

    @Mock
    CronProcessService cronProcessService;

    @InjectMocks
    CronProcessController cronProcessController;


    @Before
    public void setUpMockMvc() {
        cronProcessController = new CronProcessController(cronProcessService);
        mvc = standaloneSetup(cronProcessController)
            .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
            .build();
    }

    @Test
    public void 모든_크론_프로세스_조회() throws Exception {

        //given
        List<Response> allResponse = new ArrayList<>();
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        Response testResponse = new Response();
        testResponse.setCronJobId(UUID.fromString("123e4567-e89b-12d3-a456-556642440000"));
        testResponse.setPid("12");
        testResponse.setStartTime(timestamp);

        Response testResponse2 = new Response();
        testResponse2.setCronJobId(UUID.fromString("123e4567-e89b-12d3-a456-556642440002"));
        testResponse2.setPid("15");
        testResponse2.setStartTime(timestamp);

        allResponse.add(testResponse);
        allResponse.add(testResponse2);

        //when
        given(cronProcessService
            .findCronProcessByPage(eq(UUID.fromString("123e4567-e89b-12d3-a456-556642440000")),
                Mockito.any(Pageable.class)))
            .willReturn(allResponse);

        //then
        mvc.perform(
            get("/cron-servers/{serverIp}/cron-jobs/{cronJobId}/process/", "0.0.0.0",
                UUID.fromString("123e4567-e89b-12d3-a456-556642440000")))
            .andDo(print())
            .andExpect(jsonPath("$.[0].pid").value("12"))
            .andExpect(jsonPath("$.[0].cronJobId")
                .value("123e4567-e89b-12d3-a456-556642440000"))
            .andExpect(jsonPath("$[1].pid").value("15"))
            .andExpect(jsonPath("$[1].cronJobId")
                .value("123e4567-e89b-12d3-a456-556642440002"))
            .andExpect(status().isOk());
    }

    @Test
    public void createCronProcess() throws Exception {
        //given
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        Response response = new Response();
        response.setCronJobId(UUID.fromString("123e4567-e89b-12d3-a456-556642440000"));
        response.setPid("12");
        response.setStartTime(timestamp);

        //when
        Request request = new Request();
        request.setPid("12");
        request.setStartTime(timestamp);
        request.setEndTime(timestamp);

        ObjectMapper mapper = new ObjectMapper();
        String requestJson = mapper.writeValueAsString(request);

        given(cronProcessService.makeCronProcess(response.getCronJobId(), request))
            .willReturn(response);

        //then
        mvc.perform(post("/cron-servers/{serverIp}/cron-jobs/{cronJobId}/process/", "0.0.0.0",
            "123e4567-e89b-12d3-a456-556642440000")
            .contentType(MediaType.APPLICATION_JSON_UTF8)
            .content(requestJson)

        ).andDo(print())
            .andExpect(jsonPath("$.pid").value("12"))
            .andExpect(jsonPath("$.cronJobId")
                .value("123e4567-e89b-12d3-a456-556642440000"))
            .andExpect(status().isOk());
    }

    @Test
    public void getCronProcess() throws Exception {
        //given
        Response response = new Response();
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        response.setCronJobId(UUID.fromString("123e4567-e89b-12d3-a456-556642440000"));
        response.setPid("12");
        response.setStartTime(timestamp);

        //when
        given(cronProcessService.findCronProcess("12")).willReturn(response);

        //then
        mvc.perform(
            get("/cron-servers/{serverIp}/cron-jobs/{cronJobId}/process/{pid}", "0.0.0.0",
                UUID.fromString("123e4567-e89b-12d3-a456-556642440000"), "12"))
            .andDo(print())
            .andExpect(jsonPath("$.pid").value("12"))
            .andExpect(jsonPath("$.cronJobId")
                .value("123e4567-e89b-12d3-a456-556642440000"))
            .andExpect(status().isOk());

    }

    @Test
    public void updateCronProcess() throws Exception {
        //given
        Response response = new Response();
        response.setCronJobId(UUID.fromString("123e4567-e89b-12d3-a456-556642440000"));
        response.setPid("12");

        //when
        Request request = new Request();
        request.setPid("12");

        given(cronProcessService.changeCronProcess("12", request)).willReturn(response);

        ObjectMapper mapper = new ObjectMapper();
        String requestJson = mapper.writeValueAsString(request);

        //then
        mvc.perform(
            patch("/cron-servers/{serverIp}/cron-jobs/{cronJobId}/process/{pid}", "0.0.0.0",
                UUID.fromString("123e4567-e89b-12d3-a456-556642440000"), "12")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(requestJson))
            .andDo(print())
            .andExpect(jsonPath("$.pid").value("12"))
            .andExpect(jsonPath("$.cronJobId")
                .value("123e4567-e89b-12d3-a456-556642440000"))
            .andExpect(status().isOk());
    }

    @Test
    public void findCronLogs() throws Exception {
        //given
        List<CronLogDto.Response> responses = new ArrayList<>();

        CronLogDto.Response response = new CronLogDto.Response();
        response.setCronProcess("1");
        response.setValue("test log");
        response.setStart(Instant.now());
        response.setStop(Instant.now());

        CronLogDto.Response response2 = new CronLogDto.Response();
        response2.setCronProcess("1");
        response2.setValue("test log2");
        response2.setStart(Instant.now());
        response2.setStop(Instant.now());

        responses.add(response);
        responses.add(response2);

        //when
        given(cronProcessService.findCronLogs("1")).willReturn(responses);

        //then
        mvc.perform(
            get("/cron-servers/{serverIp}/cron-jobs/{cronJobId}/process/{pid}/logs", "0.0.0.0",
                UUID.fromString("123e4567-e89b-12d3-a456-556642440000"), "1"))
            .andDo(print())
            .andExpect(jsonPath("$[0].cronProcess").value("1"))
            .andExpect(jsonPath("$[0].value").value("test log"))
            .andExpect(jsonPath("$[1].cronProcess").value("1"))
            .andExpect(jsonPath("$[1].value").value("test log2"))
            .andExpect(status().isOk());
    }

}