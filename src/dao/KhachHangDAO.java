
package dao;

import database.DBConnection;
import model.KhachHang;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class KhachHangDAO {

    // 1. Lay danh sach khach hang
    public List<KhachHang> getAll() {
        List<KhachHang> danhSach = new ArrayList<>();

        String sql = "SELECT fldMa_KH, fldTen_KH, fldSDT, "
                   + "fldDia_chi, fldEmail "
                   + "FROM tblKhachHang "
                   + "ORDER BY fldMa_KH";

        Connection conn = DBConnection.getConnection();

        if (conn == null) {
            return danhSach;
        }

        try (conn;
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                KhachHang kh = new KhachHang();

                kh.setMaKH(rs.getString("fldMa_KH"));
                kh.setTenKH(rs.getString("fldTen_KH"));
                kh.setSdt(rs.getString("fldSDT"));
                kh.setDiaChi(rs.getString("fldDia_chi"));
                kh.setEmail(rs.getString("fldEmail"));

                danhSach.add(kh);
            }

        } catch (SQLException e) {
            System.out.println("Loi lay danh sach khach hang!");
            e.printStackTrace();
        }

        return danhSach;
    }

    // 2. Them khach hang
    public boolean insert(KhachHang kh) {
        String sql = "INSERT INTO tblKhachHang "
                   + "(fldMa_KH, fldTen_KH, fldSDT, "
                   + "fldDia_chi, fldEmail) "
                   + "VALUES (?, ?, ?, ?, ?)";

        Connection conn = DBConnection.getConnection();

        if (conn == null) {
            return false;
        }

        try (conn;
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, kh.getMaKH());
            ps.setString(2, kh.getTenKH());
            ps.setString(3, kh.getSdt());
            ps.setString(4, kh.getDiaChi());
            ps.setString(5, kh.getEmail());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Loi them khach hang!");
            e.printStackTrace();
            return false;
        }
    }

    // 3. Cap nhat khach hang
    public boolean update(KhachHang kh) {
        String sql = "UPDATE tblKhachHang "
                   + "SET fldTen_KH = ?, "
                   + "fldSDT = ?, "
                   + "fldDia_chi = ?, "
                   + "fldEmail = ? "
                   + "WHERE fldMa_KH = ?";

        Connection conn = DBConnection.getConnection();

        if (conn == null) {
            return false;
        }

        try (conn;
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, kh.getTenKH());
            ps.setString(2, kh.getSdt());
            ps.setString(3, kh.getDiaChi());
            ps.setString(4, kh.getEmail());
            ps.setString(5, kh.getMaKH());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Loi cap nhat khach hang!");
            e.printStackTrace();
            return false;
        }
    }

    // 4. Xoa khach hang
    public boolean delete(String maKH) {
        String sql = "DELETE FROM tblKhachHang "
                   + "WHERE fldMa_KH = ?";

        Connection conn = DBConnection.getConnection();

        if (conn == null) {
            return false;
        }

        try (conn;
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maKH);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Loi xoa khach hang!");
            e.printStackTrace();
            return false;
        }
    }

    // 5. Tim khach hang theo ma
    public KhachHang getById(String maKH) {
        String sql = "SELECT fldMa_KH, fldTen_KH, fldSDT, "
                   + "fldDia_chi, fldEmail "
                   + "FROM tblKhachHang "
                   + "WHERE fldMa_KH = ?";

        Connection conn = DBConnection.getConnection();

        if (conn == null) {
            return null;
        }

        try (conn;
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maKH);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    KhachHang kh = new KhachHang();

                    kh.setMaKH(rs.getString("fldMa_KH"));
                    kh.setTenKH(rs.getString("fldTen_KH"));
                    kh.setSdt(rs.getString("fldSDT"));
                    kh.setDiaChi(rs.getString("fldDia_chi"));
                    kh.setEmail(rs.getString("fldEmail"));

                    return kh;
                }
            }

        } catch (SQLException e) {
            System.out.println("Loi tim khach hang!");
            e.printStackTrace();
        }

        return null;
    }

    // 6. Tim kiem khach hang theo ten
    public List<KhachHang> searchByName(String tenKH) {
        List<KhachHang> danhSach = new ArrayList<>();

        String sql = "SELECT fldMa_KH, fldTen_KH, fldSDT, "
                   + "fldDia_chi, fldEmail "
                   + "FROM tblKhachHang "
                   + "WHERE fldTen_KH LIKE ? "
                   + "ORDER BY fldMa_KH";

        Connection conn = DBConnection.getConnection();

        if (conn == null) {
            return danhSach;
        }

        try (conn;
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, "%" + tenKH + "%");

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    KhachHang kh = new KhachHang();

                    kh.setMaKH(rs.getString("fldMa_KH"));
                    kh.setTenKH(rs.getString("fldTen_KH"));
                    kh.setSdt(rs.getString("fldSDT"));
                    kh.setDiaChi(rs.getString("fldDia_chi"));
                    kh.setEmail(rs.getString("fldEmail"));

                    danhSach.add(kh);
                }
            }

        } catch (SQLException e) {
            System.out.println("Loi tim kiem khach hang!");
            e.printStackTrace();
        }

        return danhSach;
    }
}
