package com.wasup.mytasks.model;

import com.wasup.mytasks.model.dto.TaskDTO;
import com.wasup.mytasks.model.dto.UserRequestDTO;
import com.wasup.mytasks.model.dto.UserResponseDTO;
import com.wasup.mytasks.model.entity.Task;
import com.wasup.mytasks.model.entity.User;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class ModelUtils {

    private static final ModelMapper modelMapper = new ModelMapper();
    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    static {
        modelMapper.addMappings(new PropertyMap<UserRequestDTO, User>() {
            @Override
            protected void configure() {
                using(ctx -> passwordEncoder.encode((String) ctx.getSource()))
                        .map(source.getPassword(), destination.getPassword());
            }
        });
    }




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
