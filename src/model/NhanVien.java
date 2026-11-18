
package model;

public class NhanVien {

    private String maNV;
    private String tenNV;
    private String chucVu;
    private Boolean gioiTinh;
    private String sdt;

    public NhanVien() {
    }

    public NhanVien(String maNV, String tenNV, String chucVu,
                    Boolean gioiTinh, String sdt) {
        this.maNV = maNV;
        this.tenNV = tenNV;
        this.chucVu = chucVu;
        this.gioiTinh = gioiTinh;
        this.sdt = sdt;
    }

    public String getMaNV() {
        return maNV;
    }

    public void setMaNV(String maNV) {
        this.maNV = maNV;
    }

    public String getTenNV() {
        return tenNV;
    }

    public void setTenNV(String tenNV) {
        this.tenNV = tenNV;
    }

    public String getChucVu() {
        return chucVu;
    }

    public void setChucVu(String chucVu) {
        this.chucVu = chucVu;
    }

    public Boolean getGioiTinh() {
        return gioiTinh;
    }

    public void setGioiTinh(Boolean gioiTinh) {
        this.gioiTinh = gioiTinh;
    }

    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }
    @Override
    public String toString() {
        return getTenNV();
}
}
