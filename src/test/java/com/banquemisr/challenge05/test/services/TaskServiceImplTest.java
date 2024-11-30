package com.banquemisr.challenge05.test.services;

import com.banquemisr.challenge05.dto.task.TaskDetailsDTO;
import com.banquemisr.challenge05.exception.ResourceNotFoundException;
import com.banquemisr.challenge05.mapper.TaskMapper;
import com.banquemisr.challenge05.model.Task;
import com.banquemisr.challenge05.model.User;
import com.banquemisr.challenge05.dto.task.TaskCreationDTO;
import com.banquemisr.challenge05.dto.task.UpdateTaskDTO;
import com.banquemisr.challenge05.repository.TaskRepository;
import com.banquemisr.challenge05.repository.UserRepository;
import com.banquemisr.challenge05.service.email.IEmailService;
import com.banquemisr.challenge05.service.task.TaskServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@SpringBootTest
public class TaskServiceImplTest {

    @MockBean
    private UserRepository userRepository;
    @MockBean
    private TaskRepository taskRepository;
    @MockBean
    private TaskMapper taskMapper;
    @MockBean
    private IEmailService IEmailService;
    @Autowired
    private TaskServiceImpl taskService;

    private User user;
    private Task task;
    private TaskCreationDTO taskCreationDTO;
    private UpdateTaskDTO updateTaskDTO;

    @BeforeEach
    public void setUp() {
        // Setup common mock data before each test
        user = new User();
        user.setId(1L);
        user.setEmail("user@example.com");

        task = new Task();
        task.setId(1L);
        task.setTitle("New Task");

        taskCreationDTO = new TaskCreationDTO();
        taskCreationDTO.setTitle("New Task");
        taskCreationDTO.setUserId(1L);

        updateTaskDTO = new UpdateTaskDTO();
        updateTaskDTO.setTitle("Updated Task");
    }

    @Test
    public void testCreateTaskSuccess() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(taskMapper.mapTaskCreationDTOToTask(taskCreationDTO)).thenReturn(task);
        when(taskRepository.save(task)).thenReturn(task);
        when(taskMapper.mapTaskToTaskCreationResponse(task)).thenReturn(taskCreationDTO);

        TaskCreationDTO result = taskService.create(taskCreationDTO);

        assertNotNull(result);
        verify(IEmailService).sendEmail(eq("user@example.com"), eq("Task Assigned"), anyString());
    }
    @Test
    public void testDeleteTaskSuccess() {

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        // Call the delete method
        taskService.delete(1L);

        // Verify that the delete method in the repository was called once
        verify(taskRepository, times(1)).delete(task);
    }

    @Test
    public void testUpdateTaskNotFound() {
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            taskService.update(1L, updateTaskDTO);
        });
    }

    @Test
    public void testGetTaskByIdFound() {
        TaskDetailsDTO taskDetailsDTO = new TaskDetailsDTO();
        taskDetailsDTO.setTitle("Task 1");

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(taskMapper.mapTaskToTaskDetailsDTO(task)).thenReturn(taskDetailsDTO);

        TaskDetailsDTO result = taskService.getTaskById(1L);

        assertNotNull(result);
        assertEquals("Task 1", result.getTitle());
    }

    @Test
    public void testGetAllTasks() {
        List<Task> tasks = List.of(task);

        when(taskRepository.findAll()).thenReturn(tasks);
        when(taskMapper.mapTaskToTaskDetailsDTO(any())).thenReturn(new TaskDetailsDTO());

        List<TaskDetailsDTO> results = tasks.stream().map(taskMapper::mapTaskToTaskDetailsDTO).collect(Collectors.toList());
        assertEquals(1, results.size());
    }

    @Test
    public void testDeleteTaskNotFound() {
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            taskService.delete(1L);
        });
    }


    @Test
    public void testCreateTaskSuccessWithEmailNotification() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(taskMapper.mapTaskCreationDTOToTask(taskCreationDTO)).thenReturn(task);
        when(taskRepository.save(task)).thenReturn(task);
        when(taskMapper.mapTaskToTaskCreationResponse(task)).thenReturn(taskCreationDTO);

        TaskCreationDTO result = taskService.create(taskCreationDTO);

        assertNotNull(result);
        verify(IEmailService, times(1)).sendEmail(eq("user@example.com"), eq("Task Assigned"), anyString());
    }

    @Test
    public void testUpdateTaskNotFoundException() {
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            taskService.update(1L, updateTaskDTO);
        });
    }


    @Test
    public void testDeleteTaskNotFoundThrowsException() {
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            taskService.delete(1L);
        });
    }
}
