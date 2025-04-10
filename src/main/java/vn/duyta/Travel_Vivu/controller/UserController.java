package vn.duyta.Travel_Vivu.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.duyta.Travel_Vivu.dto.request.UserCreationRequest;
import vn.duyta.Travel_Vivu.dto.response.UserCreationResponse;
import vn.duyta.Travel_Vivu.service.UserService;
import vn.duyta.Travel_Vivu.util.annotation.ApiMessage;
import vn.duyta.Travel_Vivu.util.error.IdInvalidException;

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
        UserCreationResponse res = this.userService.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }
}
