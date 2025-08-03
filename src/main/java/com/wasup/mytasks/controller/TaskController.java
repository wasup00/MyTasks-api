package com.wasup.mytasks.controller;

import com.wasup.mytasks.api.TaskApi;
import com.wasup.mytasks.model.TaskDTO;
import com.wasup.mytasks.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

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
    public ResponseEntity<TaskDTO> getTask(Long id) {
        TaskDTO taskDTO = taskService.getTaskById(id);
        return ResponseEntity.ok(taskDTO);
    }

    @Override
    public ResponseEntity<Void> deleteTask(Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }
}
