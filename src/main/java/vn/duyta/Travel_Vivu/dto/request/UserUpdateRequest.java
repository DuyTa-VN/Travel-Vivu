package vn.duyta.Travel_Vivu.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import vn.duyta.Travel_Vivu.common.Gender;
import vn.duyta.Travel_Vivu.common.Role;

@Getter
@Setter
@Builder
public class UserUpdateRequest {
    private String fullName;
    private String phoneNumber;
    private Integer age;
    private Role role;
    private Gender gender;
}
