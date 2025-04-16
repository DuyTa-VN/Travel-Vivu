package vn.duyta.Travel_Vivu.dto.request;

import lombok.*;
import vn.duyta.Travel_Vivu.common.Gender;
import vn.duyta.Travel_Vivu.common.Role;
import vn.duyta.Travel_Vivu.model.UserProfile;

@Getter
@Setter
@Builder
public class UserUpdateRequest {
    private String fullName;
    private String phoneNumber;
    private Integer age;
    private Role role;
    private Gender gender;
    private UserProfile profile;
}
