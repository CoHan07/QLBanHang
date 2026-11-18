
package dao;

import database.DBConnection;
import model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {

    // 1. Lay danh sach tai khoan
    public List<User> getAll() {
        List<User> danhSach = new ArrayList<>();

        String sql = "SELECT fldUser, fldPassword, fldName "
                   + "FROM tblUser "
                   + "ORDER BY fldUser";

        Connection conn = DBConnection.getConnection();

        if (conn == null) {
            return danhSach;
        }

        try (conn;
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                User user = new User();

                user.setUsername(rs.getString("fldUser"));
                user.setPassword(rs.getString("fldPassword"));
                user.setName(rs.getString("fldName"));

                danhSach.add(user);
            }

        } catch (SQLException e) {
            System.out.println("Loi lay danh sach tai khoan!");
            e.printStackTrace();
        }

        return danhSach;
    }

    // 2. Them tai khoan
    public boolean insert(User user) {
        String sql = "INSERT INTO tblUser "
                   + "(fldUser, fldPassword, fldName) "
                   + "VALUES (?, ?, ?)";

        Connection conn = DBConnection.getConnection();

        if (conn == null) {
            return false;
        }

        try (conn;
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getName());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Loi them tai khoan!");
            e.printStackTrace();
            return false;
        }
    }

    // 3. Cap nhat tai khoan
    public boolean update(User user) {
        String sql = "UPDATE tblUser "
                   + "SET fldPassword = ?, fldName = ? "
                   + "WHERE fldUser = ?";

        Connection conn = DBConnection.getConnection();

        if (conn == null) {
            return false;
        }

        try (conn;
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, user.getPassword());
            ps.setString(2, user.getName());
            ps.setString(3, user.getUsername());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Loi cap nhat tai khoan!");
            e.printStackTrace();
            return false;
        }
    }

    // 4. Xoa tai khoan
    public boolean delete(String username) {
        String sql = "DELETE FROM tblUser "
                   + "WHERE fldUser = ?";

        Connection conn = DBConnection.getConnection();

        if (conn == null) {
            return false;
        }

        try (conn;
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Loi xoa tai khoan!");
            e.printStackTrace();
            return false;
        }
    }

    // 5. Tim tai khoan theo username
    public User getByUsername(String username) {
        String sql = "SELECT fldUser, fldPassword, fldName "
                   + "FROM tblUser "
                   + "WHERE fldUser = ?";

        Connection conn = DBConnection.getConnection();

        if (conn == null) {
            return null;
        }

        try (conn;
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    User user = new User();

                    user.setUsername(rs.getString("fldUser"));
                    user.setPassword(rs.getString("fldPassword"));
                    user.setName(rs.getString("fldName"));

                    return user;
                }
            }

        } catch (SQLException e) {
            System.out.println("Loi tim tai khoan!");
            e.printStackTrace();
        }

        return null;
    }

    // 6. Kiem tra dang nhap
    public User login(String username, String password) {
        String sql = "SELECT fldUser, fldPassword, fldName "
                   + "FROM tblUser "
                   + "WHERE fldUser = ? AND fldPassword = ?";

        Connection conn = DBConnection.getConnection();

        if (conn == null) {
            return null;
        }

        try (conn;
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);
            ps.setString(2, password);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    User user = new User();

                    user.setUsername(rs.getString("fldUser"));
                    user.setPassword(rs.getString("fldPassword"));
                    user.setName(rs.getString("fldName"));

                    return user;
                }
            }

        } catch (SQLException e) {
            System.out.println("Loi kiem tra dang nhap!");
            e.printStackTrace();
        }

        return null;
    }
}
