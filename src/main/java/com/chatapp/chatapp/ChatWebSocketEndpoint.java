package com.chatapp.chatapp;

import com.chatapp.model.User;
import com.chatapp.service.MessageService;
import com.chatapp.service.UserService;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.text.SimpleDateFormat;
import java.util.Date;

@ServerEndpoint("/chat-websocket")
public class ChatWebSocketEndpoint {
    
    private static Map<Integer, Session> userSessions = new ConcurrentHashMap<>();
    private MessageService messageService = new MessageService();
    private UserService userService = new UserService();
    
    @OnOpen
    public void onOpen(Session session) {
        System.out.println("WebSocket connection opened: " + session.getId());
    }
    
    @OnMessage
    public void onMessage(String message, Session session) {
        try {
            System.out.println("Received WebSocket message: " + message);
            
            String type = SimpleJsonParser.getValue(message, "type");
            
            if ("join".equals(type)) {
                int userId = SimpleJsonParser.getIntValue(message, "userId");
                if (userId > 0) {
                    userSessions.put(userId, session);
                    System.out.println("User " + userId + " joined WebSocket");
                }
                
            } else if ("message".equals(type)) {
                String content = SimpleJsonParser.getValue(message, "content");
                int senderId = SimpleJsonParser.getIntValue(message, "senderId");
                int receiverId = SimpleJsonParser.getIntValue(message, "receiverId");
                
                if (content != null && senderId > 0 && receiverId > 0) {
                    // Save message to database
                    boolean saved = messageService.sendMessage(senderId, receiverId, content);
                    
                    if (saved) {
                        // Get sender info
                        User sender = userService.getUserById(senderId);
                        String senderName = sender != null ? sender.getDisplayName() : "Unknown";
                        
                        // Format timestamp
                        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                        String timestamp = sdf.format(new Date());
                        
                        // Create response message with proper UTF-8 handling
                        String escapedContent = content.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n").replace("\r", "\\r");
                        String escapedSenderName = senderName.replace("\\", "\\\\").replace("\"", "\\\"");
                        String responseMessage = String.format(
                            "{\"type\":\"message\",\"content\":\"%s\",\"senderId\":%d,\"senderName\":\"%s\",\"timestamp\":\"%s\"}",
                            escapedContent, senderId, escapedSenderName, timestamp
                        );
                        
                        // Send to receiver if online
                        Session receiverSession = userSessions.get(receiverId);
                        if (receiverSession != null && receiverSession.isOpen()) {
                            try {
                                receiverSession.getBasicRemote().sendText(responseMessage);
                                System.out.println("Message sent to receiver " + receiverId + ": " + responseMessage);
                            } catch (IOException e) {
                                System.err.println("Error sending message to receiver: " + e.getMessage());
                                userSessions.remove(receiverId);
                            }
                        } else {
                            System.out.println("Receiver " + receiverId + " is not online");
                        }
                        
                        // Also send confirmation back to sender
                        Session senderSession = userSessions.get(senderId);
                        if (senderSession != null && senderSession.isOpen() && !senderSession.equals(session)) {
                            try {
                                String confirmMessage = String.format(
                                    "{\"type\":\"sent_confirmation\",\"content\":\"%s\",\"receiverId\":%d,\"timestamp\":\"%s\"}",
                                    escapedContent, receiverId, timestamp
                                );
                                senderSession.getBasicRemote().sendText(confirmMessage);
                                System.out.println("Confirmation sent to sender " + senderId);
                            } catch (IOException e) {
                                System.err.println("Error sending confirmation to sender: " + e.getMessage());
                                userSessions.remove(senderId);
                            }
                        }
                    }
                }
            }
            
        } catch (Exception e) {
            System.err.println("Error processing WebSocket message: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    @OnClose
    public void onClose(Session session, CloseReason closeReason) {
        // Remove user from session map
        userSessions.entrySet().removeIf(entry -> entry.getValue().equals(session));
        System.out.println("WebSocket connection closed: " + session.getId() + ", reason: " + closeReason.getReasonPhrase());
    }
    
    @OnError
    public void onError(Session session, Throwable throwable) {
        System.err.println("WebSocket error: " + throwable.getMessage());
        throwable.printStackTrace();
        userSessions.entrySet().removeIf(entry -> entry.getValue().equals(session));
    }
} 