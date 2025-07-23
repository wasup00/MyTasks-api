package com.wasup.mytasks.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;
import java.util.Date;

@Data
@Builder
public class TaskDTO {
    private Integer id;

    @NotBlank(message = "Title is required")
    private String title;
    private String description;
    private Date date;
    private Integer userId;
}