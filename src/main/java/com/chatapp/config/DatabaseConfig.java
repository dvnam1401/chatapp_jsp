package com.chatapp.config;

public class DatabaseConfig {
    public static final String DB_URL = "jdbc:mysql://localhost:3306/chatapp_db?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Ho_Chi_Minh";
    public static final String DB_USERNAME = "root";
    public static final String DB_PASSWORD = "12345";
    public static final String DB_DRIVER = "com.mysql.cj.jdbc.Driver";
    
    private DatabaseConfig() {}
} 