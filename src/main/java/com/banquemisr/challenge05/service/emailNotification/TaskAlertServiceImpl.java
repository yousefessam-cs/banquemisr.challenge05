package com.banquemisr.challenge05.service.emailNotification;

import com.banquemisr.challenge05.exception.EmailServiceException;
import com.banquemisr.challenge05.repository.TaskRepository;
import com.banquemisr.challenge05.model.Task;
import com.banquemisr.challenge05.service.email.IEmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class TaskAlertServiceImpl implements ITaskAlertService {

    private final IEmailService IEmailService;
    private final TaskRepository taskRepository;

    public TaskAlertServiceImpl(IEmailService IEmailService, TaskRepository taskRepository) {
        this.IEmailService = IEmailService;
        this.taskRepository = taskRepository;
    }

    @Override
    public void notifyPendingTasks() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime upcomingDeadlineTime = now.plusHours(1);
        List<Task> pendingTasks = getPendingTasksFrom(now, upcomingDeadlineTime);

        if (pendingTasks.isEmpty()) {
            log.info("No tasks with upcoming deadlines.");
            return;
        }

        for (Task task : pendingTasks) {
            String emailBody = buildEmailBody(task);
            try {
                IEmailService.sendEmail(
                        task.getAssignedTo().getEmail(),
                        "Task Deadline Reminder",
                        emailBody
                );
                log.info("Task reminder email sent successfully to: {}", task.getAssignedTo().getEmail());
            } catch (Exception e) {
                log.error("Error while sending email to {}: {}", task.getAssignedTo().getEmail(), e.getMessage());
                throw new EmailServiceException(e.getMessage());
            }
        }
    }


    private List<Task> getPendingTasksFrom(LocalDateTime fromDateTime, LocalDateTime toDateTime) {
        return taskRepository.findPendingTasksByDeadline(fromDateTime, toDateTime);
    }

    private String buildEmailBody(Task task) {
        return String.format(
                "Dear %s,%n%n" +
                        "You have an upcoming task:%n" +
                        "Title: %s%n" +
                        "Description: %s%n" +
                        "Deadline: %s%n%n" +
                        "Please ensure to complete it on time.%n%n" +
                        "Best regards,%nTask Management System",
                task.getAssignedTo().getUsername(),
                task.getTitle(),
                task.getDescription(),
                task.getDueDate()
        );
    }
}

