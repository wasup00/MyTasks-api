package com.wasup.mytasks.controller;

import com.wasup.mytasks.api.TaskApi;
import com.wasup.mytasks.model.TaskDTO;
import com.wasup.mytasks.model.TaskUpdateDTO;
import com.wasup.mytasks.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class TaskController implements TaskApi {
    private final TaskService taskService;

    @Override
    public ResponseEntity<TaskDTO> createTask(TaskDTO taskDTO) {
        TaskDTO task = taskService.createTask(taskDTO);
        return ResponseEntity.created(URI.create("")).body(task);
    }

    @Override
    public ResponseEntity<List<TaskDTO>> getAllTasks() {
        List<TaskDTO> tasks = taskService.getAllTasks();
        return ResponseEntity.ok(tasks);
    }

    @Override
    public ResponseEntity<List<TaskDTO>> getTasksByUser(Long userId, String username) {
        List<TaskDTO> taskDTOS = taskService.getTasksByUser(userId, username);
        return ResponseEntity.ok(taskDTOS);
    }

    @Override
    public ResponseEntity<TaskDTO> getTask(Long id) {
        TaskDTO taskDTO = taskService.getTaskById(id);
        return ResponseEntity.ok(taskDTO);
    }

    @Override
    public ResponseEntity<Void> deleteTask(Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<TaskDTO> updateTask(Long id, TaskDTO taskDTO) {
        return ResponseEntity.ok(taskService.updateTask(id, taskDTO));
    }

    @Override
    public ResponseEntity<TaskDTO> patchTask(Long id, TaskUpdateDTO taskUpdateDTO) {
        return ResponseEntity.ok(taskService.patchUser(id, taskUpdateDTO));
    }

}
