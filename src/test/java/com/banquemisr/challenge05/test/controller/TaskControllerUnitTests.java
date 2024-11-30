package com.banquemisr.challenge05.test.controller;

import com.banquemisr.challenge05.controller.TaskController;
import com.banquemisr.challenge05.dto.task.TaskDetailsDTO;
import com.banquemisr.challenge05.enums.TaskPriority;
import com.banquemisr.challenge05.enums.TaskStatus;
import com.banquemisr.challenge05.dto.task.TaskCreationDTO;
import com.banquemisr.challenge05.dto.task.UpdateTaskDTO;
import com.banquemisr.challenge05.service.task.ITaskService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest()
@ContextConfiguration(classes = TaskController.class)
@AutoConfigureMockMvc(addFilters = false)
public class TaskControllerUnitTests{
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ITaskService ITaskService;

    @Autowired
    private ObjectMapper objectMapper;

    private TaskCreationDTO taskCreationDTO;
    private TaskDetailsDTO taskDetailsDTO;
    private UpdateTaskDTO updateTaskDTO;

    @BeforeEach
    void setUp() {
        taskCreationDTO = TaskCreationDTO.builder()
                .title("Sample Task")
                .description("Description")
                .priority(TaskPriority.HIGH)
                .dueDate(LocalDateTime.now().plusDays(1))
                .status(TaskStatus.PENDING)
                .userId(1L)
                .username("testuser")
                .build();

        taskDetailsDTO = TaskDetailsDTO.builder()
                .userId(1L)
                .title("Sample Task")
                .description("Description")
                .priority(TaskPriority.HIGH)
                .dueDate(LocalDateTime.now().plusDays(1))
                .status(TaskStatus.PENDING)
                .username("testuser")
                .build();

        updateTaskDTO = UpdateTaskDTO.builder()
                .title("Updated Task")
                .description("Updated Description")
                .priority(TaskPriority.MEDIUM)
                .dueDate(LocalDateTime.now().plusDays(2))
                .status(TaskStatus.DONE)
                .userId(1L)
                .username("updateduser")
                .build();
    }

    @Test
    void createTask_ShouldReturnCreatedTask() throws Exception {
        Mockito.when(ITaskService.create(any(TaskCreationDTO.class))).thenReturn(taskCreationDTO);

        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskCreationDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title", is(taskCreationDTO.getTitle())))
                .andExpect(jsonPath("$.priority", is(taskCreationDTO.getPriority().toString())))
                .andExpect(jsonPath("$.userId", is(taskCreationDTO.getUserId().intValue())))
                .andExpect(jsonPath("$.username", is(taskCreationDTO.getUsername())));
    }

    @Test
    void getTaskById_ShouldReturnTask() throws Exception {
        Mockito.when(ITaskService.getTaskById(eq(1L))).thenReturn(taskDetailsDTO);

        mockMvc.perform(get("/api/tasks/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is(taskDetailsDTO.getTitle())))
                .andExpect(jsonPath("$.userId", is(taskDetailsDTO.getUserId().intValue())))
                .andExpect(jsonPath("$.username", is(taskDetailsDTO.getUsername())));
    }

    @Test
    void getAllTasks_ShouldReturnPagedTasks() throws Exception {
        Page<TaskDetailsDTO> tasks = new PageImpl<>(Collections.singletonList(taskDetailsDTO));
        Mockito.when(ITaskService.SearchAllTasks(any())).thenReturn(tasks);

        mockMvc.perform(get("/api/tasks")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].title", is(taskDetailsDTO.getTitle())))
                .andExpect(jsonPath("$.content[0].userId", is(taskDetailsDTO.getUserId().intValue())))
                .andExpect(jsonPath("$.content[0].username", is(taskDetailsDTO.getUsername())));
    }

    @Test
    void updateTask_ShouldReturnUpdatedTask() throws Exception {
        Mockito.when(ITaskService.update(eq(1L), any(UpdateTaskDTO.class))).thenReturn(updateTaskDTO);

        mockMvc.perform(put("/api/tasks/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateTaskDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is(updateTaskDTO.getTitle())))
                .andExpect(jsonPath("$.priority", is(updateTaskDTO.getPriority().toString())))
                .andExpect(jsonPath("$.userId", is(updateTaskDTO.getUserId().intValue())))
                .andExpect(jsonPath("$.username", is(updateTaskDTO.getUsername())));
    }

    @Test
    void deleteTask_Success() throws Exception {
        mockMvc.perform(delete("/api/tasks/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Task with ID 1 deleted successfully.")); // Check the message

        Mockito.verify(ITaskService).delete(1L);
    }
}
