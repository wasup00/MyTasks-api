package com.wasup.mytasks.model;

import com.wasup.mytasks.model.dto.TaskDTO;
import com.wasup.mytasks.model.dto.UserResponseDTO;
import com.wasup.mytasks.model.entity.Task;
import com.wasup.mytasks.model.entity.User;
import org.modelmapper.ModelMapper;

public class ModelUtils {

    private static final ModelMapper modelMapper = new ModelMapper();

    public static <D, E> E convertToEntity(D dto, Class<E> entityClass) {
        return modelMapper.map(dto, entityClass);
    }

    public static <D, E> D convertToDto(E entity, Class<D> dtoClass) {
        return modelMapper.map(entity, dtoClass);
    }

    public static boolean validateUser(User user, UserResponseDTO userResponseDTO) {
        return user.getId().equals(userResponseDTO.getId()) && user.getUsername().equals(userResponseDTO.getUsername()) &&
                user.getName().equals(userResponseDTO.getName()) && user.getLastName().equals(userResponseDTO.getLastName());
    }

    public static boolean validateTask(Task task, TaskDTO taskDTO) {
        return task.getTitle().equals(taskDTO.getTitle()) && task.getDescription().equals(taskDTO.getDescription()) &&
                task.getDate().equals(taskDTO.getDate()) && task.getUserId().getId().equals(taskDTO.getUserId());
    }
}
