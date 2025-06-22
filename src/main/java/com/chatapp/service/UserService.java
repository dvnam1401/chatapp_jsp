package com.chatapp.service;

import com.chatapp.dao.UserDAO;
import com.chatapp.model.User;
import org.mindrot.jbcrypt.BCrypt;
import java.util.List;

public class UserService {
    private UserDAO userDAO;
    
    public UserService() {
        this.userDAO = new UserDAO();
    }
    
    public boolean registerUser(String username, String password, String displayName) {
        if (username == null || username.trim().isEmpty()) {
            System.err.println("Username không được để trống");
            return false;
        }
        
        if (password == null || password.length() < 6) {
            System.err.println("Password phải có ít nhất 6 ký tự");
            return false;
        }
        
        if (displayName == null || displayName.trim().isEmpty()) {
            System.err.println("Display name không được để trống");
            return false;
        }
        
        User existingUser = userDAO.getUserByUsername(username);
        if (existingUser != null) {
            System.err.println("Username đã tồn tại");
            return false;
        }
        
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        User newUser = new User(username, hashedPassword, displayName);
        
        return userDAO.createUser(newUser);
    }
    
    public User loginUser(String username, String password) {
        if (username == null || username.trim().isEmpty()) {
            System.err.println("Username không được để trống");
            return null;
        }
        
        if (password == null || password.trim().isEmpty()) {
            System.err.println("Password không được để trống");
            return null;
        }
        
        User user = userDAO.getUserByUsername(username);
        if (user == null) {
            System.err.println("User không tồn tại");
            return null;
        }
        
        if (BCrypt.checkpw(password, user.getPasswordHash())) {
            userDAO.updateLastLogin(user.getUserId());
            return user;
        } else {
            System.err.println("Password không đúng");
            return null;
        }
    }
    
    public User getUserById(int userId) {
        return userDAO.getUserById(userId);
    }
    
    public User getUserByUsername(String username) {
        return userDAO.getUserByUsername(username);
    }
    
    public List<User> getAllUsers() {
        return userDAO.getAllUsers();
    }
    
    public boolean updateLastLogin(int userId) {
        return userDAO.updateLastLogin(userId);
    }
    
    public boolean isValidUser(int userId) {
        User user = userDAO.getUserById(userId);
        return user != null;
    }
} 