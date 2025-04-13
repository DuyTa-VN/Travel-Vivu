package vn.duyta.Travel_Vivu.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import vn.duyta.Travel_Vivu.dto.request.LoginRequest;
import vn.duyta.Travel_Vivu.dto.response.LoginResponse;
import vn.duyta.Travel_Vivu.model.User;
import vn.duyta.Travel_Vivu.util.SecurityUtil;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final SecurityUtil securityUtil;

    @Value("${jwt.refresh-token-validity-in-seconds}")
    private long refreshTokenExpiration;

    public LoginResponse login(LoginRequest request){

        //input gồm email và password
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword());

        // xác thực người dùng => cần viết hàm loadUserByUsername
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        // nếu xác thực thành công thì lưu thông tin vào SecurityContextHolder
        SecurityContextHolder.getContext().setAuthentication(authentication);

        LoginResponse res = new LoginResponse();
        User currentUserDB = this.userService.handleGetUserByUsername(request.getEmail());
        if (currentUserDB != null){
            LoginResponse.UserLogin userLogin = LoginResponse.UserLogin.builder()
                    .id(currentUserDB.getId())
                    .email(currentUserDB.getEmail())
                    .fullName(currentUserDB.getFullName())
                    .build();
            res.setUserLogin(userLogin);
        }
        String access_token = this.securityUtil.createAccessToken(authentication, res.getUserLogin());
        res.setAccessToken(access_token);

        // tạo refresh token
        String refresh_token = this.securityUtil.createRefreshToken(request.getEmail(), res);
        //update refresh token vào Database
        this.userService.updateUserToken(refresh_token, request.getEmail());

        return LoginResponse.builder()
                .accessToken(res.getAccessToken())
                .userLogin(res.getUserLogin())
                .build();
    }

    public LoginResponse getAccount(){
        String email = SecurityUtil.getCurrentUserLogin().isPresent() ? SecurityUtil.getCurrentUserLogin().get() : "";
        User currentUserDB = this.userService.handleGetUserByUsername(email);
        LoginResponse.UserLogin userLogin = new LoginResponse.UserLogin();
        if (currentUserDB != null){
            userLogin.setId(currentUserDB.getId());
            userLogin.setEmail(currentUserDB.getEmail());
            userLogin.setFullName(currentUserDB.getFullName());
        }
        return LoginResponse.builder()
                .userLogin(userLogin)
                .build();
    }
}
