package com.wasup.mytasks.controller;

import com.wasup.mytasks.api.AuthApi;
import com.wasup.mytasks.model.LoginRequest;
import com.wasup.mytasks.model.LoginResponse;
import com.wasup.mytasks.model.UserRequestDTO;
import com.wasup.mytasks.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequiredArgsConstructor
public class AuthController implements AuthApi {

    private final AuthService authService;

    @Override
    public ResponseEntity<Void> register(UserRequestDTO userRequestDTO) {
        authService.register(userRequestDTO);
        return ResponseEntity.created(URI.create("")).build();
    }

    @Override
    public ResponseEntity<LoginResponse> login(LoginRequest loginRequest) {
        return ResponseEntity.ok(authService.login(loginRequest));
    }
}
