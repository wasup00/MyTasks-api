package com.wasup.mytasks.service;

import com.wasup.mytasks.exception.ResourceNotFoundException;
import com.wasup.mytasks.exception.ValidationException;
import com.wasup.mytasks.model.ModelUtils;
import com.wasup.mytasks.model.TaskDTO;
import com.wasup.mytasks.model.entity.Task;
import com.wasup.mytasks.repository.TaskRepository;
import com.wasup.mytasks.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public TaskDTO createTask(TaskDTO taskDTO) {
        if (!userRepository.existsById(taskDTO.getUserId())) {
            log.atError().addArgument(taskDTO::getUserId).log("No user with userId -> {} found");
            throw new ResourceNotFoundException("User not found");
        }
        Task savedTask = taskRepository.save(ModelUtils.convertToEntity(taskDTO, Task.class));
        taskDTO.setId(savedTask.getId());
        if (!ModelUtils.validateTask(savedTask, taskDTO)) {
            log.atError()
                    .addArgument(taskDTO::toString)
                    .addArgument(savedTask::toString)
                    .log("Problem when validating task saved and requested\ntaskDTO:\n\t{}\nsavedtask:\n\t{}");
            throw new ValidationException("Task validation failed");
        }
        log.atInfo()
                .addArgument(taskDTO::getId)
                .addArgument(taskDTO::getUserId)
                .log("Task {} created successfully for user {}");
        return taskDTO;
    }

    public List<TaskDTO> getAllTasks() {
        return taskRepository.findAll()
                .stream()
                .map(task -> ModelUtils.convertToDto(task, TaskDTO.class))
                .toList();
    }

    public TaskDTO getTaskById(Long id) {
        return taskRepository.findById(id)
                .map(task -> ModelUtils.convertToDto(task, TaskDTO.class))
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));
    }

    public void deleteTask(Long id) {
        if (!taskRepository.existsById(id)) {
            log.atError().addArgument(id).log("No task with id -> {} found");
            throw new ResourceNotFoundException("Task not found");
        }
        taskRepository.deleteById(id);
        log.atInfo().addArgument(id).log("Task {} deleted successfully");
    }
}
