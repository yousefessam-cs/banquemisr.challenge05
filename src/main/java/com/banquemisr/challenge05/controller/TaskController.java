package com.banquemisr.challenge05.controller;

import com.banquemisr.challenge05.dto.task.TaskCreationDTO;
import com.banquemisr.challenge05.dto.task.TaskDetailsDTO;
import com.banquemisr.challenge05.dto.task.UpdateTaskDTO;
import com.banquemisr.challenge05.enums.TaskPriority;
import com.banquemisr.challenge05.enums.TaskStatus;
import com.banquemisr.challenge05.specification.criteria.TaskSearchCriteria;
import com.banquemisr.challenge05.service.task.ITaskService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/tasks")
@Slf4j
public class TaskController {


    private final ITaskService ITaskService;

    public TaskController(ITaskService ITaskService) {
        this.ITaskService = ITaskService;
    }

    @PostMapping
    public ResponseEntity<TaskCreationDTO> createTask(@Valid @RequestBody TaskCreationDTO taskCreationDTO) {
        log.info("Creating task with details: {}", taskCreationDTO);
        TaskCreationDTO createdTask = ITaskService.create(taskCreationDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTask);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UpdateTaskDTO> updateTask(@PathVariable Long id, @Valid @RequestBody UpdateTaskDTO updateTaskDTO) {
        log.info("Updating task with ID: {} and details: {}", id, updateTaskDTO);
        UpdateTaskDTO updatedTaskDTO = ITaskService.update(id, updateTaskDTO);
        return ResponseEntity.ok(updatedTaskDTO);
    }
    @GetMapping("/{id}")
    public ResponseEntity<TaskDetailsDTO> getTaskById(@PathVariable Long id) {
        log.info("Getting task details with ID: {}", id);
        TaskDetailsDTO taskDetailsDTO = ITaskService.getTaskById(id);
        return ResponseEntity.ok(taskDetailsDTO);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteTask(@PathVariable Long id) {
        log.info("Deleting task with ID: {}", id);
        ITaskService.delete(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(String.format("Task with ID %d deleted successfully.", id));
    }

    @GetMapping
    public ResponseEntity<Page<TaskDetailsDTO>> searchAllTasks(
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "priority", required = false) TaskPriority priority,
            @RequestParam(value = "status", required = false) TaskStatus status,
            @RequestParam(value = "dueDateFrom", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dueDateFrom,
            @RequestParam(value = "dueDateTo", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dueDateTo,
            @RequestParam(value = "sortBy", required = false, defaultValue = "id") String sortBy,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        log.info("Fetching all tasks with filters: title={}, description={}, priority={}, status={}, dueDateFrom={}, dueDateTo={}, sortBy={}, page={}, size={}",
                title, description, priority, status, dueDateFrom, dueDateTo, sortBy, page, size);

        TaskSearchCriteria criteria = buildSearchCriteria(title, description, priority, status, dueDateFrom, dueDateTo, sortBy, page, size);
        Page<TaskDetailsDTO> tasks = ITaskService.SearchAllTasks(criteria);

        return ResponseEntity.ok(tasks);
    }


    private TaskSearchCriteria buildSearchCriteria(
            String title, String description, TaskPriority priority, TaskStatus status,
            LocalDateTime dueDateFrom, LocalDateTime dueDateTo, String sortBy, int page, int size
    ) {
        return TaskSearchCriteria.builder()
                .title(title)
                .description(description)
                .priority(priority)
                .status(status)
                .dueDateFrom(dueDateFrom)
                .dueDateTo(dueDateTo)
                .sortBy(sortBy)
                .page(page)
                .size(size)
                .build();
    }
}
