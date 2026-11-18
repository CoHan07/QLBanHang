
package dao;

import database.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;

public class ThongKeDAO {

    // Lay tong doanh thu
    public double getTongDoanhThu() {

        String sql = """
                SELECT COALESCE(SUM(fldTong_tien), 0)
                FROM tblHoaDon
                """;

        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()
        ) {

            if (rs.next()) {
                return rs.getDouble(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    // Lay tong so hoa don
    public int getTongHoaDon() {

        String sql = """
                SELECT COUNT(*)
                FROM tblHoaDon
                """;

        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()
        ) {

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    // Lay tong so san pham da ban
    public int getTongSanPhamDaBan() {

        String sql = """
                SELECT COALESCE(SUM(fldSo_luong), 0)
                FROM tblChiTietHoaDon
                """;

        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()
        ) {

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    // Lay so san pham sap het hang
    public int getSoSanPhamSapHet() {

        String sql = """
                SELECT COUNT(*)
                FROM tblSanPham
                WHERE fldSo_luong_ton <= 5
                """;

        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()
        ) {

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    // Lay doanh thu theo thang
    public Map<String, Double> getDoanhThuTheoThang() {

        Map<String, Double> data = new LinkedHashMap<>();

        String sql = """
                SELECT
                    YEAR(ngay) AS nam,
                    MONTH(ngay) AS thang,
                    SUM(COALESCE(hd.fldTong_tien, 0)) AS doanhThu
                FROM (
                    SELECT
                        fldNgay_lap,
                        fldTong_tien,
                        TRY_CONVERT(date, fldNgay_lap, 103) AS ngay
                    FROM tblHoaDon
                ) hd
                WHERE ngay IS NOT NULL
                GROUP BY YEAR(ngay), MONTH(ngay)
                ORDER BY YEAR(ngay), MONTH(ngay)
                """;

        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()
        ) {

            while (rs.next()) {

                int thang = rs.getInt("thang");
                int nam = rs.getInt("nam");

                double doanhThu = rs.getDouble("doanhThu");

                String nhan = String.format("%02d/%d", thang, nam);

                data.put(nhan, doanhThu);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return data;
    }

    // Lay Top 5 san pham ban chay
    public List<Object[]> getTop5SanPhamBanChay() {

        List<Object[]> data = new ArrayList<>();

        String sql = """
                SELECT TOP 5
                    sp.fldMa_SP,
                    sp.fldTen_SP,
                    SUM(ct.fldSo_luong) AS tongSoLuong,
                    SUM(ct.fldSo_luong * ct.fldDon_gia) AS doanhThu
                FROM tblChiTietHoaDon ct
                INNER JOIN tblSanPham sp
                    ON ct.fldMa_SP = sp.fldMa_SP
                GROUP BY
                    sp.fldMa_SP,
                    sp.fldTen_SP
                ORDER BY
                    SUM(ct.fldSo_luong) DESC,
                    sp.fldMa_SP ASC
                """;

        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()
        ) {

            while (rs.next()) {

                Object[] row = {
                    rs.getString("fldMa_SP"),
                    rs.getString("fldTen_SP"),
                    rs.getInt("tongSoLuong"),
                    rs.getDouble("doanhThu")
                };

                data.add(row);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return data;
    }
}
