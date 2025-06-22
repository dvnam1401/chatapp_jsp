<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Đăng nhập - Chat App</title>
    <link href="https://cdnjs.cloudflare.com/ajax/libs/mdb-ui-kit/6.4.2/mdb.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
</head>
<body class="bg-light">
    <div class="container">
        <div class="row justify-content-center">
            <div class="col-md-6 col-lg-4">
                <div class="card mt-5 shadow">
                    <div class="card-header text-center bg-primary text-white">
                        <h3><i class="fas fa-comments me-2"></i>Chat App</h3>
                    </div>
                    <div class="card-body p-5">
                        <% if (request.getAttribute("error") != null) { %>
                            <div class="alert alert-danger" role="alert">
                                <i class="fas fa-exclamation-triangle me-2"></i><%= request.getAttribute("error") %>
                            </div>
                        <% } %>
                        
                        <% if (request.getAttribute("success") != null) { %>
                            <div class="alert alert-success" role="alert">
                                <i class="fas fa-check-circle me-2"></i><%= request.getAttribute("success") %>
                            </div>
                        <% } %>
                        
                        <form method="post" action="login">
                            <div data-mdb-input-init class="form-outline mb-4">
                                <input type="text" id="username" name="username" class="form-control" required />
                                <label class="form-label" for="username">Tên đăng nhập</label>
                            </div>

                            <div data-mdb-input-init class="form-outline mb-4">
                                <input type="password" id="password" name="password" class="form-control" required />
                                <label class="form-label" for="password">Mật khẩu</label>
                            </div>

                            <div class="row mb-4">
                                <div class="col d-flex justify-content-center">
                                    <div class="form-check">
                                        <input class="form-check-input" type="checkbox" value="" id="rememberMe" />
                                        <label class="form-check-label" for="rememberMe">Ghi nhớ đăng nhập</label>
                                    </div>
                                </div>
                            </div>

                            <button type="submit" data-mdb-button-init data-mdb-ripple-init class="btn btn-primary btn-block mb-4 w-100">
                                <i class="fas fa-sign-in-alt me-2"></i>Đăng nhập
                            </button>

                            <div class="text-center">
                                <p>Chưa có tài khoản? <a href="register" class="fw-bold text-primary">Đăng ký ngay</a></p>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>
    
    <script src="https://cdnjs.cloudflare.com/ajax/libs/mdb-ui-kit/6.4.2/mdb.min.js"></script>
</body>
</html> 