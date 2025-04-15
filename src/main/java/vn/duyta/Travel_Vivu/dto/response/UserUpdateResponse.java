package vn.duyta.Travel_Vivu.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import vn.duyta.Travel_Vivu.common.Gender;
import vn.duyta.Travel_Vivu.common.Role;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class UserUpdateResponse {
    private Long id;
    private String fullName;
    private String email;
    private String phoneNumber;
    private Integer age;
    private Role role;
    private Gender gender;
    private LocalDateTime updatedAt;
}
