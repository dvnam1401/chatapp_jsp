package com.chatapp.dao;

import com.chatapp.database.DatabaseConnection;
import com.chatapp.model.User;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {
    private DatabaseConnection dbConnection;
    
    public UserDAO() {
        this.dbConnection = DatabaseConnection.getInstance();
    }
    
    public boolean createUser(User user) {
        String sql = "INSERT INTO users (username, password_hash, display_name) VALUES (?, ?, ?)";
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = dbConnection.getConnection();
            if (conn == null) {
                System.err.println("Failed to get database connection for createUser");
                return false;
            }
            
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPasswordHash());
            stmt.setString(3, user.getDisplayName());
            
            int result = stmt.executeUpdate();
            System.out.println("User created successfully: " + user.getUsername());
            return result > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi tạo user: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            closeResources(stmt, null);
        }
    }
    
    public User getUserByUsername(String username) {
        String sql = "SELECT * FROM users WHERE username = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = dbConnection.getConnection();
            if (conn == null) {
                System.err.println("Failed to get database connection for getUserByUsername");
                return null;
            }
            
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                User user = new User();
                user.setUserId(rs.getInt("user_id"));
                user.setUsername(rs.getString("username"));
                user.setPasswordHash(rs.getString("password_hash"));
                user.setDisplayName(rs.getString("display_name"));
                user.setCreatedAt(rs.getTimestamp("created_at"));
                user.setLastLogin(rs.getTimestamp("last_login"));
                System.out.println("User found: " + username);
                return user;
            }
            System.out.println("User not found: " + username);
        } catch (SQLException e) {
            System.err.println("Lỗi lấy user theo username: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(stmt, rs);
        }
        return null;
    }
    
    public User getUserById(int userId) {
        String sql = "SELECT * FROM users WHERE user_id = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = dbConnection.getConnection();
            if (conn == null) {
                System.err.println("Failed to get database connection for getUserById");
                return null;
            }
            
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userId);
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                User user = new User();
                user.setUserId(rs.getInt("user_id"));
                user.setUsername(rs.getString("username"));
                user.setPasswordHash(rs.getString("password_hash"));
                user.setDisplayName(rs.getString("display_name"));
                user.setCreatedAt(rs.getTimestamp("created_at"));
                user.setLastLogin(rs.getTimestamp("last_login"));
                return user;
            }
        } catch (SQLException e) {
            System.err.println("Lỗi lấy user theo ID: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(stmt, rs);
        }
        return null;
    }
    
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users ORDER BY username";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = dbConnection.getConnection();
            if (conn == null) {
                System.err.println("Failed to get database connection for getAllUsers");
                return users;
            }
            
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                User user = new User();
                user.setUserId(rs.getInt("user_id"));
                user.setUsername(rs.getString("username"));
                user.setPasswordHash(rs.getString("password_hash"));
                user.setDisplayName(rs.getString("display_name"));
                user.setCreatedAt(rs.getTimestamp("created_at"));
                user.setLastLogin(rs.getTimestamp("last_login"));
                users.add(user);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi lấy danh sách users: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(stmt, rs);
        }
        return users;
    }
    
    public boolean updateLastLogin(int userId) {
        String sql = "UPDATE users SET last_login = CURRENT_TIMESTAMP WHERE user_id = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = dbConnection.getConnection();
            if (conn == null) {
                System.err.println("Failed to get database connection for updateLastLogin");
                return false;
            }
            
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userId);
            int result = stmt.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi cập nhật last login: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            closeResources(stmt, null);
        }
    }
    
    private void closeResources(PreparedStatement stmt, ResultSet rs) {
        try {
            if (rs != null) {
                rs.close();
            }
            if (stmt != null) {
                stmt.close();
            }
        } catch (SQLException e) {
            System.err.println("Lỗi đóng resources: " + e.getMessage());
        }
    }
} 