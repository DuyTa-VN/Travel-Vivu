package vn.duyta.Travel_Vivu.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import vn.duyta.Travel_Vivu.common.Gender;
import vn.duyta.Travel_Vivu.common.Role;
import vn.duyta.Travel_Vivu.model.UserProfile;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class UserResponse {
    private Long id;
    private String fullName;
    private String email;
    private String phoneNumber;
    private Integer age;
    private Role role;
    private Gender gender;
    private UserProfile profile;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
