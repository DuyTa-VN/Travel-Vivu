package vn.duyta.Travel_Vivu.service;

import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import vn.duyta.Travel_Vivu.common.Role;
import vn.duyta.Travel_Vivu.dto.request.UserCreationRequest;
import vn.duyta.Travel_Vivu.dto.request.UserUpdateRequest;
import vn.duyta.Travel_Vivu.dto.response.UserCreationResponse;
import vn.duyta.Travel_Vivu.dto.response.UserResponse;
import vn.duyta.Travel_Vivu.dto.response.UserUpdateResponse;
import vn.duyta.Travel_Vivu.model.User;
import vn.duyta.Travel_Vivu.model.UserProfile;
import vn.duyta.Travel_Vivu.repository.UserRepository;
import vn.duyta.Travel_Vivu.util.error.IdInvalidException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    // tạo người dùng
    public UserCreationResponse createUser(UserCreationRequest request) throws IdInvalidException {
        log.info("User creation");
        if (userRepository.existsByEmail(request.getEmail())) {
            log.error("Email already exists {}", request.getEmail());
            throw new IdInvalidException("Email " + request.getEmail() + " đã tồn tại, vui lòng sử dụng email khác");
        }
        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .fullName(request.getFullName())
                .phoneNumber(request.getPhoneNumber())
                .age(request.getAge())
                .role(Role.CUSTOMER)
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
                .build();
    }

    //Update user
    public UserUpdateResponse updateUser(Long id, UserUpdateRequest request) throws IdInvalidException {
        log.info("User update");
        User currentUser = this.userRepository.findById(id)
                .orElseThrow(() -> new IdInvalidException("User không được tìm thấy với id: " + id));

        currentUser.setFullName(request.getFullName());
        currentUser.setPhoneNumber(request.getPhoneNumber());
        currentUser.setAge(request.getAge());
        currentUser.setGender(request.getGender());
        if (request.getRole() != null) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            boolean isAdmin = authentication.getAuthorities().stream()
                    .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));
            if (isAdmin) {
                currentUser.setRole(request.getRole());
            } else {
                throw new IdInvalidException("Bạn không có quyền thay đổi role của người dùng");
            }
        }
        currentUser = this.userRepository.save(currentUser);

        UserProfile userProfile = currentUser.getProfile();
        if (userProfile == null) {
            userProfile = new UserProfile();
            userProfile.setUser(currentUser);
        }
        if (request.getProfile() != null){
            userProfile.setAvatarUrl(request.getProfile().getAvatarUrl());
            userProfile.setDateOfBirth(request.getProfile().getDateOfBirth());
            userProfile.setAddress(request.getProfile().getAddress());
        }
        currentUser.setProfile(userProfile);

        // save user và userProfile
        this.userRepository.save(currentUser);

        return UserUpdateResponse.builder()
                .id(currentUser.getId())
                .fullName(currentUser.getFullName())
                .email(currentUser.getEmail())
                .phoneNumber(currentUser.getPhoneNumber())
                .age(currentUser.getAge())
                .role(currentUser.getRole())
                .gender(currentUser.getGender())
                .profile(currentUser.getProfile())
                .updatedAt(currentUser.getUpdatedAt())
                .build();
    }

    // Lấy thông tin tất cả người dùng
    public List<UserResponse> getAllUsers(){
        List<User> users = this.userRepository.findAll();
        return users.stream().map(user -> UserResponse.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .age(user.getAge())
                .role(user.getRole())
                .gender(user.getGender())
                .profile(user.getProfile())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build()).collect(Collectors.toList());
    }

    // Lấy thông tin người dùng theo ID
    public UserResponse fetchUserById(Long id) throws IdInvalidException {
        User user = this.userRepository.findById(id)
                .orElseThrow(() -> new IdInvalidException("User với id = " + id + " không tồn tại"));
        return UserResponse.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .age(user.getAge())
                .role(user.getRole())
                .gender(user.getGender())
                .profile(user.getProfile())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }

    // Xóa người dùng
    public void handleDeleteUser(Long id) throws IdInvalidException {
        User currentUser = this.userRepository.findById(id)
                .orElseThrow(() -> new IdInvalidException("User với id = " + id + " không tồn tại"));
        this.userRepository.delete(currentUser);
    }

    public User handleGetUserByUsername(String email) {
        return userRepository.findByEmail(email);
    }

    public void updateUserToken(String token, String email) {
        User currentUser = this.handleGetUserByUsername(email);
        if (currentUser != null) {
            currentUser.setRefreshToken(token);
            this.userRepository.save(currentUser);
        } else {
            log.error("User not found with email: {}", email);
        }
    }

    public User getUserByRefreshTokenAndEmail(String token, String email) {
        return userRepository.findByRefreshTokenAndEmail(token, email);
    }

    public boolean checkEmailExist(String email) {
        return this.userRepository.existsByEmail(email);
    }
}
