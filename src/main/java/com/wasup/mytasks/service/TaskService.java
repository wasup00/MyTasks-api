package com.wasup.mytasks.service;

import com.wasup.mytasks.model.ModelUtils;
import com.wasup.mytasks.model.dto.TaskDTO;
import com.wasup.mytasks.model.entity.Task;
import com.wasup.mytasks.model.entity.User;
import com.wasup.mytasks.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserService userService;

    public TaskDTO createTask(TaskDTO taskDTO) {
        Task savedTask = taskRepository.save(ModelUtils.convertToEntity(taskDTO, Task.class));
        taskDTO.setId(savedTask.getId());
        if (ModelUtils.validateTask(savedTask, taskDTO)){
            return taskDTO;
        }
        return null;
    }

    public List<TaskDTO> getTasksByUserId(Long userId) {
        User user = userService.findUserEntityById(userId);
        List<Task> tasks = taskRepository.findByUserIdOrderByDateDesc(user);
        return tasks.stream().map(task -> ModelUtils.convertToDto(task, TaskDTO.class)).collect(Collectors.toList());
    }

    public TaskDTO getTaskById(Long id) {
        return taskRepository.findById(id)
                .map(task -> ModelUtils.convertToDto(task, TaskDTO.class))
                .orElseThrow(() -> new RuntimeException("Task not found"));
    }
}
