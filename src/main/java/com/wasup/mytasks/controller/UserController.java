package com.wasup.mytasks.controller;

import com.wasup.mytasks.api.UserApi;
import com.wasup.mytasks.model.UserRequestDTO;
import com.wasup.mytasks.model.UserResponseDTO;
import com.wasup.mytasks.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserController implements UserApi {
    private final UserService userService;

    @Override
    public ResponseEntity<UserResponseDTO> createUser(UserRequestDTO userRequestDTO) {
        UserResponseDTO userResponseDTO = userService.createUser(userRequestDTO);
        return ResponseEntity.created(URI.create("")).body(userResponseDTO);
    }

    @Override
    public ResponseEntity<Void> deleteUser(Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        List<UserResponseDTO> userResponseDTOList = userService.getAllUsers();
        return ResponseEntity.ok(userResponseDTOList);
    }

    @Override
    public ResponseEntity<UserResponseDTO> getUser(Long id) {
        UserResponseDTO userResponseDTO = userService.getUser(id);
        return ResponseEntity.ok(userResponseDTO);
    }
}
