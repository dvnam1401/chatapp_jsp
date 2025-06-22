<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.chatapp.model.User" %>
<%@ page import="com.chatapp.model.Message" %>
<%@ page import="com.chatapp.service.MessageService" %>
<%@ page import="java.util.List" %>
<%@ page import="java.text.SimpleDateFormat" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Chat App</title>
    <link href="https://cdnjs.cloudflare.com/ajax/libs/mdb-ui-kit/6.4.2/mdb.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    <style>
        #chat2 .form-control {
            border-color: transparent;
        }

        #chat2 .form-control:focus {
            border-color: transparent;
            box-shadow: inset 0px 0px 0px 1px transparent;
        }

        .divider:after,
        .divider:before {
            content: "";
            flex: 1;
            height: 1px;
            background: #eee;
        }
        
        /* Modern Chat App Styling */
        body, html {
            height: 100%;
            overflow: hidden;
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
        }
        
        .container-fluid {
            height: 100vh;
            overflow: hidden;
            padding: 0;
        }
        
        .row {
            height: 100%;
            margin: 0;
        }
        
        /* Left sidebar - user list */
        .col-md-3 {
            height: 100vh;
            overflow-y: auto;
            background: linear-gradient(180deg, #f8f9fa 0%, #e9ecef 100%);
            border-right: 3px solid #dee2e6;
            box-shadow: 2px 0 10px rgba(0,0,0,0.1);
        }
        
        /* Right chat area */
        .col-md-9 {
            height: 100vh;
            display: flex;
            flex-direction: column;
            background: #ffffff;
        }
        
        /* Chat header styling */
        .card-header {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%) !important;
            color: white !important;
            border: none !important;
            box-shadow: 0 2px 10px rgba(0,0,0,0.1);
        }
        
        .card-header h5 {
            color: white !important;
        }
        
        .card-header .text-muted {
            color: rgba(255,255,255,0.8) !important;
        }
        
        /* Chat messages container */
        #chatMessages {
            flex: 1;
            overflow-y: auto !important;
            height: 0;
            padding: 20px;
            display: block !important;
            background: linear-gradient(180deg, #f8f9fa 0%, #ffffff 100%);
        }
        
        /* Message bubbles styling */
        #chatMessages .bg-primary {
            background: linear-gradient(135deg, #66bb6a 0%, #43a047 100%) !important;
            border: none !important;
            box-shadow: 0 2px 8px rgba(102, 187, 106, 0.4);
        }
        
        #chatMessages .bg-body-tertiary {
            background: linear-gradient(135deg, #e9ecef 0%, #f8f9fa 100%) !important;
            color: #495057 !important;
            border: 1px solid #dee2e6;
            box-shadow: 0 2px 8px rgba(0,0,0,0.1);
        }
        
        /* Avatar styling */
        .rounded-circle {
            box-shadow: 0 2px 8px rgba(0,0,0,0.2) !important;
            border: 2px solid #ffffff !important;
        }
        
        /* Chat card adjustments */
        .card {
            height: 100%;
            display: flex;
            flex-direction: column;
            border: none !important;
            border-radius: 0 !important;
        }
        
        .card-body {
            flex: 1;
            overflow: hidden;
            padding: 0;
        }
        
        /* Input area styling */
        .card-footer {
            flex-shrink: 0;
            background: linear-gradient(135deg, #f8f9fa 0%, #e9ecef 100%) !important;
            border-top: 2px solid #dee2e6 !important;
            padding: 15px 20px !important;
        }
        
        #messageInput {
            border-radius: 25px !important;
            border: 2px solid #dee2e6 !important;
            padding: 12px 20px !important;
            background: #ffffff !important;
            box-shadow: inset 0 2px 4px rgba(0,0,0,0.1);
        }
        
        #messageInput:focus {
            border-color: #667eea !important;
            box-shadow: 0 0 0 0.2rem rgba(102, 126, 234, 0.25) !important;
        }
        
        #sendButton {
            background: linear-gradient(135deg, #66bb6a 0%, #43a047 100%) !important;
            color: white !important;
            border-radius: 50% !important;
            width: 45px !important;
            height: 45px !important;
            border: none !important;
            box-shadow: 0 2px 8px rgba(102, 187, 106, 0.4) !important;
            transition: all 0.3s ease !important;
        }
        
        #sendButton:hover {
            transform: scale(1.1) !important;
            box-shadow: 0 4px 12px rgba(102, 187, 106, 0.6) !important;
        }
        
        /* User list styling */
        .user-item {
            background: white !important;
            border: 1px solid #dee2e6 !important;
            border-radius: 10px !important;
            transition: all 0.3s ease !important;
            cursor: pointer !important;
            box-shadow: 0 2px 4px rgba(0,0,0,0.1) !important;
        }
        
        .user-item:hover {
            transform: translateY(-2px) !important;
            box-shadow: 0 4px 12px rgba(0,0,0,0.15) !important;
            border-color: #667eea !important;
        }
        
        .user-item.active {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%) !important;
            color: white !important;
            border-color: #667eea !important;
        }
        
        .user-item.active .text-muted {
            color: rgba(255,255,255,0.8) !important;
        }
        
        /* Scrollbar styling */
        ::-webkit-scrollbar {
            width: 8px;
        }
        
        ::-webkit-scrollbar-track {
            background: #f1f1f1;
            border-radius: 10px;
        }
        
        ::-webkit-scrollbar-thumb {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            border-radius: 10px;
        }
        
        ::-webkit-scrollbar-thumb:hover {
            background: linear-gradient(135deg, #764ba2 0%, #667eea 100%);
        }
        
        /* Welcome screen styling */
        .vh-100 {
            background: linear-gradient(135deg, #f8f9fa 0%, #ffffff 100%) !important;
        }
        
        /* Animation for new messages */
        @keyframes slideInRight {
            from { transform: translateX(100%); opacity: 0; }
            to { transform: translateX(0); opacity: 1; }
        }
        
        @keyframes slideInLeft {
            from { transform: translateX(-100%); opacity: 0; }
            to { transform: translateX(0); opacity: 1; }
        }
        
        /* Timestamps styling */
        .small.text-muted {
            font-size: 0.75rem !important;
            opacity: 0.7 !important;
        }
        
        #chatMessages .d-flex {
            display: flex !important;
            visibility: visible !important;
            opacity: 1 !important;
        }
        
        #chatMessages p {
            display: block !important;
            visibility: visible !important;
            opacity: 1 !important;
            color: inherit !important;
        }
        
        #chatMessages .small {
            font-size: 0.875em !important;
            display: block !important;
        }
        
        #chatMessages .rounded-circle {
            display: flex !important;
            visibility: visible !important;
            opacity: 1 !important;
        }

        .user-list {
            max-height: 400px;
            overflow-y: auto;
        }
        
        .user-item {
            cursor: pointer;
            transition: background-color 0.2s;
        }
        
        .user-item:hover {
            background-color: #f8f9fa;
        }
        
        .user-item.active {
            background-color: #007bff;
            color: white;
        }
        
        .unread-badge {
            background-color: #dc3545;
            color: white;
            border-radius: 50%;
            font-size: 12px;
            font-weight: bold;
            min-width: 18px;
            height: 18px;
            display: flex;
            align-items: center;
            justify-content: center;
            margin-left: auto;
        }
        
        .user-item.has-unread {
            font-weight: bold;
        }
    </style>
</head>
<body>
    <%
        User currentUser = (User) request.getAttribute("currentUser");
        User chatPartner = (User) request.getAttribute("chatPartner");
        List<User> allUsers = (List<User>) request.getAttribute("allUsers");
        List<Message> messages = (List<Message>) request.getAttribute("messages");
        MessageService messageService = (MessageService) request.getAttribute("messageService");
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
    %>
    
    <div class="container-fluid">
        <div class="row">
            <!-- Sidebar danh sách người dùng -->
            <div class="col-md-3 bg-light p-3">
                <div class="d-flex justify-content-between align-items-center mb-3" style="background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); padding: 15px; border-radius: 10px; color: white; box-shadow: 0 4px 12px rgba(102, 126, 234, 0.3);">
                    <h5 class="mb-0" style="color: white !important;"><i class="fas fa-comments me-2"></i>Chat App</h5>
                    <a href="logout" class="btn btn-sm btn-light" data-mdb-button-init data-mdb-ripple-init style="border-radius: 20px; box-shadow: 0 2px 6px rgba(0,0,0,0.2);">
                        <i class="fas fa-sign-out-alt"></i> Đăng xuất
                    </a>
                </div>
                
                <div class="mb-3">
                    <div class="d-flex align-items-center">
                        <div class="rounded-circle bg-success text-white d-flex align-items-center justify-content-center me-2" 
                             style="width: 35px; height: 35px;">
                            <%= currentUser.getDisplayName().charAt(0) %>
                        </div>
                        <div>
                            <strong><%= currentUser.getDisplayName() %></strong>
                            <br><small class="text-muted">@<%= currentUser.getUsername() %></small>
                        </div>
                    </div>
                </div>
                
                <hr>
                
                <h6>Danh sách người dùng</h6>
                <div class="user-list">
                    <% if (allUsers != null && !allUsers.isEmpty()) { %>
                        <% for (User user : allUsers) { %>
                            <div class="user-item p-2 rounded mb-2 <%= (chatPartner != null && chatPartner.getUserId() == user.getUserId()) ? "active" : "" %>" 
                                 onclick="startChat(<%= user.getUserId() %>)" data-user-id="<%= user.getUserId() %>">
                                <div class="d-flex align-items-center">
                                    <div class="rounded-circle bg-primary text-white d-flex align-items-center justify-content-center" 
                                         style="width: 35px; height: 35px;">
                                        <%= user.getDisplayName().charAt(0) %>
                                    </div>
                                    <div class="ms-2 flex-grow-1">
                                        <div class="fw-bold"><%= user.getDisplayName() %></div>
                                        <small class="text-muted">@<%= user.getUsername() %></small>
                                    </div>
                                    <div class="unread-badge d-none" id="unread-<%= user.getUserId() %>">0</div>
                                </div>
                            </div>
                        <% } %>
                    <% } else { %>
                        <p class="text-muted">Không có người dùng khác</p>
                    <% } %>
                </div>
            </div>
            
            <!-- Khu vực chat -->
            <div class="col-md-9 p-0">
                <% if (chatPartner != null) { %>
                    <div class="card" id="chat2">
                        <div class="card-header d-flex justify-content-between align-items-center p-3">
                            <div class="d-flex align-items-center">
                                <div class="rounded-circle bg-primary text-white d-flex align-items-center justify-content-center me-3" 
                                     style="width: 45px; height: 45px;">
                                    <%= chatPartner.getDisplayName().charAt(0) %>
                                </div>
                                <div>
                                    <h5 class="mb-0"><%= chatPartner.getDisplayName() %></h5>
                                    <small class="text-muted">@<%= chatPartner.getUsername() %></small>
                                </div>
                            </div>
                            <button type="button" data-mdb-button-init data-mdb-ripple-init class="btn btn-primary btn-sm" data-mdb-ripple-color="dark">
                                <i class="fas fa-video me-1"></i>Video Call
                            </button>
                        </div>
                        <div class="card-body" id="chatMessages">
                                            <% if (messages != null && !messages.isEmpty()) { %>
                                                <% for (Message message : messages) { %>
                                                    <% if (message.getSenderId() == currentUser.getUserId()) { %>
                                                        <!-- Tin nhắn gửi đi -->
                                                        <div class="d-flex flex-row justify-content-end mb-4 pt-1">
                                                            <div>
                                                                <p class="small p-2 me-3 mb-1 text-white rounded-3 bg-primary"><%= message.getContent() %></p>
                                                                <p class="small me-3 mb-3 rounded-3 text-muted d-flex justify-content-end"><%= sdf.format(message.getTimestamp()) %></p>
                                                            </div>
                                                            <div class="rounded-circle bg-success text-white d-flex align-items-center justify-content-center" 
                                                                 style="width: 45px; height: 45px;">
                                                                <%= currentUser.getDisplayName().charAt(0) %>
                                                            </div>
                                                        </div>
                                                    <% } else { %>
                                                        <!-- Tin nhắn nhận được -->
                                                        <div class="d-flex flex-row justify-content-start mb-4">
                                                            <div class="rounded-circle bg-primary text-white d-flex align-items-center justify-content-center me-3" 
                                                                 style="width: 45px; height: 45px;">
                                                                <%= chatPartner.getDisplayName().charAt(0) %>
                                                            </div>
                                                            <div>
                                                                <p class="small p-2 ms-3 mb-1 rounded-3 bg-body-tertiary"><%= message.getContent() %></p>
                                                                <p class="small ms-3 mb-3 rounded-3 text-muted"><%= sdf.format(message.getTimestamp()) %></p>
                                                            </div>
                                                        </div>
                                                    <% } %>
                                                <% } %>
                                            <% } else { %>
                                                <div class="text-center text-muted mt-5">
                                                    <i class="fas fa-comments fa-3x mb-3"></i>
                                                    <p>Chưa có tin nhắn nào. Hãy bắt đầu cuộc trò chuyện!</p>
                                                </div>
                                            <% } %>
                        </div>
                        <div class="card-footer text-muted d-flex justify-content-start align-items-center p-3">
                            <div class="rounded-circle bg-success text-white d-flex align-items-center justify-content-center me-3" 
                                 style="width: 40px; height: 40px;">
                                <%= currentUser.getDisplayName().charAt(0) %>
                            </div>
                            <div class="d-flex w-100" id="messageForm">
                                <input type="text" class="form-control form-control-lg me-2" id="messageInput"
                                       placeholder="Nhập tin nhắn..." autocomplete="off">
                                <button type="button" class="btn btn-link text-muted" id="sendButton" data-mdb-button-init data-mdb-ripple-init>
                                    <i class="fas fa-paper-plane"></i>
                                </button>
                            </div>
                        </div>
                    </div>
                <% } else { %>
                    <!-- Màn hình chào mừng -->
                    <div class="d-flex align-items-center justify-content-center vh-100">
                        <div class="text-center">
                            <i class="fas fa-comments fa-5x text-muted mb-4"></i>
                            <h3>Chào mừng đến với Chat App</h3>
                            <p class="text-muted">Chọn một người dùng từ danh sách bên trái để bắt đầu trò chuyện</p>
                        </div>
                    </div>
                <% } %>
            </div>
        </div>
    </div>
    
    <script src="https://cdnjs.cloudflare.com/ajax/libs/mdb-ui-kit/6.4.2/mdb.min.js"></script>
    <script>
        let websocket;
        let currentReceiverId = <%= chatPartner != null ? chatPartner.getUserId() : "null" %>;
        let currentUserId = <%= currentUser.getUserId() %>;
        let unreadCounts = {};
        
        function startChat(userId) {
            // Clear unread count for this user
            clearUnreadCount(userId);
            window.location.href = 'chat?chatWith=' + userId;
        }
        
        function updateUnreadCount(userId, increment = true) {
            if (!unreadCounts[userId]) {
                unreadCounts[userId] = 0;
            }
            
            if (increment) {
                unreadCounts[userId]++;
            }
            
            const badge = document.getElementById('unread-' + userId);
            const userItem = document.querySelector('[data-user-id="' + userId + '"]');
            
            if (badge && userItem) {
                if (unreadCounts[userId] > 0) {
                    badge.textContent = unreadCounts[userId];
                    badge.classList.remove('d-none');
                    userItem.classList.add('has-unread');
                } else {
                    badge.classList.add('d-none');
                    userItem.classList.remove('has-unread');
                }
            }
        }
        
        function clearUnreadCount(userId) {
            unreadCounts[userId] = 0;
            updateUnreadCount(userId, false);
        }
        
        function scrollToBottom() {
            const chatMessages = document.getElementById('chatMessages');
            if (chatMessages) {
                chatMessages.scrollTop = chatMessages.scrollHeight;
            }
        }
        
        function connectWebSocket() {
            // Always connect WebSocket to receive messages from any user
            console.log('Attempting to connect WebSocket. CurrentReceiverId:', currentReceiverId);
            
            if (true) { // Changed from currentReceiverId check
                const protocol = window.location.protocol === 'https:' ? 'wss:' : 'ws:';
                const wsUrl = protocol + '//' + window.location.host + '/ChatApp/chat-websocket';
                
                console.log('Connecting to WebSocket:', wsUrl);
                websocket = new WebSocket(wsUrl);
                
                websocket.onopen = function() {
                    console.log('WebSocket connected successfully');
                    websocket.send(JSON.stringify({
                        type: 'join',
                        userId: currentUserId
                    }));
                    console.log('Sent join message for user:', currentUserId);
                };
                
                websocket.onmessage = function(event) {
                    console.log('Received WebSocket message:', event.data);
                    try {
                        const data = JSON.parse(event.data);
                        if (data.type === 'message') {
                            console.log('Message details - SenderId:', data.senderId, 'CurrentReceiverId:', currentReceiverId);
                            
                            // Fix logic: if currently chatting with the sender
                            if (currentReceiverId && data.senderId == currentReceiverId) {
                                console.log('Displaying message from current chat partner');
                                addMessageToChat(data.content, data.senderName, false, data.timestamp);
                                scrollToBottom();
                            } else if (currentReceiverId) {
                                console.log('Message from different user, adding to unread');
                                updateUnreadCount(data.senderId, true);
                            } else {
                                console.log('No current chat open, adding to unread');
                                updateUnreadCount(data.senderId, true);
                            }
                        }
                    } catch (e) {
                        console.error('Error parsing WebSocket message:', e);
                    }
                };
                
                websocket.onclose = function() {
                    console.log('WebSocket disconnected');
                    setTimeout(connectWebSocket, 3000);
                };
                
                websocket.onerror = function(error) {
                    console.error('WebSocket error:', error);
                };
            }
        }
        
        function addMessageToChat(content, senderName, isSent, timestamp) {
            // console.log('DEBUG: addMessageToChat called with:', { content, senderName, isSent, timestamp });
            
            const chatMessages = document.getElementById('chatMessages');
            // console.log('DEBUG: chatMessages element found:', !!chatMessages);
            
            if (!chatMessages) {
                console.error('ERROR: chatMessages element not found!');
                return;
            }
            
            // Simple escape HTML - fix the escaping issue
            function escapeHtml(text) {
                if (!text) return '';
                return String(text)
                    .replace(/&/g, '&amp;')
                    .replace(/</g, '&lt;')
                    .replace(/>/g, '&gt;')
                    .replace(/"/g, '&quot;')
                    .replace(/'/g, '&#39;');
            }
            
            const messageDiv = document.createElement('div');
            const escapedContent = escapeHtml(content);
            const escapedSenderName = escapeHtml(senderName);
            const firstChar = senderName ? senderName.charAt(0).toUpperCase() : 'U';
            
            // console.log('DEBUG: Values before template:', {
            //     content: content,
            //     escapedContent: escapedContent,
            //     senderName: senderName,
            //     firstChar: firstChar,
            //     timestamp: timestamp
            // });
            
            let htmlContent = '';
            if (isSent) {
                htmlContent = '<div class="d-flex flex-row justify-content-end mb-4 pt-1">' +
                    '<div>' +
                        '<p class="small p-2 me-3 mb-1 text-white rounded-3 bg-primary">' + escapedContent + '</p>' +
                        '<p class="small me-3 mb-3 rounded-3 text-muted d-flex justify-content-end">' + timestamp + '</p>' +
                    '</div>' +
                    '<div class="rounded-circle bg-success text-white d-flex align-items-center justify-content-center" style="width: 45px; height: 45px;">' +
                        firstChar +
                    '</div>' +
                '</div>';
            } else {
                htmlContent = '<div class="d-flex flex-row justify-content-start mb-4">' +
                    '<div class="rounded-circle bg-primary text-white d-flex align-items-center justify-content-center me-3" style="width: 45px; height: 45px;">' +
                        firstChar +
                    '</div>' +
                    '<div>' +
                        '<p class="small p-2 ms-3 mb-1 rounded-3 bg-body-tertiary">' + escapedContent + '</p>' +
                        '<p class="small ms-3 mb-3 rounded-3 text-muted">' + timestamp + '</p>' +
                    '</div>' +
                '</div>';
            }
            
            // console.log('DEBUG: Final HTML content:', htmlContent);
            messageDiv.innerHTML = htmlContent;
            
            //             // Add smooth animation for new messages
            if (isSent) {
                messageDiv.style.animation = 'slideInRight 0.3s ease-out';
            } else {
                messageDiv.style.animation = 'slideInLeft 0.3s ease-out';
            }
            
            console.log('DEBUG: About to append messageDiv to chatMessages');
            chatMessages.appendChild(messageDiv);
            
            // Force display refresh
            messageDiv.style.display = 'block';
            messageDiv.offsetHeight; // Trigger reflow
            
            // console.log('DEBUG: Message div appended successfully');
            
            // Remove the "no messages" placeholder if it exists
            const noMessagesDiv = chatMessages.querySelector('.text-center.text-muted.mt-5');
            if (noMessagesDiv) {
                console.log('DEBUG: Removing no messages placeholder');
                noMessagesDiv.remove();
            }
            
            // console.log('DEBUG: Total messages in chat now:', chatMessages.children.length);
            
            // Debug container styles
            const computedStyle = window.getComputedStyle(chatMessages);
            // console.log('DEBUG: chatMessages container styles:', {
            //     height: computedStyle.height,
            //     maxHeight: computedStyle.maxHeight,
            //     overflow: computedStyle.overflow,
            //     overflowY: computedStyle.overflowY,
            //     display: computedStyle.display,
            //     visibility: computedStyle.visibility
            // });
            
            // Debug message div styles
            const msgComputedStyle = window.getComputedStyle(messageDiv);
            // console.log('DEBUG: messageDiv styles:', {
            //     height: msgComputedStyle.height,
            //     display: msgComputedStyle.display,
            //     visibility: msgComputedStyle.visibility,
            //     opacity: msgComputedStyle.opacity
            // });
        }
        
        function sendMessage(event) {
            if (event) {
                event.preventDefault();
                event.stopPropagation();
            }
            
            const messageInput = document.getElementById('messageInput');
            if (!messageInput) return false;
            
            const message = messageInput.value.trim();
            
            if (!message) {
                return false;
            }
            
            // Check WebSocket connection
            if (!websocket || websocket.readyState !== WebSocket.OPEN) {
                console.error('WebSocket not connected');
                alert('Kết nối bị mất. Vui lòng reload trang.');
                return false;
            }
            
            if (currentReceiverId) {
                console.log('Sending message via WebSocket:', message);
                
                websocket.send(JSON.stringify({
                    type: 'message',
                    content: message,
                    receiverId: currentReceiverId,
                    senderId: currentUserId
                }));
                
                const now = new Date();
                const timestamp = now.getHours().toString().padStart(2, '0') + ':' + 
                                now.getMinutes().toString().padStart(2, '0');
                
                // Add message to chat immediately for sender
                addMessageToChat(message, '<%= currentUser != null ? currentUser.getDisplayName() : "You" %>', true, timestamp);
                messageInput.value = '';
                scrollToBottom();
                
                // console.log('Message sent and added to chat');
            }
            
            return false;
        }
        
        // Khởi tạo
        document.addEventListener('DOMContentLoaded', function() {
            scrollToBottom();
            connectWebSocket();
            
            // Xử lý nút gửi
            const sendButton = document.getElementById('sendButton');
            if (sendButton) {
                sendButton.addEventListener('click', function(e) {
                    e.preventDefault();
                    sendMessage(e);
                });
            }
            
            // Xử lý phím Enter
            const messageInput = document.getElementById('messageInput');
            if (messageInput) {
                messageInput.addEventListener('keypress', function(e) {
                    if (e.key === 'Enter' && !e.shiftKey) {
                        e.preventDefault();
                        sendMessage(e);
                    }
                });
            }
        });
        
        window.addEventListener('beforeunload', function() {
            if (websocket) {
                websocket.close();
            }
        });
    </script>
</body>
</html> 