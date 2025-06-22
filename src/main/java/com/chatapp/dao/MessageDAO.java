package com.chatapp.dao;

import com.chatapp.database.DatabaseConnection;
import com.chatapp.model.Message;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MessageDAO {
    private DatabaseConnection dbConnection;
    
    public MessageDAO() {
        this.dbConnection = DatabaseConnection.getInstance();
    }
    
    public boolean createMessage(Message message) {
        String sql = "INSERT INTO messages (sender_id, receiver_id, content) VALUES (?, ?, ?)";
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = dbConnection.getConnection();
            if (conn == null) {
                System.err.println("Failed to get database connection for createMessage");
                return false;
            }
            
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, message.getSenderId());
            stmt.setInt(2, message.getReceiverId());
            stmt.setString(3, message.getContent());
            
            int result = stmt.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi tạo message: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            closeResources(stmt, null);
        }
    }
    
    public List<Message> getMessagesBetweenUsers(int user1Id, int user2Id) {
        List<Message> messages = new ArrayList<>();
        String sql = "SELECT * FROM messages WHERE " +
                    "(sender_id = ? AND receiver_id = ?) OR " +
                    "(sender_id = ? AND receiver_id = ?) " +
                    "ORDER BY timestamp ASC";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = dbConnection.getConnection();
            if (conn == null) {
                System.err.println("Failed to get database connection for getMessagesBetweenUsers");
                return messages;
            }
            
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, user1Id);
            stmt.setInt(2, user2Id);
            stmt.setInt(3, user2Id);
            stmt.setInt(4, user1Id);
            
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                Message message = new Message();
                message.setMessageId(rs.getInt("message_id"));
                message.setSenderId(rs.getInt("sender_id"));
                message.setReceiverId(rs.getInt("receiver_id"));
                message.setContent(rs.getString("content"));
                message.setTimestamp(rs.getTimestamp("timestamp"));
                message.setRead(rs.getBoolean("is_read"));
                messages.add(message);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi lấy messages: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(stmt, rs);
        }
        return messages;
    }
    
    public List<Message> getUnreadMessages(int receiverId) {
        List<Message> messages = new ArrayList<>();
        String sql = "SELECT * FROM messages WHERE receiver_id = ? AND is_read = FALSE ORDER BY timestamp DESC";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = dbConnection.getConnection();
            if (conn == null) {
                System.err.println("Failed to get database connection for getUnreadMessages");
                return messages;
            }
            
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, receiverId);
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                Message message = new Message();
                message.setMessageId(rs.getInt("message_id"));
                message.setSenderId(rs.getInt("sender_id"));
                message.setReceiverId(rs.getInt("receiver_id"));
                message.setContent(rs.getString("content"));
                message.setTimestamp(rs.getTimestamp("timestamp"));
                message.setRead(rs.getBoolean("is_read"));
                messages.add(message);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi lấy unread messages: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(stmt, rs);
        }
        return messages;
    }
    
    public boolean markMessageAsRead(int messageId) {
        String sql = "UPDATE messages SET is_read = TRUE WHERE message_id = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = dbConnection.getConnection();
            if (conn == null) {
                System.err.println("Failed to get database connection for markMessageAsRead");
                return false;
            }
            
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, messageId);
            int result = stmt.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi mark message as read: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            closeResources(stmt, null);
        }
    }
    
    public boolean markAllMessagesAsRead(int senderId, int receiverId) {
        String sql = "UPDATE messages SET is_read = TRUE WHERE sender_id = ? AND receiver_id = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = dbConnection.getConnection();
            if (conn == null) {
                System.err.println("Failed to get database connection for markAllMessagesAsRead");
                return false;
            }
            
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, senderId);
            stmt.setInt(2, receiverId);
            int result = stmt.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi mark all messages as read: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            closeResources(stmt, null);
        }
    }
    
    public List<Message> getRecentMessages(int userId, int limit) {
        List<Message> messages = new ArrayList<>();
        String sql = "SELECT * FROM messages WHERE sender_id = ? OR receiver_id = ? " +
                    "ORDER BY timestamp DESC LIMIT ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = dbConnection.getConnection();
            if (conn == null) {
                System.err.println("Failed to get database connection for getRecentMessages");
                return messages;
            }
            
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userId);
            stmt.setInt(2, userId);
            stmt.setInt(3, limit);
            
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                Message message = new Message();
                message.setMessageId(rs.getInt("message_id"));
                message.setSenderId(rs.getInt("sender_id"));
                message.setReceiverId(rs.getInt("receiver_id"));
                message.setContent(rs.getString("content"));
                message.setTimestamp(rs.getTimestamp("timestamp"));
                message.setRead(rs.getBoolean("is_read"));
                messages.add(message);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi lấy recent messages: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(stmt, rs);
        }
        return messages;
    }
    
    public int getUnreadMessageCount(int receiverId, int senderId) {
        String sql = "SELECT COUNT(*) as count FROM messages WHERE receiver_id = ? AND sender_id = ? AND is_read = FALSE";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = dbConnection.getConnection();
            if (conn == null) {
                System.err.println("Failed to get database connection for getUnreadMessageCount");
                return 0;
            }
            
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, receiverId);
            stmt.setInt(2, senderId);
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt("count");
            }
        } catch (SQLException e) {
            System.err.println("Lỗi đếm unread messages: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(stmt, rs);
        }
        return 0;
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