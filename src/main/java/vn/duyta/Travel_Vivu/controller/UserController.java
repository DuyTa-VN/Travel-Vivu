package vn.duyta.Travel_Vivu.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import vn.duyta.Travel_Vivu.dto.request.UserCreationRequest;
import vn.duyta.Travel_Vivu.dto.request.UserUpdateRequest;
import vn.duyta.Travel_Vivu.dto.response.UserCreationResponse;
import vn.duyta.Travel_Vivu.dto.response.UserResponse;
import vn.duyta.Travel_Vivu.dto.response.UserUpdateResponse;
import vn.duyta.Travel_Vivu.model.User;
import vn.duyta.Travel_Vivu.service.UserService;
import vn.duyta.Travel_Vivu.util.annotation.ApiMessage;
import vn.duyta.Travel_Vivu.util.error.IdInvalidException;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @PostMapping
    @ApiMessage("Create a user")
    public ResponseEntity<UserCreationResponse> createUser(@Valid @RequestBody UserCreationRequest request) throws IdInvalidException {
        log.info("Create user with email: {}", request.getEmail());
        UserCreationResponse res = this.userService.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    @PutMapping("/{id}")
    @ApiMessage("Update a user")
    public ResponseEntity<UserUpdateResponse> updateUser(@Valid @PathVariable("id") Long id,
                                                         @RequestBody UserUpdateRequest request) throws IdInvalidException {
        log.info("Update user with id: {}", id);
        UserUpdateResponse res = this.userService.updateUser(id, request);
        return ResponseEntity.ok().body(res);
    }

    @GetMapping
    @ApiMessage("Get all users")
    public ResponseEntity<List<UserResponse>> fetchAllUsers() {
        log.info("Fetch all users");
        List<UserResponse> res = this.userService.getAllUsers();
        return ResponseEntity.ok().body(res);
    }

    @GetMapping("/{id}")
    @ApiMessage("Get user by id")
    public ResponseEntity<UserResponse> getUserById(@PathVariable("id") Long id) throws IdInvalidException {
        log.info("Fetch user with id: {}", id);
        UserResponse res = this.userService.fetchUserById(id);
        return ResponseEntity.ok().body(res);
    }

    @DeleteMapping("/{id}")
    @ApiMessage("Delete a user")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") Long id) throws IdInvalidException {
        log.info("Delete user with id: {}", id);
        this.userService.handleDeleteUser(id);
        return ResponseEntity.ok(null);
    }
}
