package com.chatapp.service;

import com.chatapp.dao.MessageDAO;
import com.chatapp.dao.UserDAO;
import com.chatapp.model.Message;
import com.chatapp.model.User;
import java.util.List;

public class MessageService {
    private MessageDAO messageDAO;
    private UserDAO userDAO;
    
    public MessageService() {
        this.messageDAO = new MessageDAO();
        this.userDAO = new UserDAO();
    }
    
    public boolean sendMessage(int senderId, int receiverId, String content) {
        if (content == null || content.trim().isEmpty()) {
            System.err.println("Nội dung tin nhắn không được để trống");
            return false;
        }
        
        User sender = userDAO.getUserById(senderId);
        User receiver = userDAO.getUserById(receiverId);
        
        if (sender == null) {
            System.err.println("Người gửi không tồn tại");
            return false;
        }
        
        if (receiver == null) {
            System.err.println("Người nhận không tồn tại");
            return false;
        }
        
        if (senderId == receiverId) {
            System.err.println("Không thể gửi tin nhắn cho chính mình");
            return false;
        }
        
        Message message = new Message(senderId, receiverId, content.trim());
        return messageDAO.createMessage(message);
    }
    
    public List<Message> getConversation(int user1Id, int user2Id) {
        User user1 = userDAO.getUserById(user1Id);
        User user2 = userDAO.getUserById(user2Id);
        
        if (user1 == null || user2 == null) {
            System.err.println("Một trong hai user không tồn tại");
            return null;
        }
        
        return messageDAO.getMessagesBetweenUsers(user1Id, user2Id);
    }
    
    public List<Message> getUnreadMessages(int userId) {
        User user = userDAO.getUserById(userId);
        if (user == null) {
            System.err.println("User không tồn tại");
            return null;
        }
        
        return messageDAO.getUnreadMessages(userId);
    }
    
    public boolean markMessageAsRead(int messageId) {
        return messageDAO.markMessageAsRead(messageId);
    }
    
    public boolean markAllMessagesAsReadBetweenUsers(int senderId, int receiverId) {
        User sender = userDAO.getUserById(senderId);
        User receiver = userDAO.getUserById(receiverId);
        
        if (sender == null || receiver == null) {
            System.err.println("Một trong hai user không tồn tại");
            return false;
        }
        
        return messageDAO.markAllMessagesAsRead(senderId, receiverId);
    }
    
    public List<Message> getRecentMessages(int userId, int limit) {
        User user = userDAO.getUserById(userId);
        if (user == null) {
            System.err.println("User không tồn tại");
            return null;
        }
        
        if (limit <= 0) {
            limit = 10;
        }
        
        return messageDAO.getRecentMessages(userId, limit);
    }
    
    public int getUnreadMessageCount(int userId) {
        List<Message> unreadMessages = getUnreadMessages(userId);
        return unreadMessages != null ? unreadMessages.size() : 0;
    }
    
    public int getUnreadMessageCountFromSender(int receiverId, int senderId) {
        User sender = userDAO.getUserById(senderId);
        User receiver = userDAO.getUserById(receiverId);
        
        if (sender == null || receiver == null) {
            System.err.println("Một trong hai user không tồn tại");
            return 0;
        }
        
        return messageDAO.getUnreadMessageCount(receiverId, senderId);
    }
} 