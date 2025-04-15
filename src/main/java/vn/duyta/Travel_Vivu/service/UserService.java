package vn.duyta.Travel_Vivu.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import vn.duyta.Travel_Vivu.common.Role;
import vn.duyta.Travel_Vivu.dto.request.UserCreationRequest;
import vn.duyta.Travel_Vivu.dto.request.UserUpdateRequest;
import vn.duyta.Travel_Vivu.dto.response.UserCreationResponse;
import vn.duyta.Travel_Vivu.dto.response.UserUpdateResponse;
import vn.duyta.Travel_Vivu.model.User;
import vn.duyta.Travel_Vivu.repository.UserRepository;
import vn.duyta.Travel_Vivu.util.error.IdInvalidException;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // tạo người dùng
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
                .build();
    }

    //Update user
    public UserUpdateResponse updateUser(Long id, UserUpdateRequest request) throws IdInvalidException{
        log.info("User update");
        User currentUser = this.userRepository.findById(id)
                .orElseThrow(() -> new IdInvalidException("User not found with id: " + id));
        if (currentUser != null){
            currentUser.setFullName(request.getFullName());
            currentUser.setPhoneNumber(request.getPhoneNumber());
            currentUser.setAge(request.getAge());
            currentUser.setGender(request.getGender());
            if (currentUser.getRole().equals(Role.ADMIN) && request.getRole() != null){
                currentUser.setRole(request.getRole());
            }

            currentUser = this.userRepository.save(currentUser);
        }
        return UserUpdateResponse.builder()
                .id(currentUser.getId())
                .fullName(currentUser.getFullName())
                .email(currentUser.getEmail())
                .phoneNumber(currentUser.getPhoneNumber())
                .age(currentUser.getAge())
                .role(currentUser.getRole())
                .gender(currentUser.getGender())
                .updatedAt(currentUser.getUpdatedAt())
                .build();
    }

    public User fetchUserById(Long id){
        Optional<User> userOptional = this.userRepository.findById(id);
        if (userOptional.isPresent()){
            return userOptional.get();
        } else {
            log.error("User not found with id: {}", id);
            return null;
        }
    }

    public void handleDeleteUser(Long id) throws IdInvalidException {
        User currentUser = this.userRepository.findById(id)
                .orElseThrow(() -> new IdInvalidException("User với id = " + id + " không tồn tại"));
        this.userRepository.delete(currentUser);
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

    public User getUserByRefreshTokenAndEmail(String token, String email) {
        return userRepository.findByRefreshTokenAndEmail(token, email);
    }

}
