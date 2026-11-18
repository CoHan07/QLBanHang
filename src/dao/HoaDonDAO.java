
package dao;

import database.DBConnection;
import model.HoaDon;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class HoaDonDAO {

    // 1. Lay danh sach hoa don
    public List<HoaDon> getAll() {
        List<HoaDon> danhSach = new ArrayList<>();

        String sql = "SELECT fldMa_HD, fldNgay_lap, fldMa_KH, "
                   + "fldMa_NV, fldTong_tien "
                   + "FROM tblHoaDon "
                   + "ORDER BY fldNgay_lap DESC";

        Connection conn = DBConnection.getConnection();

        if (conn == null) {
            return danhSach;
        }

        try (conn;
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                HoaDon hd = new HoaDon();

                hd.setMaHD(rs.getString("fldMa_HD"));
                hd.setNgayLap(rs.getString("fldNgay_lap"));
                hd.setMaKH(rs.getString("fldMa_KH"));
                hd.setMaNV(rs.getString("fldMa_NV"));

                double tongTien = rs.getDouble("fldTong_tien");

                if (rs.wasNull()) {
                    hd.setTongTien(null);
                } else {
                    hd.setTongTien(tongTien);
                }

                danhSach.add(hd);
            }

        } catch (SQLException e) {
            System.out.println("Loi lay danh sach hoa don!");
            e.printStackTrace();
        }

        return danhSach;
    }

    // 2. Them hoa don
    public boolean insert(HoaDon hd) {
        String sql = "INSERT INTO tblHoaDon "
                   + "(fldMa_HD, fldNgay_lap, fldMa_KH, "
                   + "fldMa_NV, fldTong_tien) "
                   + "VALUES (?, ?, ?, ?, ?)";

        Connection conn = DBConnection.getConnection();

        if (conn == null) {
            return false;
        }

        try (conn;
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, hd.getMaHD());
            ps.setString(2, hd.getNgayLap());

            if (hd.getMaKH() == null) {
                ps.setNull(3, java.sql.Types.NVARCHAR);
            } else {
                ps.setString(3, hd.getMaKH());
            }

            if (hd.getMaNV() == null) {
                ps.setNull(4, java.sql.Types.NVARCHAR);
            } else {
                ps.setString(4, hd.getMaNV());
            }

            if (hd.getTongTien() == null) {
                ps.setNull(5, java.sql.Types.DOUBLE);
            } else {
                ps.setDouble(5, hd.getTongTien());
            }

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Loi them hoa don!");
            e.printStackTrace();
            return false;
        }
    }

    // 3. Cap nhat hoa don
    public boolean update(HoaDon hd) {
        String sql = "UPDATE tblHoaDon "
                   + "SET fldNgay_lap = ?, "
                   + "fldMa_KH = ?, "
                   + "fldMa_NV = ?, "
                   + "fldTong_tien = ? "
                   + "WHERE fldMa_HD = ?";

        Connection conn = DBConnection.getConnection();

        if (conn == null) {
            return false;
        }

        try (conn;
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, hd.getNgayLap());

            if (hd.getMaKH() == null) {
                ps.setNull(2, java.sql.Types.NVARCHAR);
            } else {
                ps.setString(2, hd.getMaKH());
            }

            if (hd.getMaNV() == null) {
                ps.setNull(3, java.sql.Types.NVARCHAR);
            } else {
                ps.setString(3, hd.getMaNV());
            }

            if (hd.getTongTien() == null) {
                ps.setNull(4, java.sql.Types.DOUBLE);
            } else {
                ps.setDouble(4, hd.getTongTien());
            }

            ps.setString(5, hd.getMaHD());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Loi cap nhat hoa don!");
            e.printStackTrace();
            return false;
        }
    }

    // 4. Xoa hoa don
    public boolean delete(String maHD) {
        String sql = "DELETE FROM tblHoaDon "
                   + "WHERE fldMa_HD = ?";

        Connection conn = DBConnection.getConnection();

        if (conn == null) {
            return false;
        }

        try (conn;
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maHD);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Loi xoa hoa don!");
            e.printStackTrace();
            return false;
        }
    }

    // 5. Tim hoa don theo ma
    public HoaDon getById(String maHD) {
        String sql = "SELECT fldMa_HD, fldNgay_lap, fldMa_KH, "
                   + "fldMa_NV, fldTong_tien "
                   + "FROM tblHoaDon "
                   + "WHERE fldMa_HD = ?";

        Connection conn = DBConnection.getConnection();

        if (conn == null) {
            return null;
        }

        try (conn;
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maHD);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    HoaDon hd = new HoaDon();

                    hd.setMaHD(rs.getString("fldMa_HD"));
                    hd.setNgayLap(rs.getString("fldNgay_lap"));
                    hd.setMaKH(rs.getString("fldMa_KH"));
                    hd.setMaNV(rs.getString("fldMa_NV"));

                    double tongTien = rs.getDouble("fldTong_tien");

                    if (rs.wasNull()) {
                        hd.setTongTien(null);
                    } else {
                        hd.setTongTien(tongTien);
                    }

                    return hd;
                }
            }

        } catch (SQLException e) {
            System.out.println("Loi tim hoa don!");
            e.printStackTrace();
        }

        return null;
    }

    // 6. Tim hoa don theo ma khach hang
    public List<HoaDon> getByMaKH(String maKH) {
        List<HoaDon> danhSach = new ArrayList<>();

        String sql = "SELECT fldMa_HD, fldNgay_lap, fldMa_KH, "
                   + "fldMa_NV, fldTong_tien "
                   + "FROM tblHoaDon "
                   + "WHERE fldMa_KH = ? "
                   + "ORDER BY fldNgay_lap DESC";

        Connection conn = DBConnection.getConnection();

        if (conn == null) {
            return danhSach;
        }

        try (conn;
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maKH);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    HoaDon hd = new HoaDon();

                    hd.setMaHD(rs.getString("fldMa_HD"));
                    hd.setNgayLap(rs.getString("fldNgay_lap"));
                    hd.setMaKH(rs.getString("fldMa_KH"));
                    hd.setMaNV(rs.getString("fldMa_NV"));

                    double tongTien = rs.getDouble("fldTong_tien");

                    if (rs.wasNull()) {
                        hd.setTongTien(null);
                    } else {
                        hd.setTongTien(tongTien);
                    }

                    danhSach.add(hd);
                }
            }

        } catch (SQLException e) {
            System.out.println("Loi tim hoa don theo khach hang!");
            e.printStackTrace();
        }

        return danhSach;
    }
}
