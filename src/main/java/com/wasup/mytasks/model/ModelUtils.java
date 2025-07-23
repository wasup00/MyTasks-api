package com.wasup.mytasks.model;

import com.wasup.mytasks.model.dto.TaskDTO;
import com.wasup.mytasks.model.dto.UserDTO;
import com.wasup.mytasks.model.entity.Task;
import com.wasup.mytasks.model.entity.User;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;

public class ModelUtils {

    private static final ModelMapper modelMapper = new ModelMapper();

    public static void configurePasswordEncoder(PasswordEncoder passwordEncoder) {
        modelMapper.createTypeMap(UserDTO.class, User.class)
                .addMappings(mapper -> mapper.map(src -> passwordEncoder.encode(src.getPassword()),
                        User::setPassword));
    }

    public static <D, E> E convertToEntity(D dto, Class<E> entityClass) {
        return modelMapper.map(dto, entityClass);
    }

    public static <D, E> D convertToDto(E entity, Class<D> dtoClass) {
        return modelMapper.map(entity, dtoClass);
    }

    public static boolean validateUser(User user, UserDTO userDTO) {
        return user.getId().equals(userDTO.getId()) && user.getUsername().equals(userDTO.getUsername()) &&
                user.getName().equals(userDTO.getName()) && user.getLastName().equals(userDTO.getLastName());
    }

    public static boolean validateTask(Task task, TaskDTO taskDTO) {
        return task.getTitle().equals(taskDTO.getTitle()) && task.getDescription().equals(taskDTO.getDescription()) &&
                task.getDate().equals(taskDTO.getDate()) && task.getUserId().getId().equals(taskDTO.getUserId());
    }
}
