package vn.duyta.Travel_Vivu.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginResponse {
    private String accessToken;
    private UserLogin userLogin;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class UserLogin{
        private Long id;
        private String email;
        private String fullName;
    }

    public static class UserGetAccount{

    }
}
