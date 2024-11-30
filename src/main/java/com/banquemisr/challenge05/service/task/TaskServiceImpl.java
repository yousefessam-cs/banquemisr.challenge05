package com.banquemisr.challenge05.service.task;

import com.banquemisr.challenge05.dto.task.TaskCreationDTO;
import com.banquemisr.challenge05.exception.ResourceNotFoundException;
import com.banquemisr.challenge05.repository.TaskRepository;
import com.banquemisr.challenge05.repository.UserRepository;
import com.banquemisr.challenge05.mapper.TaskMapper;
import com.banquemisr.challenge05.model.Task;
import com.banquemisr.challenge05.model.User;
import com.banquemisr.challenge05.specification.criteria.TaskSearchCriteria;
import com.banquemisr.challenge05.specification.TaskSpecification;
import com.banquemisr.challenge05.dto.task.TaskDetailsDTO;
import com.banquemisr.challenge05.dto.task.UpdateTaskDTO;
import com.banquemisr.challenge05.service.email.IEmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class TaskServiceImpl implements ITaskService {


    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;
    private final UserRepository userRepository;
    private final IEmailService IEmailService;

    public TaskServiceImpl(TaskRepository taskRepository, TaskMapper taskMapper, UserRepository userRepository, IEmailService IEmailService) {
        this.taskRepository = taskRepository;
        this.taskMapper = taskMapper;
        this.userRepository = userRepository;
        this.IEmailService = IEmailService;
    }

    @Override
    public TaskCreationDTO create(TaskCreationDTO taskCreationDTO) {
        log.info("Start creating a new task with title: {}", taskCreationDTO.getTitle());
        Task task = taskMapper.mapTaskCreationDTOToTask(taskCreationDTO);
        User user = fetchUser(taskCreationDTO.getUserId(), taskCreationDTO.getUsername());
        assignTaskToUser(task, user);
        Task createdTask = taskRepository.save(task);
        notifyUser(user, "Task Assigned", "You have been assigned a new task: " + task.getTitle());
        log.info("End creating a new task with title: {}", taskCreationDTO.getTitle());
        return taskMapper.mapTaskToTaskCreationResponse(createdTask);
    }

    @Override
    public UpdateTaskDTO update(Long id, UpdateTaskDTO updateTaskDTO) {
        log.info("Start updating task with Id: {}, and request body: {}", id, updateTaskDTO);
        Task existingTask = findTaskById(id);
        taskMapper.updateTaskFromDTO(updateTaskDTO, existingTask);
        User user = fetchUser(updateTaskDTO.getUserId(), updateTaskDTO.getUsername());
        assignTaskToUser(existingTask, user);
        Task updatedTask = taskRepository.save(existingTask);
        notifyUser(user, "Task Updated", "Your task has been updated: " + existingTask.getTitle());
        log.info("End updating task with Id: {}, and request body: {}", id, updateTaskDTO);
        return taskMapper.mapTaskToUpdateTaskDTO(updatedTask);
    }

    @Override
    public TaskDetailsDTO getTaskById(Long id) {
        log.info("Start fetching task by Id: {}", id);
        Task task = findTaskById(id);
        log.info("End fetching task by Id: {}", id);
        return taskMapper.mapTaskToTaskDetailsDTO(task);
    }

    @Override
    public Page<TaskDetailsDTO> SearchAllTasks(TaskSearchCriteria criteria) {
        log.info("Start searching for tasks with criteria: {}", criteria);
        Pageable pageable = PageRequest.of(criteria.getPage(), criteria.getSize(), Sort.by(criteria.getSortBy()));
        Specification<Task> specification = TaskSpecification.buildSearchCriteria(criteria);
        Page<Task> tasks = taskRepository.findAll(specification, pageable);
        log.info("End searching for tasks with criteria: {}", criteria);
        return tasks.map(taskMapper::mapTaskToTaskDetailsDTO);
    }


    @Override
    public void delete(Long id) {
        log.info("Start deleting task with Id: {}", id);
        Task task = findTaskById(id);
        taskRepository.delete(task);
        log.info("End deleting task with Id: {}", id);
    }

    private Task findTaskById(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + id));
    }

    private User fetchUser(Long userId, String username) {
        if (userId != null) {
            return userRepository.findById(userId)
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        } else if (username != null) {
            return userRepository.findByUserName(username)
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + username));
        }
        return null;
    }

    private void assignTaskToUser(Task task, User user) {
        if (user != null) {
            task.setAssignedTo(user);
        }
    }

    private void notifyUser(User user, String subject, String message) {
        if (user != null) {
            IEmailService.sendEmail(user.getEmail(), subject, message);
        }
    }

}

