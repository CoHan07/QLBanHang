
package dao;

import database.DBConnection;
import model.ChiTietHoaDon;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ChiTietHoaDonDAO {

    // 1. Lay toan bo chi tiet hoa don
    public List<ChiTietHoaDon> getAll() {
        List<ChiTietHoaDon> danhSach = new ArrayList<>();

        String sql = "SELECT fldMa_HD, fldMa_SP, "
                   + "fldSo_luong, fldDon_gia "
                   + "FROM tblChiTietHoaDon "
                   + "ORDER BY fldMa_HD, fldMa_SP";

        Connection conn = DBConnection.getConnection();

        if (conn == null) return danhSach;

        try (conn;
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                ChiTietHoaDon ct = new ChiTietHoaDon();

                ct.setMaHD(rs.getString("fldMa_HD"));
                ct.setMaSP(rs.getString("fldMa_SP"));
                ct.setSoLuong(rs.getInt("fldSo_luong"));
                ct.setDonGia(rs.getDouble("fldDon_gia"));

                danhSach.add(ct);
            }

        } catch (SQLException e) {
            System.out.println("Loi lay chi tiet hoa don!");
            e.printStackTrace();
        }

        return danhSach;
    }

    // 2. Them chi tiet hoa don va tru ton kho
    public boolean insert(ChiTietHoaDon ct) {
        String sqlTruKho =
                "UPDATE tblSanPham "
              + "SET fldSo_luong_ton = fldSo_luong_ton - ? "
              + "WHERE fldMa_SP = ? AND fldSo_luong_ton >= ?";

        String sqlThem =
                "INSERT INTO tblChiTietHoaDon "
              + "(fldMa_HD, fldMa_SP, fldSo_luong, fldDon_gia) "
              + "VALUES (?, ?, ?, ?)";

        Connection conn = DBConnection.getConnection();

        if (conn == null) return false;

        try (conn) {
            conn.setAutoCommit(false);

            try {
                // Tru ton kho
                try (PreparedStatement ps = conn.prepareStatement(sqlTruKho)) {
                    ps.setInt(1, ct.getSoLuong());
                    ps.setString(2, ct.getMaSP());
                    ps.setInt(3, ct.getSoLuong());

                    if (ps.executeUpdate() == 0) {
                        conn.rollback();
                        return false;
                    }
                }

                // Them chi tiet hoa don
                try (PreparedStatement ps = conn.prepareStatement(sqlThem)) {
                    ps.setString(1, ct.getMaHD());
                    ps.setString(2, ct.getMaSP());
                    ps.setInt(3, ct.getSoLuong());
                    ps.setDouble(4, ct.getDonGia());

                    ps.executeUpdate();
                }

                conn.commit();
                return true;

            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }

        } catch (SQLException e) {
            System.out.println("Loi them chi tiet hoa don va tru kho!");
            e.printStackTrace();
            return false;
        }
    }

    // 3. Sua chi tiet hoa don va dieu chinh ton kho
    public boolean update(ChiTietHoaDon ct) {

        String sqlLayCu =
                "SELECT fldSo_luong FROM tblChiTietHoaDon "
              + "WITH (UPDLOCK, ROWLOCK) "
              + "WHERE fldMa_HD = ? AND fldMa_SP = ?";

        String sqlTruKho =
                "UPDATE tblSanPham "
              + "SET fldSo_luong_ton = fldSo_luong_ton - ? "
              + "WHERE fldMa_SP = ? AND fldSo_luong_ton >= ?";

        String sqlCongKho =
                "UPDATE tblSanPham "
              + "SET fldSo_luong_ton = fldSo_luong_ton + ? "
              + "WHERE fldMa_SP = ?";

        String sqlSua =
                "UPDATE tblChiTietHoaDon "
              + "SET fldSo_luong = ?, fldDon_gia = ? "
              + "WHERE fldMa_HD = ? AND fldMa_SP = ?";

        Connection conn = DBConnection.getConnection();

        if (conn == null) return false;

        try (conn) {
            conn.setAutoCommit(false);

            try {
                int soLuongCu;

                // Lay so luong cu va khoa dong du lieu
                try (PreparedStatement ps = conn.prepareStatement(sqlLayCu)) {
                    ps.setString(1, ct.getMaHD());
                    ps.setString(2, ct.getMaSP());

                    try (ResultSet rs = ps.executeQuery()) {
                        if (!rs.next()) {
                            conn.rollback();
                            return false;
                        }

                        soLuongCu = rs.getInt("fldSo_luong");
                    }
                }

                int chenhLech = ct.getSoLuong() - soLuongCu;

                // Neu tang so luong thi tru them kho
                if (chenhLech > 0) {
                    try (PreparedStatement ps = conn.prepareStatement(sqlTruKho)) {
                        ps.setInt(1, chenhLech);
                        ps.setString(2, ct.getMaSP());
                        ps.setInt(3, chenhLech);

                        if (ps.executeUpdate() == 0) {
                            conn.rollback();
                            return false;
                        }
                    }
                }

                // Neu giam so luong thi hoan lai kho
                if (chenhLech < 0) {
                    try (PreparedStatement ps = conn.prepareStatement(sqlCongKho)) {
                        ps.setInt(1, -chenhLech);
                        ps.setString(2, ct.getMaSP());

                        if (ps.executeUpdate() == 0) {
                            conn.rollback();
                            return false;
                        }
                    }
                }

                // Cap nhat chi tiet hoa don
                try (PreparedStatement ps = conn.prepareStatement(sqlSua)) {
                    ps.setInt(1, ct.getSoLuong());
                    ps.setDouble(2, ct.getDonGia());
                    ps.setString(3, ct.getMaHD());
                    ps.setString(4, ct.getMaSP());

                    if (ps.executeUpdate() == 0) {
                        conn.rollback();
                        return false;
                    }
                }

                conn.commit();
                return true;

            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }

        } catch (SQLException e) {
            System.out.println("Loi cap nhat chi tiet hoa don va ton kho!");
            e.printStackTrace();
            return false;
        }
    }

    // 4. Xoa chi tiet hoa don va hoan lai ton kho
    public boolean delete(String maHD, String maSP) {

        String sqlLayCu =
                "SELECT fldSo_luong FROM tblChiTietHoaDon "
              + "WITH (UPDLOCK, ROWLOCK) "
              + "WHERE fldMa_HD = ? AND fldMa_SP = ?";

        String sqlCongKho =
                "UPDATE tblSanPham "
              + "SET fldSo_luong_ton = fldSo_luong_ton + ? "
              + "WHERE fldMa_SP = ?";

        String sqlXoa =
                "DELETE FROM tblChiTietHoaDon "
              + "WHERE fldMa_HD = ? AND fldMa_SP = ?";

        Connection conn = DBConnection.getConnection();

        if (conn == null) return false;

        try (conn) {
            conn.setAutoCommit(false);

            try {
                int soLuongCu;

                // Lay so luong can hoan lai
                try (PreparedStatement ps = conn.prepareStatement(sqlLayCu)) {
                    ps.setString(1, maHD);
                    ps.setString(2, maSP);

                    try (ResultSet rs = ps.executeQuery()) {
                        if (!rs.next()) {
                            conn.rollback();
                            return false;
                        }

                        soLuongCu = rs.getInt("fldSo_luong");
                    }
                }

                // Hoan lai ton kho
                try (PreparedStatement ps = conn.prepareStatement(sqlCongKho)) {
                    ps.setInt(1, soLuongCu);
                    ps.setString(2, maSP);

                    if (ps.executeUpdate() == 0) {
                        conn.rollback();
                        return false;
                    }
                }

                // Xoa chi tiet hoa don
                try (PreparedStatement ps = conn.prepareStatement(sqlXoa)) {
                    ps.setString(1, maHD);
                    ps.setString(2, maSP);

                    if (ps.executeUpdate() == 0) {
                        conn.rollback();
                        return false;
                    }
                }

                conn.commit();
                return true;

            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }

        } catch (SQLException e) {
            System.out.println("Loi xoa chi tiet hoa don va hoan kho!");
            e.printStackTrace();
            return false;
        }
    }

    // 5. Lay chi tiet theo ma hoa don
    public List<ChiTietHoaDon> getByMaHD(String maHD) {
        List<ChiTietHoaDon> danhSach = new ArrayList<>();

        String sql = "SELECT fldMa_HD, fldMa_SP, "
                   + "fldSo_luong, fldDon_gia "
                   + "FROM tblChiTietHoaDon "
                   + "WHERE fldMa_HD = ? "
                   + "ORDER BY fldMa_SP";

        Connection conn = DBConnection.getConnection();

        if (conn == null) return danhSach;

        try (conn;
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maHD);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ChiTietHoaDon ct = new ChiTietHoaDon();

                    ct.setMaHD(rs.getString("fldMa_HD"));
                    ct.setMaSP(rs.getString("fldMa_SP"));
                    ct.setSoLuong(rs.getInt("fldSo_luong"));
                    ct.setDonGia(rs.getDouble("fldDon_gia"));

                    danhSach.add(ct);
                }
            }

        } catch (SQLException e) {
            System.out.println("Loi lay chi tiet theo ma hoa don!");
            e.printStackTrace();
        }

        return danhSach;
    }

    // 6. Tim chi tiet theo ma hoa don va ma san pham
    public ChiTietHoaDon getById(String maHD, String maSP) {
        String sql = "SELECT fldMa_HD, fldMa_SP, "
                   + "fldSo_luong, fldDon_gia "
                   + "FROM tblChiTietHoaDon "
                   + "WHERE fldMa_HD = ? AND fldMa_SP = ?";

        Connection conn = DBConnection.getConnection();

        if (conn == null) return null;

        try (conn;
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maHD);
            ps.setString(2, maSP);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    ChiTietHoaDon ct = new ChiTietHoaDon();

                    ct.setMaHD(rs.getString("fldMa_HD"));
                    ct.setMaSP(rs.getString("fldMa_SP"));
                    ct.setSoLuong(rs.getInt("fldSo_luong"));
                    ct.setDonGia(rs.getDouble("fldDon_gia"));

                    return ct;
                }
            }

        } catch (SQLException e) {
            System.out.println("Loi tim chi tiet hoa don!");
            e.printStackTrace();
        }

        return null;
    }
}
