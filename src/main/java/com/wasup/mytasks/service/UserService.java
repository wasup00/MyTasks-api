package com.wasup.mytasks.service;

import com.wasup.mytasks.exception.ResourceNotFoundException;
import com.wasup.mytasks.exception.ValidationException;
import com.wasup.mytasks.model.ModelUtils;
import com.wasup.mytasks.model.UserRequestDTO;
import com.wasup.mytasks.model.UserResponseDTO;
import com.wasup.mytasks.model.UserUpdateDTO;
import com.wasup.mytasks.model.entity.User;
import com.wasup.mytasks.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;

    public UserResponseDTO createUser(UserRequestDTO userRequestDTO) {
        User savedUser = userRepository.save(ModelUtils.convertToEntity(userRequestDTO, User.class));
        UserResponseDTO userResponseDTO = ModelUtils.convertToDto(savedUser, UserResponseDTO.class);

        if (!ModelUtils.validateUser(savedUser, userResponseDTO)) {
            log.atError()
                    .addArgument(savedUser::toString)
                    .addArgument(userResponseDTO::toString)
                    .log("Problem when validating user saved and created\nsavedUser:\n\t{}\nuserResponseDTO:\n\t{}");
            throw new ValidationException("User validation failed");

        }
        log.atInfo().addArgument(savedUser::getUsername).log("User {} created successfully");
        return userResponseDTO;
    }

    public UserResponseDTO getUser(Long id) {
        return userRepository.findById(id)
                .map(user -> ModelUtils.convertToDto(user, UserResponseDTO.class))
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    public List<UserResponseDTO> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(user -> ModelUtils.convertToDto(user, UserResponseDTO.class))
                .toList();
    }

    public Optional<User> findUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public boolean userExistsById(Long id) {
        return userRepository.existsById(id);
    }

    public void deleteUser(Long id) {
        if (!userExistsById(id)) {
            log.atError().addArgument(id).log("No user with id -> {} found");
            throw new ResourceNotFoundException("User not found");
        }
        userRepository.deleteById(id);
        log.atInfo().addArgument(id).log("User {} deleted successfully");
    }

    public UserResponseDTO updateUser(Long id, UserRequestDTO userRequestDTO) {
        if (!userExistsById(id)) {
            log.atError().addArgument(id).log("No user with id -> {} found");
            throw new ResourceNotFoundException("User not found");
        }
        User user = ModelUtils.convertToEntity(userRequestDTO, User.class);
        user.setId(id);
        User savedUser = userRepository.save(user);
        return ModelUtils.convertToDto(savedUser, UserResponseDTO.class);
    }

    public UserResponseDTO patchUser(Long id, UserUpdateDTO userUpdateDTO) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (userUpdateDTO.getName() != null) {
            user.setName(userUpdateDTO.getName());
        }
        if (userUpdateDTO.getLastName() != null) {
            user.setLastName(userUpdateDTO.getLastName());
        }
        if (userUpdateDTO.getUsername() != null) {
            user.setUsername(userUpdateDTO.getUsername());
        }
        if (userUpdateDTO.getPassword() != null) {
            user.setPassword(userUpdateDTO.getPassword());
        }

        User savedPatchedUser = userRepository.save(user);
        return ModelUtils.convertToDto(savedPatchedUser, UserResponseDTO.class);
    }
}
