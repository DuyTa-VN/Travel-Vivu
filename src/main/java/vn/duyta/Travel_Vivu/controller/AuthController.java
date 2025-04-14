package vn.duyta.Travel_Vivu.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import vn.duyta.Travel_Vivu.dto.request.LoginRequest;
import vn.duyta.Travel_Vivu.dto.response.LoginResponse;
import vn.duyta.Travel_Vivu.model.User;
import vn.duyta.Travel_Vivu.service.UserService;
import vn.duyta.Travel_Vivu.util.SecurityUtil;
import vn.duyta.Travel_Vivu.util.annotation.ApiMessage;
import vn.duyta.Travel_Vivu.util.error.IdInvalidException;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final SecurityUtil securityUtil;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Value("${jwt.refresh-token-validity-in-seconds}")
    private long refreshTokenExpiration;

    @PostMapping("/login")
    @ApiMessage("Login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        // Nạp INPUT gồm email và password vào SecurityContext
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword());

        // Xác thực người dùng
        Authentication authentication = authenticationManagerBuilder
                .getObject()
                .authenticate(authenticationToken);
        // Lưu thông tin người dùng vào SecurityContext
        SecurityContextHolder.getContext().setAuthentication(authentication);

        LoginResponse res = new LoginResponse();
        User currentUserDB = this.userService.handleGetUserByUsername(request.getEmail());
        if (currentUserDB != null){
            LoginResponse.UserLogin userLogin = new LoginResponse.UserLogin(
                    currentUserDB.getId(),
                    currentUserDB.getEmail(),
                    currentUserDB.getFullName(),
                    currentUserDB.getRole()
            );
            res.setUserLogin(userLogin);
        }

        String access_token = this.securityUtil.createAccessToken(request.getEmail(), res);
        res.setAccessToken(access_token);

        // tạo refresh token
        String refresh_token = this.securityUtil.createRefreshToken(request.getEmail(), res);
        // lưu refresh token vào DB
        this.userService.updateUserToken(refresh_token, request.getEmail());
        // set cookie
        ResponseCookie responseCookie = ResponseCookie
                .from("refresh_token", refresh_token)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(refreshTokenExpiration)
                .build();
        return ResponseEntity
                .ok()
                .header(HttpHeaders.SET_COOKIE, responseCookie.toString())
                .body(res);
    }

    @GetMapping("/account")
    @ApiMessage("Fetch account information")
    public ResponseEntity<LoginResponse.UserGetAccount> getAccount() {
        String email = SecurityUtil.getCurrentUserLogin().isPresent()
                ? SecurityUtil.getCurrentUserLogin().get()
                : "";
        User currentUserDB = this.userService.handleGetUserByUsername(email);
        LoginResponse.UserLogin userLogin = new LoginResponse.UserLogin();
        LoginResponse.UserGetAccount userGetAccount = new LoginResponse.UserGetAccount();

        if (currentUserDB != null) {
            userLogin.setId(currentUserDB.getId());
            userLogin.setEmail(currentUserDB.getEmail());
            userLogin.setFullName(currentUserDB.getFullName());
            userLogin.setRole(currentUserDB.getRole());
            userGetAccount.setUser(userLogin);
        }
        return ResponseEntity.ok().body(userGetAccount);
    }

    @GetMapping("/refresh")
    @ApiMessage("Get User by refresh token")
    public ResponseEntity<LoginResponse> getRefreshToken(
            @CookieValue(name = "refresh_token", defaultValue = "") String refresh_token) throws IdInvalidException {

        Jwt decodedJwt = this.securityUtil.checkValidRefreshToken(refresh_token);
        String email = decodedJwt.getSubject();

        // check user by token + email
        User currentUser = this.userService.getUserByRefreshTokenAndEmail(refresh_token, email);
        if (currentUser == null) {
            throw new IdInvalidException("Refresh token không hợp lệ");
        }

        LoginResponse res = new LoginResponse();
        User currentUserDB = this.userService.handleGetUserByUsername(email);
        if (currentUserDB != null) {
            LoginResponse.UserLogin userLogin = new LoginResponse.UserLogin(
                    currentUserDB.getId(),
                    currentUserDB.getEmail(),
                    currentUserDB.getFullName(),
                    currentUserDB.getRole()
            );
            res.setUserLogin(userLogin);
        }

        String access_token = this.securityUtil.createAccessToken(email, res);
        res.setAccessToken(access_token);

        // tạo refresh token
        String new_refresh_token = this.securityUtil.createRefreshToken(email, res);
        // lưu refresh token vào DB
        this.userService.updateUserToken(new_refresh_token, email);
        // set cookie
        ResponseCookie responseCookie = ResponseCookie
                .from("refresh_token", new_refresh_token)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(refreshTokenExpiration)
                .build();
        return ResponseEntity
                .ok()
                .header(HttpHeaders.SET_COOKIE, responseCookie.toString())
                .body(res);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout() throws IdInvalidException{
        String email = SecurityUtil.getCurrentUserLogin().isPresent()
                ? SecurityUtil.getCurrentUserLogin().get()
                : "";
        if (email.isEmpty()) {
            throw new IdInvalidException("Access token không hợp lệ");
        }

        //update refresh token bằng null
        this.userService.updateUserToken(null, email);

        // xóa refresh token trong cookie
        ResponseCookie deleteSpringCookie = ResponseCookie
                .from("refresh_token", null)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(0)
                .build();
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, deleteSpringCookie.toString()).build();
    }
}
