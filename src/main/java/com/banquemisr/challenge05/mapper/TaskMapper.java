package com.banquemisr.challenge05.mapper;

import com.banquemisr.challenge05.dto.task.TaskCreationDTO;
import com.banquemisr.challenge05.dto.task.TaskDetailsDTO;
import com.banquemisr.challenge05.dto.task.UpdateTaskDTO;
import com.banquemisr.challenge05.model.Task;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface TaskMapper {
    @Mapping(target = "userId", source = "assignedTo.id")
    @Mapping(target = "username", source = "assignedTo.username")
    TaskDetailsDTO mapTaskToTaskDetailsDTO(Task task);

    @Mapping(target = "assignedTo", ignore = true)
    Task mapTaskCreationDTOToTask(TaskCreationDTO taskCreationDTO);

    @Mapping(target = "userId", source = "assignedTo.id")
    TaskCreationDTO mapTaskToTaskCreationResponse(Task task);

    @Mapping(target = "userId", source = "assignedTo.id")
    @Mapping(target = "username", source = "assignedTo.username")
    UpdateTaskDTO mapTaskToUpdateTaskDTO(Task task);

    @Mapping(target = "assignedTo", ignore = true)
    void updateTaskFromDTO(UpdateTaskDTO dto, @MappingTarget Task entity);


}
