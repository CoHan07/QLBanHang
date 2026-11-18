
# QUẢN LÝ BÁN HÀNG

## 1. Giới thiệu
Phần mềm quản lý bán hàng được xây dựng bằng Java Swing, sử dụng SQL Server để lưu trữ dữ liệu.

## 2. Công nghệ sử dụng
- Java JDK 21
- Java Swing
- Apache NetBeans
- Microsoft SQL Server
- JDBC Driver

## 3. Cấu trúc dự án
- src/: Mã nguồn Java
- database/: Script tạo cơ sở dữ liệu SQL Server
- nbproject/: Cấu hình dự án NetBeans
- build.xml: Cấu hình biên dịch dự án

## 4. Cài đặt cơ sở dữ liệu
1. Mở SQL Server Management Studio.
2. Chạy file database/QuanLyBanHang_DoAn.sql.
3. Tạo SQL Login app_qlbh và cấp quyền đọc/ghi trên database QuanLyBanHang_DoAn.

## 5. Cấu hình kết nối
Mở file DBConnection.java trong package database.

Cấu hình lại:
- SQL Server instance
- Database name: QuanLyBanHang_DoAn
- Username: app_qlbh
- Password: Mật khẩu SQL Login đã tạo trên máy.

## 6. Chạy chương trình
1. Mở dự án bằng Apache NetBeans.
2. Kiểm tra JDBC Driver đã được thêm vào Libraries.
3. Kiểm tra kết nối SQL Server.
4. Chạy chương trình từ NetBeans.

## 7. Tài khoản demo
- Username: admin
- Password: 123
