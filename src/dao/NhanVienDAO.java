
package dao;

import database.DBConnection;
import model.NhanVien;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class NhanVienDAO {

    // 1. Lay danh sach nhan vien
    public List<NhanVien> getAll() {
        List<NhanVien> danhSach = new ArrayList<>();

        String sql = "SELECT fldMa_NV, fldTen_NV, fldChuc_vu, "
                   + "fldGioi_tinh, fldSDT "
                   + "FROM tblNhanVien "
                   + "ORDER BY fldMa_NV";

        Connection conn = DBConnection.getConnection();

        if (conn == null) {
            return danhSach;
        }

        try (conn;
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                NhanVien nv = new NhanVien();

                nv.setMaNV(rs.getString("fldMa_NV"));
                nv.setTenNV(rs.getString("fldTen_NV"));
                nv.setChucVu(rs.getString("fldChuc_vu"));

                boolean gioiTinh = rs.getBoolean("fldGioi_tinh");

                if (rs.wasNull()) {
                    nv.setGioiTinh(null);
                } else {
                    nv.setGioiTinh(gioiTinh);
                }

                nv.setSdt(rs.getString("fldSDT"));

                danhSach.add(nv);
            }

        } catch (SQLException e) {
            System.out.println("Loi lay danh sach nhan vien!");
            e.printStackTrace();
        }

        return danhSach;
    }

    // 2. Them nhan vien
    public boolean insert(NhanVien nv) {
        String sql = "INSERT INTO tblNhanVien "
                   + "(fldMa_NV, fldTen_NV, fldChuc_vu, "
                   + "fldGioi_tinh, fldSDT) "
                   + "VALUES (?, ?, ?, ?, ?)";

        Connection conn = DBConnection.getConnection();

        if (conn == null) {
            return false;
        }

        try (conn;
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, nv.getMaNV());
            ps.setString(2, nv.getTenNV());
            ps.setString(3, nv.getChucVu());

            if (nv.getGioiTinh() == null) {
                ps.setNull(4, java.sql.Types.BIT);
            } else {
                ps.setBoolean(4, nv.getGioiTinh());
            }

            ps.setString(5, nv.getSdt());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Loi them nhan vien!");
            e.printStackTrace();
            return false;
        }
    }

    // 3. Cap nhat nhan vien
    public boolean update(NhanVien nv) {
        String sql = "UPDATE tblNhanVien "
                   + "SET fldTen_NV = ?, "
                   + "fldChuc_vu = ?, "
                   + "fldGioi_tinh = ?, "
                   + "fldSDT = ? "
                   + "WHERE fldMa_NV = ?";

        Connection conn = DBConnection.getConnection();

        if (conn == null) {
            return false;
        }

        try (conn;
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, nv.getTenNV());
            ps.setString(2, nv.getChucVu());

            if (nv.getGioiTinh() == null) {
                ps.setNull(3, java.sql.Types.BIT);
            } else {
                ps.setBoolean(3, nv.getGioiTinh());
            }

            ps.setString(4, nv.getSdt());
            ps.setString(5, nv.getMaNV());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Loi cap nhat nhan vien!");
            e.printStackTrace();
            return false;
        }
    }

    // 4. Xoa nhan vien
    public boolean delete(String maNV) {
        String sql = "DELETE FROM tblNhanVien "
                   + "WHERE fldMa_NV = ?";

        Connection conn = DBConnection.getConnection();

        if (conn == null) {
            return false;
        }

        try (conn;
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maNV);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Loi xoa nhan vien!");
            e.printStackTrace();
            return false;
        }
    }

    // 5. Tim nhan vien theo ma
    public NhanVien getById(String maNV) {
        String sql = "SELECT fldMa_NV, fldTen_NV, fldChuc_vu, "
                   + "fldGioi_tinh, fldSDT "
                   + "FROM tblNhanVien "
                   + "WHERE fldMa_NV = ?";

        Connection conn = DBConnection.getConnection();

        if (conn == null) {
            return null;
        }

        try (conn;
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maNV);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    NhanVien nv = new NhanVien();

                    nv.setMaNV(rs.getString("fldMa_NV"));
                    nv.setTenNV(rs.getString("fldTen_NV"));
                    nv.setChucVu(rs.getString("fldChuc_vu"));

                    boolean gioiTinh = rs.getBoolean("fldGioi_tinh");

                    if (rs.wasNull()) {
                        nv.setGioiTinh(null);
                    } else {
                        nv.setGioiTinh(gioiTinh);
                    }

                    nv.setSdt(rs.getString("fldSDT"));

                    return nv;
                }
            }

        } catch (SQLException e) {
            System.out.println("Loi tim nhan vien!");
            e.printStackTrace();
        }

        return null;
    }

    // 6. Tim kiem nhan vien theo ten
    public List<NhanVien> searchByName(String tenNV) {
        List<NhanVien> danhSach = new ArrayList<>();

        String sql = "SELECT fldMa_NV, fldTen_NV, fldChuc_vu, "
                   + "fldGioi_tinh, fldSDT "
                   + "FROM tblNhanVien "
                   + "WHERE fldTen_NV LIKE ? "
                   + "ORDER BY fldMa_NV";

        Connection conn = DBConnection.getConnection();

        if (conn == null) {
            return danhSach;
        }

        try (conn;
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, "%" + tenNV + "%");

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    NhanVien nv = new NhanVien();

                    nv.setMaNV(rs.getString("fldMa_NV"));
                    nv.setTenNV(rs.getString("fldTen_NV"));
                    nv.setChucVu(rs.getString("fldChuc_vu"));

                    boolean gioiTinh = rs.getBoolean("fldGioi_tinh");

                    if (rs.wasNull()) {
                        nv.setGioiTinh(null);
                    } else {
                        nv.setGioiTinh(gioiTinh);
                    }

                    nv.setSdt(rs.getString("fldSDT"));

                    danhSach.add(nv);
                }
            }

        } catch (SQLException e) {
            System.out.println("Loi tim kiem nhan vien!");
            e.printStackTrace();
        }

        return danhSach;
    }
}
