package com.banquemisr.challenge05.repository;

import com.banquemisr.challenge05.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification,Long> {
}
