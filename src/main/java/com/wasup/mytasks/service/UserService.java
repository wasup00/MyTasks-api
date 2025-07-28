package com.wasup.mytasks.service;

import com.wasup.mytasks.model.ModelUtils;
import com.wasup.mytasks.model.dto.UserRequestDTO;
import com.wasup.mytasks.model.dto.UserResponseDTO;
import com.wasup.mytasks.model.entity.User;
import com.wasup.mytasks.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;

    public ResponseEntity<UserResponseDTO> createUser(UserRequestDTO userRequestDTO) {
        User savedUser = userRepository.save(ModelUtils.convertToEntity(userRequestDTO, User.class));
        UserResponseDTO userResponseDTO = ModelUtils.convertToDto(savedUser, UserResponseDTO.class);
        if (!ModelUtils.validateUser(savedUser, userResponseDTO)) {
            log.atError()
                    .addArgument(savedUser::toString)
                    .addArgument(userResponseDTO::toString)
                    .log("Problem when validating user saved and created\nsavedUser:\n\t{}\nuserResponseDTO:\n\t{}");
            return ResponseEntity.internalServerError().build();
        }
        log.atInfo().addArgument(savedUser::getId).log("User {} created successfully");
        return ResponseEntity.created(URI.create("")).body(userResponseDTO);
    }

    public ResponseEntity<UserResponseDTO> getUserById(Long id) {
        return userRepository.findById(id)
                .map(user -> ResponseEntity.ok(ModelUtils.convertToDto(user, UserResponseDTO.class)))
                .orElse(ResponseEntity.notFound().build());
    }

    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        return ResponseEntity.ok(userRepository.findAll()
                .stream()
                .map(user -> ModelUtils.convertToDto(user, UserResponseDTO.class))
                .toList());
    }

    public Optional<User> findUserById(Long id) {
        return userRepository.findById(id);
    }

    public boolean existsById(Long id) {
        return userRepository.existsById(id);
    }

}
