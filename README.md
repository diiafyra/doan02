Mô phỏng web cho đồ án nghiên cứu bảo mật cho cổng thông tin điện tử.
Luồng cơ bản:
  1.	Người đọc truy cập website thông qua trình duyệt
  2.	Website tải dữ liệu (bao gồm bài báo, bình luận, thông tin user (đã đăng nhập)) từ SQL Server để hiển thị
  3.	Người dùng có thể đăng nhập và bình luận, dữ liệu bình luận gửi qua backend Java và lưu vào SQL Server
  (Quản trị viên sử dụng SharePoint để đăng bài, hệ thống ghi nhận thông tin và lưu vào SQL Server. Nginx đóng vai trò xử lý request và tối ưu phân phối nội dung)

Sử dụng để mô phỏng:
SQL Injeciton
  - Tấn công vào backend và database.
  - Nhập các đoạn SQL độc vào các trường nhập:
    + Ô đăng nhập.
    + Trường nhập bình luận.
XSS
  - Tấn công vào giao diện frontend.
  - Nhúng mã js độc vào trường bình luận.
Brute Force
  - Tấn công vào hệ thống xác thực của backend.
  - Sử dụng công cụ thử mật khẩu vào hệ thống đăng nhập 


