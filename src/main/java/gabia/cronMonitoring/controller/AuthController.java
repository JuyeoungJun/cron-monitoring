package gabia.cronMonitoring.controller;

import gabia.cronMonitoring.dto.request.RefreshTokenDTO;
import gabia.cronMonitoring.dto.request.UserAuthDTO;
import gabia.cronMonitoring.dto.response.AccessTokenDTO;
import gabia.cronMonitoring.dto.response.UserInfoDTO;
import gabia.cronMonitoring.exception.auth.InvalidTokenException;
import gabia.cronMonitoring.service.AuthService;
import gabia.cronMonitoring.service.UserService;
import gabia.cronMonitoring.util.jwt.JwtFilter;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
@Log4j2
public class AuthController {

    private final AuthService authService;

    private final UserService userService;

    @PostMapping("/local/login")
    public ResponseEntity<AccessTokenDTO> login(@Valid @RequestBody UserAuthDTO request) {

        AccessTokenDTO tokenDTO = authService.authenticate(request);
        if (tokenDTO == null) {
            log.error("Do not find access token: {}", request.getAccount());
            throw new InvalidTokenException("엑세스 토큰이 발급되지 않았습니다.");
        }

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + tokenDTO.getToken());

        log.info("Login Success: {}", request.getAccount());

        return new ResponseEntity(tokenDTO, httpHeaders, HttpStatus.OK);
    }

    @PostMapping("/local/logout")
    public ResponseEntity<String> logout(@Valid @RequestBody UserAuthDTO request) {
        authService.unauthenticate(request.getAccount());

        log.info("Logout Success: {}", request.getAccount());

        return new ResponseEntity("Refresh Token Deleted Successfully!", HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<AccessTokenDTO> register(@Valid @RequestBody UserAuthDTO request) {
        UserInfoDTO userInfoDTO = userService.addUser(request);

        log.info("Register Success: {}", request.getAccount());

        return new ResponseEntity(userInfoDTO, HttpStatus.CREATED);
    }

    @PostMapping("/local/reissue")
    public ResponseEntity<AccessTokenDTO> refreshToken(
        @Valid @RequestBody RefreshTokenDTO request) {
        AccessTokenDTO accessTokenDTO = authService
            .reissueAccessToken(request.getRefreshToken());

        log.info("Success make refreshToken: {}", request.getRefreshToken());

        return new ResponseEntity(accessTokenDTO, HttpStatus.CREATED);
    }
}
