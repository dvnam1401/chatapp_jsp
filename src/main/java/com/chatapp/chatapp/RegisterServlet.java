package com.chatapp.chatapp;

import com.chatapp.service.UserService;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    
    private UserService userService;
    
    @Override
    public void init() throws ServletException {
        super.init();
        this.userService = new UserService();
        System.out.println("RegisterServlet initialized");
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        request.getRequestDispatcher("/register.jsp").forward(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");
        String displayName = request.getParameter("displayName");
        
        System.out.println("Register attempt for username: " + username);
        
        if (username == null || password == null || confirmPassword == null || displayName == null ||
            username.trim().isEmpty() || password.trim().isEmpty() || 
            confirmPassword.trim().isEmpty() || displayName.trim().isEmpty()) {
            System.out.println("Invalid registration data provided");
            request.setAttribute("error", "Vui lòng nhập đầy đủ thông tin");
            request.getRequestDispatcher("/register.jsp").forward(request, response);
            return;
        }
        
        if (!password.equals(confirmPassword)) {
            System.out.println("Password confirmation mismatch for user: " + username);
            request.setAttribute("error", "Mật khẩu xác nhận không khớp");
            request.getRequestDispatcher("/register.jsp").forward(request, response);
            return;
        }
        
        try {
            boolean success = userService.registerUser(username, password, displayName);
            
            if (success) {
                System.out.println("Registration successful for user: " + username);
                request.setAttribute("success", "Đăng ký thành công! Vui lòng đăng nhập.");
                request.getRequestDispatcher("/login.jsp").forward(request, response);
            } else {
                System.out.println("Registration failed for user: " + username);
                request.setAttribute("error", "Tên đăng nhập đã tồn tại hoặc có lỗi xảy ra");
                request.getRequestDispatcher("/register.jsp").forward(request, response);
            }
        } catch (Exception e) {
            System.err.println("Exception during registration for user " + username + ": " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("error", "Lỗi hệ thống: " + e.getMessage());
            request.getRequestDispatcher("/register.jsp").forward(request, response);
        }
    }
} 