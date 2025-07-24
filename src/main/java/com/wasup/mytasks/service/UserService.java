package com.wasup.mytasks.service;

import com.wasup.mytasks.model.ModelUtils;
import com.wasup.mytasks.model.dto.UserRequestDTO;
import com.wasup.mytasks.model.dto.UserResponseDTO;
import com.wasup.mytasks.model.entity.User;
import com.wasup.mytasks.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserResponseDTO createUser(UserRequestDTO userRequestDTO) {
        User savedUser = userRepository.save(ModelUtils.convertToEntity(userRequestDTO, User.class));
        UserResponseDTO userResponseDTO = ModelUtils.convertToDto(savedUser, UserResponseDTO.class);
        if (!ModelUtils.validateUser(savedUser, userResponseDTO)) {
            return null;
        }
        return userResponseDTO;
    }

    public UserResponseDTO getUserById(Long id) {
        return userRepository.findById(id)
                .map(user -> ModelUtils.convertToDto(user, UserResponseDTO.class))
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public List<UserResponseDTO> getAllUsers() {
        return userRepository.findAll().stream().map(user -> ModelUtils.convertToDto(user, UserResponseDTO.class)).toList();
    }

    public User findUserEntityById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
    }
}
