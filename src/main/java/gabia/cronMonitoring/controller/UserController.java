package gabia.cronMonitoring.controller;

import gabia.cronMonitoring.dto.request.UserAuthDTO;
import gabia.cronMonitoring.dto.response.UserInfoDTO;
import gabia.cronMonitoring.service.UserService;
import java.util.List;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Log4j2
@CrossOrigin(origins = "*")
public class UserController {

    private final UserService userService;

    @GetMapping
    @ResponseBody
    public ResponseEntity<List<UserInfoDTO>> getUsers() {
        List<UserInfoDTO> users = userService.getUsers();
        ResponseEntity responseEntity = new ResponseEntity(users, HttpStatus.OK);

        log.info("Success get user list");

        return responseEntity;
    }

    @GetMapping(value = "/{userId}")
    @ResponseBody
    public ResponseEntity<UserInfoDTO> getUser(
        @NotBlank @PathVariable(name = "userId") String userId) {
        UserAuthDTO request = new UserAuthDTO();
        request.setAccount(userId);

        UserInfoDTO userInfoDTO = userService.getUser(request);

        log.info("Success get user (userId: {})", userId);

        ResponseEntity responseEntity = new ResponseEntity(userInfoDTO, HttpStatus.OK);
        return responseEntity;
    }

    @PatchMapping(value = "/{userId}")
    @ResponseBody
    public ResponseEntity<UserInfoDTO> patchUser(
        @NotBlank @PathVariable(name = "userId") String userId,
        @RequestBody UserAuthDTO request) {
        UserInfoDTO userInfoDTO = userService
            .updateUser(userId, request);
        ResponseEntity responseEntity = new ResponseEntity(userInfoDTO, HttpStatus.OK);

        log.info("Success update user (userId: {})", userId);

        return responseEntity;
    }

    @DeleteMapping(value = "/{userId}")
    @ResponseBody
    public ResponseEntity deleteUser(@NotBlank @PathVariable(name = "userId") String userId) {
        UserAuthDTO request = new UserAuthDTO();
        request.setAccount(userId);
        userService.deleteUser(request);
        ResponseEntity responseEntity = new ResponseEntity(HttpStatus.NO_CONTENT);

        log.info("Success get user (userId: {})", userId);

        return responseEntity;
    }
}
