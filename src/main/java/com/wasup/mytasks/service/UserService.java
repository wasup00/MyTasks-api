package com.wasup.mytasks.service;

import com.wasup.mytasks.model.ModelUtils;
import com.wasup.mytasks.model.dto.UserDTO;
import com.wasup.mytasks.model.entity.User;
import com.wasup.mytasks.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserDTO createUser(UserDTO userDTO) {
        /*User user = User.builder()
                .name(userDTO.getName())
                .lastName(userDTO.getLastName())
                .username(userDTO.getUsername())
                .password(passwordEncoder.encode(userDTO.getPassword()))
                .build();*/

        User savedUser = userRepository.save(ModelUtils.convertToEntity(userDTO, User.class));
        userDTO.setId(savedUser.getId());
        if (!ModelUtils.validateUser(savedUser, userDTO)) {
            return null;
        }
        userDTO.setPassword(null); // Don't return the password
        return userDTO;
    }

    public UserDTO getUserById(Long id) {
        return userRepository.findById(id)
                .map(user -> ModelUtils.convertToDto(user, UserDTO.class))
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream().map(user -> ModelUtils.convertToDto(user, UserDTO.class)).toList();
    }

    public User findUserEntityById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
    }
}
