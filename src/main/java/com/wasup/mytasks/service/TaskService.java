package com.wasup.mytasks.service;

import com.wasup.mytasks.exception.ResourceNotFoundException;
import com.wasup.mytasks.exception.ValidationException;
import com.wasup.mytasks.model.ModelUtils;
import com.wasup.mytasks.model.TaskDTO;
import com.wasup.mytasks.model.TaskUpdateDTO;
import com.wasup.mytasks.model.entity.Task;
import com.wasup.mytasks.model.entity.User;
import com.wasup.mytasks.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TaskService {

    private final TaskRepository taskRepository;
    private static final String USER_NOT_FOUND_MSG = "User not found";
    private final UserService userService;

    private static TaskDTO toTaskDTO(Task task) {
        return ModelUtils.convertToDto(task, TaskDTO.class);
    }

    public TaskDTO createTask(TaskDTO taskDTO) {
        if (!userService.userExistsById(taskDTO.getUserId())) {
            log.atError().addArgument(taskDTO::getUserId).log("No user with userId -> {} found");
            throw new ResourceNotFoundException(USER_NOT_FOUND_MSG);
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
                .map(TaskService::toTaskDTO)
                .toList();
    }

    public List<TaskDTO> getTasksByUser(Long userId, String username) {
        Long resolvedUserId = resolveUserId(userId, username);
        return taskRepository.findByUser_IdOrderByDateDesc(resolvedUserId)
                .stream()
                .map(TaskService::toTaskDTO)
                .toList();
    }

    public void deleteTask(Long id) {
        if (!taskRepository.existsById(id)) {
            log.atError().addArgument(id).log("No task with id -> {} found");
            throw new ResourceNotFoundException("Task not found");
        }
        taskRepository.deleteById(id);
        log.atInfo().addArgument(id).log("Task {} deleted successfully");
    }

    public TaskDTO getTaskById(Long id) {
        return taskRepository.findById(id)
                .map(TaskService::toTaskDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));
    }

    private Long resolveUserId(Long userId, String username) {
        if (userId == null && username == null) {
            throw new IllegalArgumentException("Either userId or username must be provided");
        }
        if (userId != null) {
            if (!userService.userExistsById(userId)) {
                log.atError().addArgument(userId).log("No user with userId -> {} found");
                throw new ResourceNotFoundException(USER_NOT_FOUND_MSG);
            }
            return userId;
        }
        return userService.findUserByUsername(username)
                .map(User::getId)
                .orElseThrow(() -> new ResourceNotFoundException(USER_NOT_FOUND_MSG));
    }


    public TaskDTO updateTask(Long id, TaskDTO taskDTO) {
        if (!taskRepository.existsById(id)) {
            log.atError().addArgument(id).log("No task with id -> {} found");
            throw new ResourceNotFoundException("Task not found");
        }
        Task task = ModelUtils.convertToEntity(taskDTO, Task.class);
        task.setId(id);
        Task savedTask = taskRepository.save(task);
        return ModelUtils.convertToDto(savedTask, TaskDTO.class);
    }

    public TaskDTO patchUser(Long id, TaskUpdateDTO taskUpdateDTO) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));

        if (taskUpdateDTO.getCompleted() != null) {
            task.setCompleted(taskUpdateDTO.getCompleted());
        }
        if (taskUpdateDTO.getDate() != null) {
            task.setDate(taskUpdateDTO.getDate());
        }
        if (taskUpdateDTO.getDescription() != null) {
            task.setDescription(taskUpdateDTO.getDescription());
        }
        if (taskUpdateDTO.getTitle() != null) {
            task.setTitle(taskUpdateDTO.getTitle());
        }

        Task savedPatchedTask = taskRepository.save(task);
        return ModelUtils.convertToDto(savedPatchedTask, TaskDTO.class);
    }

}
