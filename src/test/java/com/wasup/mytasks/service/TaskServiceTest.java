package com.wasup.mytasks.service;

import com.wasup.mytasks.exception.ResourceNotFoundException;
import com.wasup.mytasks.model.TaskDTO;
import com.wasup.mytasks.model.entity.Task;
import com.wasup.mytasks.model.entity.User;
import com.wasup.mytasks.repository.TaskRepository;
import com.wasup.mytasks.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private TaskService taskService;

    private TaskDTO taskDTO;
    private Task task;
    private User user;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1L)
                .name("John")
                .lastName("Doe")
                .username("johndoe")
                .build();

        OffsetDateTime now = OffsetDateTime.now();

        taskDTO = new TaskDTO()
                .id(1L)
                .title("Test Task")
                .description("Test Description")
                .completed(false)
                .date(now)
                .userId(1L);

        task = Task.builder()
                .id(1L)
                .title("Test Task")
                .description("Test Description")
                .completed(false)
                .date(now)
                .user(user)
                .build();
    }

    @Test
    void createTask_Success() {
        when(userRepository.existsById(1L)).thenReturn(true);
        when(taskRepository.save(any())).thenReturn(task);

        TaskDTO result = taskService.createTask(taskDTO);

        assertThat(result).isNotNull().isEqualTo(taskDTO);
        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    void createTask_UserNotFound() {
        when(userRepository.existsById(1L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> taskService.createTask(taskDTO));
    }

    @Test
    void getTaskById_Success() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        TaskDTO result = taskService.getTaskById(1L);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getTitle()).isEqualTo("Test Task");
    }

    @Test
    void getTaskById_NotFound() {
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> taskService.getTaskById(1L));
    }

    @Test
    void deleteTask_Success() {
        when(taskRepository.existsById(1L)).thenReturn(true);

        taskService.deleteTask(1L);

        verify(taskRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteTask_NotFound() {
        when(taskRepository.existsById(1L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> taskService.deleteTask(1L));
    }
}
