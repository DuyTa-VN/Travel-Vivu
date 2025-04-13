package vn.duyta.Travel_Vivu.controller;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.duyta.Travel_Vivu.dto.request.LoginRequest;
import vn.duyta.Travel_Vivu.dto.response.LoginResponse;
import vn.duyta.Travel_Vivu.service.AuthService;
import vn.duyta.Travel_Vivu.util.SecurityUtil;
import vn.duyta.Travel_Vivu.util.annotation.ApiMessage;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;
    private final SecurityUtil securityUtil;

    @Value("${jwt.refresh-token-validity-in-seconds}")
    private long refreshTokenExpiration;

    @PostMapping("/login")
    @ApiMessage("Login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request,
                                               HttpServletResponse response) {
        LoginResponse res = this.authService.login(request);
        String refreshToken = this.securityUtil.createRefreshToken(request.getEmail(), res);
        // Tạo cookie với tên là refresh_token và giá trị là refresh_token
        ResponseCookie responseCookie = ResponseCookie
                .from("refresh_token", refreshToken)
                .httpOnly(true)
                .path("/")
                .secure(true) // true nếu chỉ cho gửi qua https
                .maxAge(refreshTokenExpiration) // thời gian sống của cookie
                .build();

        return ResponseEntity
                .ok()
                .header(HttpHeaders.SET_COOKIE, responseCookie.toString())
                .body(res);
    }

    @GetMapping("/account")
    @ApiMessage("Fetch account information")
    public ResponseEntity<LoginResponse.UserLogin> getAccount() {
        LoginResponse response = this.authService.getAccount();
        return ResponseEntity.ok().body(response.getUserLogin());
    }
}
