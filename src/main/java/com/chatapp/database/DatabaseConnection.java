package com.chatapp.database;

import com.chatapp.config.DatabaseConfig;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static DatabaseConnection instance;
    private Connection connection;
    
    private DatabaseConnection() {
        createConnection();
    }
    
    private void createConnection() {
        try {
            Class.forName(DatabaseConfig.DB_DRIVER);
            this.connection = DriverManager.getConnection(
                DatabaseConfig.DB_URL, 
                DatabaseConfig.DB_USERNAME, 
                DatabaseConfig.DB_PASSWORD
            );
            System.out.println("Database connection established successfully");
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("Lỗi kết nối database: " + e.getMessage());
            this.connection = null;
        }
    }
    
    public static DatabaseConnection getInstance() {
        if (instance == null) {
            synchronized (DatabaseConnection.class) {
                if (instance == null) {
                    instance = new DatabaseConnection();
                }
            }
        }
        return instance;
    }
    
    public Connection getConnection() {
        try {
            // Kiểm tra connection hiện tại có hợp lệ không
            if (connection != null && !connection.isClosed() && connection.isValid(5)) {
                return connection; // Tái sử dụng connection hiện tại
            }
            
            System.out.println("Connection is invalid, creating new connection...");
            
            // Tạo connection mới và lưu vào singleton
            createConnection();
            
            // Nếu singleton connection vẫn fail, tạo fresh connection không lưu
            if (connection == null) {
                System.out.println("Singleton connection failed, creating temporary connection...");
                Class.forName(DatabaseConfig.DB_DRIVER);
                return DriverManager.getConnection(
                    DatabaseConfig.DB_URL, 
                    DatabaseConfig.DB_USERNAME, 
                    DatabaseConfig.DB_PASSWORD
                );
            }
            
            return connection;
            
        } catch (SQLException | ClassNotFoundException e) {
            System.err.println("Lỗi lấy connection: " + e.getMessage());
            
            // Emergency fallback: tạo connection tạm thời
            try {
                System.out.println("Emergency fallback: creating temporary connection...");
                Class.forName(DatabaseConfig.DB_DRIVER);
                return DriverManager.getConnection(
                    DatabaseConfig.DB_URL, 
                    DatabaseConfig.DB_USERNAME, 
                    DatabaseConfig.DB_PASSWORD
                );
            } catch (ClassNotFoundException | SQLException ex) {
                System.err.println("All connection attempts failed: " + ex.getMessage());
                return null;
            }
        }
    }
    
    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Database connection closed");
            }
        } catch (SQLException e) {
            System.err.println("Lỗi đóng kết nối database: " + e.getMessage());
        }
    }
    
    public boolean testConnection() {
        Connection testConn = getConnection();
        if (testConn != null) {
            try {
                boolean isValid = testConn.isValid(5);
                System.out.println("Database connection test: " + (isValid ? "SUCCESS" : "FAILED"));
                return isValid;
            } catch (SQLException e) {
                System.err.println("Database connection test failed: " + e.getMessage());
            }
        }
        return false;
    }
} 