package com.chatapp.chatapp;

import com.chatapp.model.User;
import com.chatapp.service.UserService;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    
    private UserService userService;
    
    @Override
    public void init() throws ServletException {
        super.init();
        this.userService = new UserService();
        System.out.println("LoginServlet initialized");
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("user") != null) {
            response.sendRedirect("chat");
            return;
        }
        
        request.getRequestDispatcher("/login.jsp").forward(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        
        System.out.println("Login attempt for username: " + username);
        
        if (username == null || password == null || username.trim().isEmpty() || password.trim().isEmpty()) {
            System.out.println("Invalid username or password provided");
            request.setAttribute("error", "Vui lòng nhập đầy đủ thông tin");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
            return;
        }
        
        try {
            User user = userService.loginUser(username, password);
            
            if (user != null) {
                System.out.println("Login successful for user: " + username);
                HttpSession session = request.getSession();
                session.setAttribute("user", user);
                session.setAttribute("userId", user.getUserId());
                session.setAttribute("username", user.getUsername());
                session.setAttribute("displayName", user.getDisplayName());
                response.sendRedirect("chat");
            } else {
                System.out.println("Login failed for user: " + username);
                request.setAttribute("error", "Tên đăng nhập hoặc mật khẩu không chính xác");
                request.getRequestDispatcher("/login.jsp").forward(request, response);
            }
        } catch (Exception e) {
            System.err.println("Exception during login for user " + username + ": " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("error", "Lỗi hệ thống: " + e.getMessage());
            request.getRequestDispatcher("/login.jsp").forward(request, response);
        }
    }
} 