package com.banquemisr.challenge05.dto.task;

import com.banquemisr.challenge05.enums.TaskPriority;
import com.banquemisr.challenge05.enums.TaskStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskDetailsDTO {

    private Long userId;
    private String title;
    private String description;
    private TaskPriority priority;
    private LocalDateTime dueDate;
    private TaskStatus status;
    private String username;

}
