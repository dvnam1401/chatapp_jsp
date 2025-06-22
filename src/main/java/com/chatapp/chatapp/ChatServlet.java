package com.chatapp.chatapp;

import com.chatapp.model.Message;
import com.chatapp.model.User;
import com.chatapp.service.MessageService;
import com.chatapp.service.UserService;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebServlet("/chat")
public class ChatServlet extends HttpServlet {
    private UserService userService;
    private MessageService messageService;
    
    @Override
    public void init() throws ServletException {
        userService = new UserService();
        messageService = new MessageService();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("user");
        
        if (currentUser == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        List<User> allUsers = userService.getAllUsers();
        allUsers.removeIf(user -> user.getUserId() == currentUser.getUserId());
        
        String chatWith = request.getParameter("chatWith");
        List<Message> messages = null;
        User chatPartner = null;
        
        if (chatWith != null) {
            try {
                int chatPartnerId = Integer.parseInt(chatWith);
                chatPartner = userService.getUserById(chatPartnerId);
                
                if (chatPartner != null) {
                    messages = messageService.getConversation(currentUser.getUserId(), chatPartnerId);
                    messageService.markAllMessagesAsReadBetweenUsers(chatPartnerId, currentUser.getUserId());
                }
            } catch (NumberFormatException e) {
                System.err.println("Invalid chatWith parameter: " + chatWith);
            }
        }
        
        // Load unread message counts for each user
        request.setAttribute("messageService", messageService);
        
        request.setAttribute("allUsers", allUsers);
        request.setAttribute("messages", messages);
        request.setAttribute("chatPartner", chatPartner);
        request.setAttribute("currentUser", currentUser);
        
        request.getRequestDispatcher("/chat.jsp").forward(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("user");
        
        if (currentUser == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        String receiverIdStr = request.getParameter("receiverId");
        String messageContent = request.getParameter("message");
        
        if (receiverIdStr != null && messageContent != null) {
            try {
                int receiverId = Integer.parseInt(receiverIdStr);
                boolean success = messageService.sendMessage(currentUser.getUserId(), receiverId, messageContent);
                
                if (success) {
                    response.sendRedirect(request.getContextPath() + "/chat?chatWith=" + receiverId);
                } else {
                    request.setAttribute("error", "Không thể gửi tin nhắn");
                    doGet(request, response);
                }
            } catch (NumberFormatException e) {
                request.setAttribute("error", "ID người nhận không hợp lệ");
                doGet(request, response);
            }
        } else {
            request.setAttribute("error", "Thông tin không đầy đủ");
            doGet(request, response);
        }
    }
}