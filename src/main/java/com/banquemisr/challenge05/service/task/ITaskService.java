package com.banquemisr.challenge05.service.task;

import com.banquemisr.challenge05.dto.task.TaskCreationDTO;
import com.banquemisr.challenge05.dto.task.TaskDetailsDTO;
import com.banquemisr.challenge05.specification.criteria.TaskSearchCriteria;
import com.banquemisr.challenge05.dto.task.UpdateTaskDTO;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;

public interface ITaskService {
    TaskCreationDTO create(TaskCreationDTO taskCreationDTO);

    UpdateTaskDTO update(Long id, UpdateTaskDTO updateTaskDTO);

    TaskDetailsDTO getTaskById(Long id);

    @Cacheable(value = "tasks", key = "#criteria.toString()")
    Page<TaskDetailsDTO> SearchAllTasks(TaskSearchCriteria criteria);

    void delete(Long id);

}
