package com.example.Job_Hunter.controller;

import com.example.Job_Hunter.domain.Entity.User;
import com.example.Job_Hunter.domain.request.ReqLoginDTO;
import com.example.Job_Hunter.domain.response.ResLoginDTO;
import com.example.Job_Hunter.service.UserService;
import com.example.Job_Hunter.utill.SecurityUtil;
import com.example.Job_Hunter.utill.exception.IdInvalidException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class AuthController {
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final SecurityUtil securityUtil;
    private final UserService userService;

    public AuthController(AuthenticationManagerBuilder authenticationManagerBuilder, SecurityUtil securityUtil, UserService userService) {
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.securityUtil = securityUtil;
        this.userService = userService;
    }

    @Value("${jwt.refresh_token-validity-in-seconds}")
    private long refreshTokenJwtKeyExpiration;

    @PostMapping("auth/login")
    public ResponseEntity<ResLoginDTO> login(@Valid @RequestBody ReqLoginDTO reqLoginDTO) {
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                reqLoginDTO.getUsername(),
                reqLoginDTO.getPassword());

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(auth);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        ResLoginDTO resLoginDTO = new ResLoginDTO();

        User user = this.userService.getUserByUsername(reqLoginDTO.getUsername());
        ResLoginDTO.UserLogin userLogin = new ResLoginDTO.UserLogin();
        userLogin.setId(user.getId());
        userLogin.setName(user.getUsername());
        userLogin.setEmail(user.getEmail());

        userLogin.setRoles(user.getRole());
        resLoginDTO.setUserLogin(userLogin);

        String access_token = securityUtil.createAccessToken(authentication.getName(), resLoginDTO.getUserLogin());

        resLoginDTO.setAccess_token(access_token);

        String refreshToken = securityUtil.createRefreshToken(reqLoginDTO.getUsername(), resLoginDTO);
        userService.updateUserToken(refreshToken, reqLoginDTO.getUsername());

        ResponseCookie responseCookie = ResponseCookie.from("RefreshToken", refreshToken)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(refreshTokenJwtKeyExpiration)
                .build();
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, responseCookie.toString())
                .body(resLoginDTO);
    }

    @GetMapping("/auth/account")
    public ResponseEntity<ResLoginDTO.UserLogin> account() {
        String email = SecurityUtil.getCurrentUserLogin().isPresent() ? SecurityUtil.getCurrentUserLogin().get() : "";
        User currentUserDB = userService.getUserByUsername(email);
        ResLoginDTO.UserLogin userLogin = new ResLoginDTO.UserLogin();
        if (currentUserDB != null) {
            userLogin.setId(currentUserDB.getId());
            userLogin.setEmail(currentUserDB.getEmail());
            userLogin.setName(currentUserDB.getUsername());
        }
        return ResponseEntity.ok().body(userLogin);
    }

    @GetMapping("/auth/refresh")
    public ResponseEntity<ResLoginDTO> refreshToken(@CookieValue(name = "RefreshToken") String refreshToken) throws IdInvalidException {
        Jwt decodedToken = securityUtil.checkValidRefreshToke(refreshToken);
        String email = decodedToken.getSubject();
        User currentUser = userService.getUserRefreshTokenAndEmail(refreshToken, email);
        if (currentUser == null) {
            throw new IdInvalidException("Refresh token expired");
        }
        ResLoginDTO resLoginDTO = new ResLoginDTO();
        User user = this.userService.getUserByUsername(email);
        ResLoginDTO.UserLogin userLogin = new ResLoginDTO.UserLogin(user.getId(), user.getEmail(), user.getUsername(), user.getRole());
        resLoginDTO.setUserLogin(userLogin);

        String access_token = securityUtil.createAccessToken(email, resLoginDTO.getUserLogin());
        resLoginDTO.setAccess_token(access_token);


        //createRefreshToken new_refreshToken
        String new_refreshToken = securityUtil.createRefreshToken(email, resLoginDTO);
        userService.updateUserToken(new_refreshToken, email);

        ResponseCookie responseCookie = ResponseCookie.from("RefreshToken", new_refreshToken)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(refreshTokenJwtKeyExpiration)
                .build();
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, responseCookie.toString())
                .body(resLoginDTO);
    }
    @PostMapping("/auth/logout")
    public ResponseEntity<Void> logout() throws IdInvalidException {
        String email = SecurityUtil.getCurrentUserLogin().isPresent() ? SecurityUtil.getCurrentUserLogin().get() : "";
        if (email.equals("")) {
            throw new IdInvalidException("Access token khong hop le");
        }
        userService.updateUserToken(null, email);

        ResponseCookie responseCookie = ResponseCookie.from("RefreshToken",null)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(0)
                .build();
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, responseCookie.toString())
                .build();
    }
}