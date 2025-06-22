-- Tạo database với charset UTF-8
CREATE DATABASE IF NOT EXISTS chatapp_db 
CHARACTER SET utf8mb4 
COLLATE utf8mb4_unicode_ci;

USE chatapp_db;

-- Tạo bảng users với charset UTF-8
CREATE TABLE IF NOT EXISTS users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    display_name VARCHAR(100) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    last_login TIMESTAMP NULL
) ENGINE=InnoDB 
CHARACTER SET utf8mb4 
COLLATE utf8mb4_unicode_ci;

-- Tạo bảng messages với charset UTF-8  
CREATE TABLE IF NOT EXISTS messages (
    message_id INT AUTO_INCREMENT PRIMARY KEY,
    sender_id INT NOT NULL,
    receiver_id INT NOT NULL,
    content TEXT NOT NULL,
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    is_read BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (sender_id) REFERENCES users(user_id),
    FOREIGN KEY (receiver_id) REFERENCES users(user_id)
) ENGINE=InnoDB 
CHARACTER SET utf8mb4 
COLLATE utf8mb4_unicode_ci;

-- Tạo bảng chat_sessions (tùy chọn) với charset UTF-8
CREATE TABLE IF NOT EXISTS chat_sessions (
    session_id INT AUTO_INCREMENT PRIMARY KEY,
    user1_id INT NOT NULL,
    user2_id INT NOT NULL,
    started_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    last_activity TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user1_id) REFERENCES users(user_id),
    FOREIGN KEY (user2_id) REFERENCES users(user_id),
    CONSTRAINT unique_users UNIQUE (user1_id, user2_id)
) ENGINE=InnoDB 
CHARACTER SET utf8mb4 
COLLATE utf8mb4_unicode_ci;

-- Thêm index để tối ưu hiệu suất
CREATE INDEX idx_messages_sender_receiver ON messages (sender_id, receiver_id);
CREATE INDEX idx_messages_timestamp ON messages (timestamp);
CREATE INDEX idx_users_username ON users (username);

-- Dữ liệu mẫu với tiếng Việt
-- INSERT INTO users (username, password_hash, display_name) VALUES 
-- ('user1', '$2a$10$example', 'Đặng Nam'),
-- ('user2', '$2a$10$example', 'Nguyễn Văn A'),
-- ('user3', '$2a$10$example', 'Trần Thị B')
-- ON DUPLICATE KEY UPDATE display_name = VALUES(display_name);

-- -- Chèn tin nhắn mẫu
-- INSERT INTO messages (sender_id, receiver_id, content) VALUES
-- (1, 2, 'Chào bạn!'),
-- (2, 1, 'Xin chào! Bạn khỏe không?'),
-- (1, 2, 'Tôi khỏe, cảm ơn bạn.'),
-- (1, 3, 'Hello user2!'),
-- (3, 1, 'Hi admin!'); 