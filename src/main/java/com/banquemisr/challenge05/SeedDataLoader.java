package com.banquemisr.challenge05;

import com.banquemisr.challenge05.enums.Role;
import com.banquemisr.challenge05.model.User;
import com.banquemisr.challenge05.repository.TaskRepository;
import com.banquemisr.challenge05.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import jakarta.transaction.Transactional;

@Component
@Transactional
public class SeedDataLoader implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Running SeedDataLoader...");
        System.out.println("User count: " + userRepository.count());

        if (userRepository.count() == 0) {
            // Create admin user
            User admin = User.builder()
                    .userName("admin")
                    .password(passwordEncoder.encode("admin"))
                    .email("admin@example.com")
                    .role(Role.ADMIN)
                    .build();
            userRepository.save(admin);

            // Create regular user
            User user = User.builder()
                    .userName("user")
                    .password(passwordEncoder.encode("user"))
                    .email("user@example.com")
                    .role(Role.USER)
                    .build();
            userRepository.save(user);

//            // Create tasks and assign to users
//            Task adminTask = Task.builder()
//                    .title("Admin Task")
//                    .description("A task for the admin user.")
//                    .status(TaskStatus.IN_PROGRESS)
//                    .priority(TaskPriority.HIGH)
//                    .dueDate(LocalDateTime.now().plusDays(1))
//                    .assignedTo(admin)
//                    .build();
//            taskRepository.save(adminTask);
//
//            Task userTask = Task.builder()
//                    .title("User Task")
//                    .description("A task for the regular user.")
//                    .status(TaskStatus.IN_PROGRESS)
//                    .priority(TaskPriority.MEDIUM)
//                    .dueDate(LocalDateTime.now().plusDays(2))
//                    .assignedTo(user)
//                    .build();
//            taskRepository.save(userTask);

            System.out.println("Seed data loaded successfully!");
        } else {
            System.out.println("Data already exists. Skipping seed data loading.");
        }
    }
}