<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Đăng ký - Chat App</title>
    <link href="https://cdnjs.cloudflare.com/ajax/libs/mdb-ui-kit/6.4.2/mdb.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    <style>
        .gradient-custom-3 {
            background: #84fab0;
            background: -webkit-linear-gradient(to right, rgba(132, 250, 176, 0.5), rgba(143, 211, 244, 0.5));
            background: linear-gradient(to right, rgba(132, 250, 176, 0.5), rgba(143, 211, 244, 0.5));
        }
        .gradient-custom-4 {
            background: #84fab0;
            background: -webkit-linear-gradient(to right, rgba(132, 250, 176, 1), rgba(143, 211, 244, 1));
            background: linear-gradient(to right, rgba(132, 250, 176, 1), rgba(143, 211, 244, 1));
        }
    </style>
</head>
<body>
    <section class="vh-100 bg-image" style="background-image: url('https://mdbcdn.b-cdn.net/img/Photos/new-templates/search-box/img4.webp');">
        <div class="mask d-flex align-items-center h-100 gradient-custom-3">
            <div class="container h-100">
                <div class="row d-flex justify-content-center align-items-center h-100">
                    <div class="col-12 col-md-9 col-lg-7 col-xl-6">
                        <div class="card" style="border-radius: 15px;">
                            <div class="card-body p-5">
                                <h2 class="text-uppercase text-center mb-5"><i class="fas fa-user-plus me-2"></i>Tạo tài khoản</h2>

                                <% if (request.getAttribute("error") != null) { %>
                                    <div class="alert alert-danger" role="alert">
                                        <i class="fas fa-exclamation-triangle me-2"></i><%= request.getAttribute("error") %>
                                    </div>
                                <% } %>

                                <form method="post" action="register" id="registerForm">
                                    <div data-mdb-input-init class="form-outline mb-4">
                                        <input type="text" id="username" name="username" class="form-control form-control-lg" required />
                                        <label class="form-label" for="username">Tên đăng nhập</label>
                                    </div>

                                    <div data-mdb-input-init class="form-outline mb-4">
                                        <input type="text" id="displayName" name="displayName" class="form-control form-control-lg" required />
                                        <label class="form-label" for="displayName">Tên hiển thị</label>
                                    </div>

                                    <div data-mdb-input-init class="form-outline mb-4">
                                        <input type="password" id="password" name="password" class="form-control form-control-lg" required minlength="6" />
                                        <label class="form-label" for="password">Mật khẩu</label>
                                    </div>

                                    <div data-mdb-input-init class="form-outline mb-4">
                                        <input type="password" id="confirmPassword" name="confirmPassword" class="form-control form-control-lg" required minlength="6" />
                                        <label class="form-label" for="confirmPassword">Nhập lại mật khẩu</label>
                                    </div>

                                    <div class="form-check d-flex justify-content-center mb-5">
                                        <input class="form-check-input me-2" type="checkbox" value="" id="agreeTerms" required />
                                        <label class="form-check-label" for="agreeTerms">
                                            Tôi đồng ý với <a href="#!" class="text-body"><u>Điều khoản sử dụng</u></a>
                                        </label>
                                    </div>

                                    <div class="d-flex justify-content-center">
                                        <button type="submit" data-mdb-button-init data-mdb-ripple-init class="btn btn-success btn-block btn-lg gradient-custom-4 text-body">
                                            <i class="fas fa-user-plus me-2"></i>Đăng ký
                                        </button>
                                    </div>

                                    <p class="text-center text-muted mt-5 mb-0">Đã có tài khoản? 
                                        <a href="login" class="fw-bold text-body"><u>Đăng nhập tại đây</u></a>
                                    </p>
                                </form>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </section>
    
    <script src="https://cdnjs.cloudflare.com/ajax/libs/mdb-ui-kit/6.4.2/mdb.min.js"></script>
    <script>
        document.getElementById('registerForm').addEventListener('submit', function(e) {
            const password = document.getElementById('password').value;
            const confirmPassword = document.getElementById('confirmPassword').value;
            
            if (password !== confirmPassword) {
                e.preventDefault();
                alert('Mật khẩu xác nhận không khớp!');
                return false;
            }
            
            if (password.length < 6) {
                e.preventDefault();
                alert('Mật khẩu phải có ít nhất 6 ký tự!');
                return false;
            }
            
            const agreeTerms = document.getElementById('agreeTerms').checked;
            if (!agreeTerms) {
                e.preventDefault();
                alert('Vui lòng đồng ý với điều khoản sử dụng!');
                return false;
            }
        });
    </script>
</body>
</html> 