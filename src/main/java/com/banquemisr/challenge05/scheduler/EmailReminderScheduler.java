package com.banquemisr.challenge05.scheduler;

import com.banquemisr.challenge05.service.emailNotification.ITaskAlertService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;



@Configuration
@RequiredArgsConstructor
@EnableScheduling
@Slf4j
public class EmailReminderScheduler {

    private final ITaskAlertService ITaskAlertService;

    @Scheduled(cron = "0 0 * * * *")  // Runs every hour
    public void checkTaskDeadlines() {
        try {
            log.info("Executing Pending Task Email Reminder Job");
            ITaskAlertService.notifyPendingTasks();
        } catch (Exception e) {
            log.error("Error executing email reminder job", e);
        }
    }
}