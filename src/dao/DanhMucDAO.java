
package dao;

import database.DBConnection;
import model.DanhMuc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DanhMucDAO {

    // 1. Lay danh sach danh muc
    public List<DanhMuc> getAll() {
        List<DanhMuc> danhSach = new ArrayList<>();

        String sql = "SELECT fldMa_danh_muc, fldTen_danh_muc "
                   + "FROM tblDanhMuc "
                   + "ORDER BY fldMa_danh_muc";

        Connection conn = DBConnection.getConnection();

        if (conn == null) {
            return danhSach;
        }

        try (conn;
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                DanhMuc dm = new DanhMuc();

                dm.setMaDanhMuc(rs.getString("fldMa_danh_muc"));
                dm.setTenDanhMuc(rs.getString("fldTen_danh_muc"));

                danhSach.add(dm);
            }

        } catch (SQLException e) {
            System.out.println("Loi lay danh sach danh muc!");
            e.printStackTrace();
        }

        return danhSach;
    }

    // 2. Them danh muc
    public boolean insert(DanhMuc dm) {
        String sql = "INSERT INTO tblDanhMuc "
                   + "(fldMa_danh_muc, fldTen_danh_muc) "
                   + "VALUES (?, ?)";

        Connection conn = DBConnection.getConnection();

        if (conn == null) {
            return false;
        }

        try (conn;
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, dm.getMaDanhMuc());
            ps.setString(2, dm.getTenDanhMuc());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Loi them danh muc!");
            e.printStackTrace();
            return false;
        }
    }

    // 3. Cap nhat danh muc
    public boolean update(DanhMuc dm) {
        String sql = "UPDATE tblDanhMuc "
                   + "SET fldTen_danh_muc = ? "
                   + "WHERE fldMa_danh_muc = ?";

        Connection conn = DBConnection.getConnection();

        if (conn == null) {
            return false;
        }

        try (conn;
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, dm.getTenDanhMuc());
            ps.setString(2, dm.getMaDanhMuc());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Loi cap nhat danh muc!");
            e.printStackTrace();
            return false;
        }
    }

    // 4. Xoa danh muc
    public boolean delete(String maDanhMuc) {
        String sql = "DELETE FROM tblDanhMuc "
                   + "WHERE fldMa_danh_muc = ?";

        Connection conn = DBConnection.getConnection();

        if (conn == null) {
            return false;
        }

        try (conn;
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maDanhMuc);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Loi xoa danh muc!");
            e.printStackTrace();
            return false;
        }
    }

    // 5. Tim danh muc theo ma
    public DanhMuc getById(String maDanhMuc) {
        String sql = "SELECT fldMa_danh_muc, fldTen_danh_muc "
                   + "FROM tblDanhMuc "
                   + "WHERE fldMa_danh_muc = ?";

        Connection conn = DBConnection.getConnection();

        if (conn == null) {
            return null;
        }

        try (conn;
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maDanhMuc);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    DanhMuc dm = new DanhMuc();

                    dm.setMaDanhMuc(rs.getString("fldMa_danh_muc"));
                    dm.setTenDanhMuc(rs.getString("fldTen_danh_muc"));

                    return dm;
                }
            }

        } catch (SQLException e) {
            System.out.println("Loi tim danh muc!");
            e.printStackTrace();
        }

        return null;
    }
}
