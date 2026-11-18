package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    // Thông tin kết nối SQL Server
    private static final String URL = "jdbc:sqlserver://DESKTOP-LGQGMEA\\SQLEXPRESS;"
            + "databaseName=QuanLyBanHang_DoAn;"
            + "encrypt=true;"
            + "trustServerCertificate=true;";

    // Tài khoản kết nối cơ sở dữ liệu
    private static final String USER = "app_qlbh";
    private static final String PASSWORD = "AppQLBH@2026_New";

    /**
     * Hàm thiết lập và trả về kết nối tới cơ sở dữ liệu SQL Server
     * @return Connection hoặc null nếu kết nối thất bại
     */
    public static Connection getConnection() {
        try {
            // Nạp driver SQL Server JDBC
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            
            // Trả về đối tượng kết nối
            return DriverManager.getConnection(URL, USER, PASSWORD);
            
        } catch (ClassNotFoundException e) {
            System.err.println("❌ Không tìm thấy JDBC Driver! Hãy kiểm tra lại thư viện (.jar).");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("❌ Lỗi kết nối SQL Server! Kiểm tra URL, tài khoản hoặc cấu hình TCP/IP.");
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Hàm main dùng để chạy kiểm tra trực tiếp trạng thái kết nối
     */
    public static void main(String[] args) {
        System.out.println("=== KIỂM TRA KẾT NỐI ===");
        System.out.println("USER: " + USER);
        System.out.println("URL : " + URL);
        System.out.println("------------------------");

        // Sử dụng try-with-resources để tự động đóng kết nối sau khi kiểm tra xong
        try (Connection conn = getConnection()) {
            if (conn != null) {
                System.out.println("✅ KẾT NỐI THÀNH CÔNG!");
                System.out.println("Database tên là: " + conn.getCatalog());
                System.out.println("Tài khoản SQL  : " + conn.getMetaData().getUserName());
            } else {
                System.out.println("❌ KẾT NỐI THẤT BẠI! Vui lòng kiểm tra lại cấu hình hệ thống.");
            }
        } catch (SQLException e) {
            System.err.println("❌ Đã xảy ra lỗi trong quá trình truy vấn thông tin kiểm tra!");
            e.printStackTrace();
        }
    }
}
