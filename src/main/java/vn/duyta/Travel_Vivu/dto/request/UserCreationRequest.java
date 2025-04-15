package vn.duyta.Travel_Vivu.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import vn.duyta.Travel_Vivu.common.Gender;
import vn.duyta.Travel_Vivu.common.Role;

@Getter
@Setter
@Builder
public class UserCreationRequest {

    @NotBlank(message = "FullName không được để trống")
    private String fullName;

    @NotBlank(message = "Email không được để trống")
    private String email;

    @NotBlank(message = "Password không được để trống")
    private String password;

    @NotBlank(message = "PhoneNumber không được để trống")
    private String phoneNumber;

    @NotNull(message = "Age không được để trống")
    private Integer age;

    @NotNull(message = "Role không được để trống")
    private Role role;

    @NotNull(message = "Gender không được để trống")
    private Gender gender;
}
