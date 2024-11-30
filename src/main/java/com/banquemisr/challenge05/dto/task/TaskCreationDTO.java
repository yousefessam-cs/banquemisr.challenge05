package com.banquemisr.challenge05.dto.task;

import com.banquemisr.challenge05.enums.TaskPriority;
import com.banquemisr.challenge05.enums.TaskStatus;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskCreationDTO {

    @NotBlank(message = "title can't be empty")
    @Size(max = 50, message = "Title maximum length is 50 characters")
    @JsonProperty("title")
    private String title;

    @Size(max = 200, message = "Description maximum length is 200 characters")
    @JsonProperty("description")
    private String description;

    @NotNull(message = "Priority can't be null")
    @JsonProperty("priority")
    private TaskPriority priority;

    @NotNull(message = "Due date can't be null")
    @JsonProperty("dueDate")
    private LocalDateTime dueDate;

    @NotNull(message = "Status can't be null")
    @JsonProperty("status")
    private TaskStatus status;

    @JsonProperty("userId")
    private Long userId;

    @JsonProperty("username")
    private String username;

}
