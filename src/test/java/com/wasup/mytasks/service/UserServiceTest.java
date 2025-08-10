package com.wasup.mytasks.service;

import com.wasup.mytasks.exception.ResourceNotFoundException;
import com.wasup.mytasks.model.UserRequestDTO;
import com.wasup.mytasks.model.UserResponseDTO;
import com.wasup.mytasks.model.entity.User;
import com.wasup.mytasks.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private UserRequestDTO userRequestDTO;
    private User user;
    private UserResponseDTO userResponseDTO;

    @BeforeEach
    void setUp() {
        userRequestDTO = new UserRequestDTO()
                .name("John")
                .lastName("Doe")
                .username("johndoe")
                .password("password123");

        user = User.builder()
                .id(1L)
                .name("John")
                .lastName("Doe")
                .username("johndoe")
                .password("encoded_password")
                .build();

        userResponseDTO = new UserResponseDTO()
                .id(1L)
                .name("John")
                .lastName("Doe")
                .username("johndoe");
    }

    @Test
    void createUser_Success() {
        when(userRepository.save(any())).thenReturn(user);

        UserResponseDTO result = userService.createUser(userRequestDTO);

        assertThat(result).isNotNull().isEqualTo(userResponseDTO);
        verify(userRepository, times(1)).save(any());
    }

    @Test
    void getUser_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        UserResponseDTO result = userService.getUser(1L);

        assertThat(result).isNotNull().isEqualTo(userResponseDTO);
    }

    @Test
    void getUser_NotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.getUser(1L));
    }

    @Test
    void getAllUsers_Success() {
        when(userRepository.findAll()).thenReturn(Collections.singletonList(user));

        List<UserResponseDTO> results = userService.getAllUsers();

        assertThat(results).hasSize(1).first().isEqualTo(userResponseDTO);
    }

    @Test
    void deleteUser_Success() {
        when(userRepository.existsById(1L)).thenReturn(true);

        userService.deleteUser(1L);

        verify(userRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteUser_NotFound() {
        when(userRepository.existsById(1L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> userService.deleteUser(1L));
    }
}
