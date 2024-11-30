-- Table for User
CREATE TABLE IF NOT EXISTS users (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       user_name VARCHAR(50) NOT NULL UNIQUE,
                       password VARCHAR(255) NOT NULL,
                       email VARCHAR(100) NOT NULL UNIQUE,
                       role VARCHAR(20) NOT NULL,
                       CONSTRAINT chk_username_length CHECK (CHAR_LENGTH(user_name) BETWEEN 4 AND 50),
                       CONSTRAINT chk_password_length CHECK (CHAR_LENGTH(password) >= 8),
                       CONSTRAINT chk_role CHECK (role IN ('ADMIN', 'USER'))
);

-- Table for Task
CREATE TABLE IF NOT EXISTS task (
                      id BIGINT AUTO_INCREMENT PRIMARY KEY,
                      title VARCHAR(50) NOT NULL,
                      description VARCHAR(200),
                      status VARCHAR(20),
                      priority VARCHAR(20),
                      due_date TIMESTAMP NOT NULL,
                      user_id BIGINT,
                      FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE SET NULL,
                      CONSTRAINT chk_task_status CHECK (status IN ('TODO','IN_PROGRESS', 'COMPLETED', 'PENDING')),
                      CONSTRAINT chk_task_priority CHECK (priority IN ('HIGH', 'MEDIUM', 'LOW')),
                      CONSTRAINT chk_task_title_length CHECK (CHAR_LENGTH(title) <= 100),
                      CONSTRAINT chk_task_description_length CHECK (CHAR_LENGTH(description) <= 400)
);

-- Table for Notification
CREATE TABLE IF NOT EXISTS notification (
                              id BIGINT AUTO_INCREMENT PRIMARY KEY,
                              task_id BIGINT NOT NULL,
                              message TEXT,
                              sent_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                              FOREIGN KEY (task_id) REFERENCES task (id) ON DELETE CASCADE
);

-- Table for History
CREATE TABLE IF NOT EXISTS history (
                         id BIGINT AUTO_INCREMENT PRIMARY KEY,
                         user_id BIGINT,
                         task_id BIGINT NOT NULL,
                         action VARCHAR(20) NOT NULL,
                         action_date TIMESTAMP NOT NULL,
                         FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE SET NULL,
                         FOREIGN KEY (task_id) REFERENCES task (id) ON DELETE CASCADE,
                         CONSTRAINT chk_action CHECK (action IN ('COMPLETED','IN_PROGRESS','UPDATED','CREATED'))
    );

-- Insert data into users table
INSERT INTO users (user_name, password, email, role) VALUES
                                                         ('admin_user', 'Admin@1234', 'admin@example.com', 'ADMIN'),
                                                         ('john_doe', 'John12345', 'john.doe@example.com', 'USER'),
                                                         ('jane_smith', 'Jane12345', 'jane.smith@example.com', 'USER');

-- Insert data into task table
INSERT INTO task (title, description, status, priority, due_date, user_id) VALUES
                                                                               ('Setup Server', 'Setup and configure the production server.', 'TODO', 'HIGH', '2024-12-15 14:00:00', 1),
                                                                               ('Design Mockups', 'Create UI mockups for the new application.', 'IN_PROGRESS', 'MEDIUM', '2024-12-10 10:00:00', 2),
                                                                               ('Write Documentation', 'Complete user and API documentation.', 'PENDING', 'LOW', '2024-12-20 17:00:00', 3);

-- Insert data into notification table
INSERT INTO notification (task_id, message) VALUES
                                                (1, 'Reminder: Task "Setup Server" is due soon.'),
                                                (2, 'Task "Design Mockups" is currently in progress.'),
                                                (3, 'Reminder: Task "Write Documentation" is still pending.');

-- Insert data into history table
INSERT INTO history (user_id, task_id, action, action_date) VALUES
                                                                (1, 1, 'CREATED', '2024-11-29 10:00:00'),
                                                                (2, 2, 'CREATED', '2024-11-29 10:30:00'),
                                                                (3, 3, 'CREATED', '2024-11-29 11:00:00'),
                                                                (2, 2, 'IN_PROGRESS', '2024-11-30 09:00:00');

