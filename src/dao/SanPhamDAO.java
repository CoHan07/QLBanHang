
package dao;

import database.DBConnection;
import model.SanPham;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SanPhamDAO {

    // 1. Lay danh sach san pham
    public List<SanPham> getAll() {
        List<SanPham> danhSach = new ArrayList<>();

        String sql = "SELECT fldMa_SP, fldTen_SP, fldGia_ban, "
                   + "fldSo_luong_ton, fldMa_danh_muc "
                   + "FROM tblSanPham "
                   + "ORDER BY fldMa_SP";

        Connection conn = DBConnection.getConnection();

        if (conn == null) {
            return danhSach;
        }

        try (conn;
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                SanPham sp = new SanPham();

                sp.setMaSP(rs.getString("fldMa_SP"));
                sp.setTenSP(rs.getString("fldTen_SP"));
                sp.setGiaBan(rs.getDouble("fldGia_ban"));
                sp.setSoLuongTon(rs.getInt("fldSo_luong_ton"));
                sp.setMaDanhMuc(rs.getString("fldMa_danh_muc"));

                danhSach.add(sp);
            }

        } catch (SQLException e) {
            System.out.println("Loi lay danh sach san pham!");
            e.printStackTrace();
        }

        return danhSach;
    }

    // 2. Them san pham
    public boolean insert(SanPham sp) {
        String sql = "INSERT INTO tblSanPham "
                   + "(fldMa_SP, fldTen_SP, fldGia_ban, "
                   + "fldSo_luong_ton, fldMa_danh_muc) "
                   + "VALUES (?, ?, ?, ?, ?)";

        Connection conn = DBConnection.getConnection();

        if (conn == null) {
            return false;
        }

        try (conn;
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, sp.getMaSP());
            ps.setString(2, sp.getTenSP());
            ps.setDouble(3, sp.getGiaBan());
            ps.setInt(4, sp.getSoLuongTon());
            ps.setString(5, sp.getMaDanhMuc());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Loi them san pham!");
            e.printStackTrace();
            return false;
        }
    }

    // 3. Cap nhat san pham
    public boolean update(SanPham sp) {
        String sql = "UPDATE tblSanPham "
                   + "SET fldTen_SP = ?, "
                   + "fldGia_ban = ?, "
                   + "fldSo_luong_ton = ?, "
                   + "fldMa_danh_muc = ? "
                   + "WHERE fldMa_SP = ?";

        Connection conn = DBConnection.getConnection();

        if (conn == null) {
            return false;
        }

        try (conn;
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, sp.getTenSP());
            ps.setDouble(2, sp.getGiaBan());
            ps.setInt(3, sp.getSoLuongTon());
            ps.setString(4, sp.getMaDanhMuc());
            ps.setString(5, sp.getMaSP());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Loi cap nhat san pham!");
            e.printStackTrace();
            return false;
        }
    }

    // 4. Xoa san pham
    public boolean delete(String maSP) {
        String sql = "DELETE FROM tblSanPham "
                   + "WHERE fldMa_SP = ?";

        Connection conn = DBConnection.getConnection();

        if (conn == null) {
            return false;
        }

        try (conn;
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maSP);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Loi xoa san pham!");
            e.printStackTrace();
            return false;
        }
    }

    // 5. Tim san pham theo ma
    public SanPham getById(String maSP) {
        String sql = "SELECT fldMa_SP, fldTen_SP, fldGia_ban, "
                   + "fldSo_luong_ton, fldMa_danh_muc "
                   + "FROM tblSanPham "
                   + "WHERE fldMa_SP = ?";

        Connection conn = DBConnection.getConnection();

        if (conn == null) {
            return null;
        }

        try (conn;
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maSP);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    SanPham sp = new SanPham();

                    sp.setMaSP(rs.getString("fldMa_SP"));
                    sp.setTenSP(rs.getString("fldTen_SP"));
                    sp.setGiaBan(rs.getDouble("fldGia_ban"));
                    sp.setSoLuongTon(rs.getInt("fldSo_luong_ton"));
                    sp.setMaDanhMuc(rs.getString("fldMa_danh_muc"));

                    return sp;
                }
            }

        } catch (SQLException e) {
            System.out.println("Loi tim san pham!");
            e.printStackTrace();
        }

        return null;
    }

    // 6. Tim kiem san pham theo ten
    public List<SanPham> searchByName(String tenSP) {
        List<SanPham> danhSach = new ArrayList<>();

        String sql = "SELECT fldMa_SP, fldTen_SP, fldGia_ban, "
                   + "fldSo_luong_ton, fldMa_danh_muc "
                   + "FROM tblSanPham "
                   + "WHERE fldTen_SP LIKE ? "
                   + "ORDER BY fldMa_SP";

        Connection conn = DBConnection.getConnection();

        if (conn == null) {
            return danhSach;
        }

        try (conn;
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, "%" + tenSP + "%");

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    SanPham sp = new SanPham();

                    sp.setMaSP(rs.getString("fldMa_SP"));
                    sp.setTenSP(rs.getString("fldTen_SP"));
                    sp.setGiaBan(rs.getDouble("fldGia_ban"));
                    sp.setSoLuongTon(rs.getInt("fldSo_luong_ton"));
                    sp.setMaDanhMuc(rs.getString("fldMa_danh_muc"));

                    danhSach.add(sp);
                }
            }

        } catch (SQLException e) {
            System.out.println("Loi tim kiem san pham!");
            e.printStackTrace();
        }

        return danhSach;
    }
}
