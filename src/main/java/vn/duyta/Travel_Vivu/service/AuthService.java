package vn.duyta.Travel_Vivu.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    public LoginResponse login(LoginRequest request){

        //input gồm email và password
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword());

        // xác thực người dùng => cần viết hàm loadUserByUsername
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        String access_token = this.securityUtil.createAccessToken(authentication);
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
        res.setAccessToken(access_token);

        // tạo refresh token
        String refresh_token = this.securityUtil.createRefreshToken(request.getEmail(), res);

        return LoginResponse.builder()
                .accessToken(res.getAccessToken())
                .userLogin(res.getUserLogin())
                .build();
    }
}
