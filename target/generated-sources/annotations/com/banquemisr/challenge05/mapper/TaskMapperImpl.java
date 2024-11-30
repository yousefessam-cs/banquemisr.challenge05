package com.banquemisr.challenge05.mapper;

import com.banquemisr.challenge05.dto.task.TaskCreationDTO;
import com.banquemisr.challenge05.dto.task.TaskDetailsDTO;
import com.banquemisr.challenge05.dto.task.UpdateTaskDTO;
import com.banquemisr.challenge05.model.Task;
import com.banquemisr.challenge05.model.User;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-11-30T12:01:57+0200",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 22.0.2 (Oracle Corporation)"
)
@Component
public class TaskMapperImpl implements TaskMapper {

    @Override
    public TaskDetailsDTO mapTaskToTaskDetailsDTO(Task task) {
        if ( task == null ) {
            return null;
        }

        TaskDetailsDTO.TaskDetailsDTOBuilder taskDetailsDTO = TaskDetailsDTO.builder();

        taskDetailsDTO.userId( taskAssignedToId( task ) );
        taskDetailsDTO.username( taskAssignedToUsername( task ) );
        taskDetailsDTO.title( task.getTitle() );
        taskDetailsDTO.description( task.getDescription() );
        taskDetailsDTO.priority( task.getPriority() );
        taskDetailsDTO.dueDate( task.getDueDate() );
        taskDetailsDTO.status( task.getStatus() );

        return taskDetailsDTO.build();
    }

    @Override
    public Task mapTaskCreationDTOToTask(TaskCreationDTO taskCreationDTO) {
        if ( taskCreationDTO == null ) {
            return null;
        }

        Task.TaskBuilder task = Task.builder();

        task.title( taskCreationDTO.getTitle() );
        task.description( taskCreationDTO.getDescription() );
        task.status( taskCreationDTO.getStatus() );
        task.priority( taskCreationDTO.getPriority() );
        task.dueDate( taskCreationDTO.getDueDate() );

        return task.build();
    }

    @Override
    public TaskCreationDTO mapTaskToTaskCreationResponse(Task task) {
        if ( task == null ) {
            return null;
        }

        TaskCreationDTO.TaskCreationDTOBuilder taskCreationDTO = TaskCreationDTO.builder();

        taskCreationDTO.userId( taskAssignedToId( task ) );
        taskCreationDTO.title( task.getTitle() );
        taskCreationDTO.description( task.getDescription() );
        taskCreationDTO.priority( task.getPriority() );
        taskCreationDTO.dueDate( task.getDueDate() );
        taskCreationDTO.status( task.getStatus() );

        return taskCreationDTO.build();
    }

    @Override
    public UpdateTaskDTO mapTaskToUpdateTaskDTO(Task task) {
        if ( task == null ) {
            return null;
        }

        UpdateTaskDTO.UpdateTaskDTOBuilder updateTaskDTO = UpdateTaskDTO.builder();

        updateTaskDTO.userId( taskAssignedToId( task ) );
        updateTaskDTO.username( taskAssignedToUsername( task ) );
        updateTaskDTO.title( task.getTitle() );
        updateTaskDTO.description( task.getDescription() );
        updateTaskDTO.priority( task.getPriority() );
        updateTaskDTO.dueDate( task.getDueDate() );
        updateTaskDTO.status( task.getStatus() );

        return updateTaskDTO.build();
    }

    @Override
    public void updateTaskFromDTO(UpdateTaskDTO dto, Task entity) {
        if ( dto == null ) {
            return;
        }

        entity.setTitle( dto.getTitle() );
        entity.setDescription( dto.getDescription() );
        entity.setStatus( dto.getStatus() );
        entity.setPriority( dto.getPriority() );
        entity.setDueDate( dto.getDueDate() );
    }

    private Long taskAssignedToId(Task task) {
        if ( task == null ) {
            return null;
        }
        User assignedTo = task.getAssignedTo();
        if ( assignedTo == null ) {
            return null;
        }
        Long id = assignedTo.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private String taskAssignedToUsername(Task task) {
        if ( task == null ) {
            return null;
        }
        User assignedTo = task.getAssignedTo();
        if ( assignedTo == null ) {
            return null;
        }
        String username = assignedTo.getUsername();
        if ( username == null ) {
            return null;
        }
        return username;
    }
}
