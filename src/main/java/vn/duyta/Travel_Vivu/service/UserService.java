package vn.duyta.Travel_Vivu.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import vn.duyta.Travel_Vivu.common.Role;
import vn.duyta.Travel_Vivu.dto.request.UserCreationRequest;
import vn.duyta.Travel_Vivu.dto.response.UserCreationResponse;
import vn.duyta.Travel_Vivu.model.User;
import vn.duyta.Travel_Vivu.repository.UserRepository;
import vn.duyta.Travel_Vivu.util.error.IdInvalidException;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserCreationResponse createUser(UserCreationRequest request) throws IdInvalidException {
        log.info("User creation");
        if (userRepository.existsByEmail(request.getEmail())){
            log.error("Email already exists {}", request.getEmail());
            throw new IdInvalidException("Email " + request.getEmail() + " đã tồn tại, vui lòng sử dụng email khác");
        }
        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .fullName(request.getFullName())
                .phoneNumber(request.getPhoneNumber())
                .age(request.getAge())
                .role(request.getRole() != null ? request.getRole() : Role.CUSTOMER)
                .gender(request.getGender())
                .build();

        User currentUser = userRepository.save(user);
        return UserCreationResponse.builder()
                .id(currentUser.getId())
                .fullName(currentUser.getFullName())
                .email(currentUser.getEmail())
                .phoneNumber(currentUser.getPhoneNumber())
                .age(currentUser.getAge())
                .role(currentUser.getRole())
                .gender(currentUser.getGender())
                .createdAt(currentUser.getCreatedAt())
                .updatedAt(currentUser.getUpdatedAt())
                .build();
    }

    public User handleGetUserByUsername(String email) {
        return userRepository.findByEmail(email);
    }

    public void updateUserToken(String token, String email){
        User currentUser = this.handleGetUserByUsername(email);
        if (currentUser != null){
            currentUser.setRefreshToken(token);
            this.userRepository.save(currentUser);
        } else {
            log.error("User not found with email: {}", email);
        }
    }


}
