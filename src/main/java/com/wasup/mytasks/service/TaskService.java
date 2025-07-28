package com.wasup.mytasks.service;

import com.wasup.mytasks.model.ModelUtils;
import com.wasup.mytasks.model.dto.TaskDTO;
import com.wasup.mytasks.model.entity.Task;
import com.wasup.mytasks.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserService userService;

    public ResponseEntity<TaskDTO> createTask(TaskDTO taskDTO) {
        if (!userService.existsById(taskDTO.getUserId())) {
            log.atError().addArgument(taskDTO::getUserId).log("No user with userId -> {} found");
            return ResponseEntity.badRequest().body(null);
        }
        Task savedTask = taskRepository.save(ModelUtils.convertToEntity(taskDTO, Task.class));
        taskDTO.setId(savedTask.getId());
        if (!ModelUtils.validateTask(savedTask, taskDTO)) {
            log.atError()
                    .addArgument(taskDTO::toString)
                    .addArgument(savedTask::toString)
                    .log("Problem when validating task saved and requested\ntaskDTO:\n\t{}\nsavedtask:\n\t{}");
            return ResponseEntity.internalServerError().build();
        }
        log.atInfo()
                .addArgument(taskDTO::getId)
                .addArgument(taskDTO::getUserId)
                .log("Task {} created successfully for user {}");
        return ResponseEntity.created(URI.create("")).body(taskDTO);
    }

    public ResponseEntity<List<TaskDTO>> getTasksByUserId(Long userId) {
        if (!userService.existsById(userId)) {
            log.atError().addArgument(userId).log("No user with userId -> {} found");
            return ResponseEntity.badRequest().body(Collections.emptyList());
        }
        List<Task> tasks = taskRepository.findByUser_IdOrderByDateDesc(userId);
        List<TaskDTO> taskDTOs = tasks.stream().map(task -> ModelUtils.convertToDto(task, TaskDTO.class)).toList();
        return ResponseEntity.ok(taskDTOs);
    }

    public ResponseEntity<TaskDTO> getTaskById(Long id) {
        return taskRepository.findById(id)
                .map(task -> ResponseEntity.ok(ModelUtils.convertToDto(task, TaskDTO.class)))
                .orElse(ResponseEntity.notFound().build());
    }
}
