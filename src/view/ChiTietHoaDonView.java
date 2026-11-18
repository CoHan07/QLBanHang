
package view;

import dao.ChiTietHoaDonDAO;
import dao.HoaDonDAO;
import dao.SanPhamDAO;

import model.ChiTietHoaDon;
import model.HoaDon;
import model.SanPham;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.Locale;

public class ChiTietHoaDonView extends JPanel {

    private JComboBox<String> cboHoaDon;
    private JComboBox<String> cboSanPham;

    private JTextField txtSoLuong;
    private JTextField txtDonGia;
    private JTextField txtThanhTien;

    private JTable tblChiTiet;
    private DefaultTableModel tableModel;

    private JButton btnThem;
    private JButton btnSua;
    private JButton btnXoa;
    private JButton btnLamMoi;

    private ChiTietHoaDonDAO chiTietDAO;
    private HoaDonDAO hoaDonDAO;
    private SanPhamDAO sanPhamDAO;

    private List<HoaDon> danhSachHoaDon;
    private List<SanPham> danhSachSanPham;

    public ChiTietHoaDonView() {

        chiTietDAO = new ChiTietHoaDonDAO();
        hoaDonDAO = new HoaDonDAO();
        sanPhamDAO = new SanPhamDAO();

        initComponents();

        loadHoaDon();
        loadSanPham();

        cboHoaDon.addActionListener(e -> loadData());

        loadData();
    }

    // Định dạng tiền: 18000000 -> 18.000.000
    private String dinhDangTien(double tien) {

        DecimalFormatSymbols symbols =
                new DecimalFormatSymbols(Locale.US);

        symbols.setGroupingSeparator('.');
        symbols.setDecimalSeparator(',');

        DecimalFormat df = new DecimalFormat("#,##0.##", symbols);

        return df.format(tien);
    }

    // Chuyển chuỗi tiền về kiểu double
    private double chuyenTien(String text) {

        return Double.parseDouble(
                text.trim().replace(".", "").replace(",", ".")
        );
    }

    private void initComponents() {

        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel lblTitle = new JLabel(
                "QUAN LY CHI TIET HOA DON",
                SwingConstants.CENTER
        );

        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitle.setForeground(new Color(35, 90, 160));

        add(lblTitle, BorderLayout.NORTH);

        // Panel nhập thông tin
        JPanel inputPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        inputPanel.setBackground(Color.WHITE);

        inputPanel.setBorder(
                BorderFactory.createTitledBorder(
                        "Thong tin chi tiet hoa don"
                )
        );

        cboHoaDon = new JComboBox<>();
        cboSanPham = new JComboBox<>();

        txtSoLuong = new JTextField();
        txtDonGia = new JTextField();
        txtThanhTien = new JTextField();

        txtDonGia.setEditable(false);
        txtThanhTien.setEditable(false);

        inputPanel.add(new JLabel("Ma hoa don:"));
        inputPanel.add(cboHoaDon);

        inputPanel.add(new JLabel("San pham:"));
        inputPanel.add(cboSanPham);

        inputPanel.add(new JLabel("So luong:"));
        inputPanel.add(txtSoLuong);

        inputPanel.add(new JLabel("Don gia:"));
        inputPanel.add(txtDonGia);

        inputPanel.add(new JLabel("Thanh tien:"));
        inputPanel.add(txtThanhTien);

        // Panel nút chức năng
        JPanel buttonPanel = new JPanel(
                new FlowLayout(FlowLayout.CENTER, 15, 10)
        );

        buttonPanel.setBackground(Color.WHITE);

        btnThem = new JButton("Them");
        btnSua = new JButton("Sua");
        btnXoa = new JButton("Xoa");
        btnLamMoi = new JButton("Lam moi");

        buttonPanel.add(btnThem);
        buttonPanel.add(btnSua);
        buttonPanel.add(btnXoa);
        buttonPanel.add(btnLamMoi);

        JPanel topPanel = new JPanel(new BorderLayout(5, 5));
        topPanel.setBackground(Color.WHITE);

        topPanel.add(inputPanel, BorderLayout.CENTER);
        topPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Bảng chi tiết hóa đơn
        String[] columns = {
            "Ma hoa don",
            "Ma san pham",
            "Ten san pham",
            "So luong",
            "Don gia",
            "Thanh tien"
        };

        tableModel = new DefaultTableModel(columns, 0) {

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tblChiTiet = new JTable(tableModel);
        tblChiTiet.setRowHeight(28);

        tblChiTiet.getTableHeader().setFont(
                new Font("Arial", Font.BOLD, 13)
        );

        JScrollPane scrollPane = new JScrollPane(tblChiTiet);

        scrollPane.setBorder(
                BorderFactory.createTitledBorder(
                        "Danh sach chi tiet hoa don"
                )
        );

        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));
        centerPanel.setBackground(Color.WHITE);

        centerPanel.add(topPanel, BorderLayout.NORTH);
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        add(centerPanel, BorderLayout.CENTER);

        // Sự kiện nút
        btnThem.addActionListener(e -> themChiTiet());
        btnSua.addActionListener(e -> suaChiTiet());
        btnXoa.addActionListener(e -> xoaChiTiet());
        btnLamMoi.addActionListener(e -> lamMoi());

        // Sự kiện chọn sản phẩm
        cboSanPham.addActionListener(e -> capNhatDonGia());

        // Sự kiện nhập số lượng
        txtSoLuong.addActionListener(e -> tinhThanhTien());

        // Sự kiện chọn dòng trong bảng
        tblChiTiet.getSelectionModel().addListSelectionListener(e -> {

            if (!e.getValueIsAdjusting()) {
                hienThiThongTin();
            }
        });
    }

    // Tải danh sách hóa đơn
    private void loadHoaDon() {

        cboHoaDon.removeAllItems();

        danhSachHoaDon = hoaDonDAO.getAll();

        for (HoaDon hd : danhSachHoaDon) {

            cboHoaDon.addItem(
                    hd.getMaHD() + " - " + hd.getNgayLap()
            );
        }
    }

    // Tải danh sách sản phẩm
    private void loadSanPham() {

        cboSanPham.removeAllItems();

        danhSachSanPham = sanPhamDAO.getAll();

        for (SanPham sp : danhSachSanPham) {

            cboSanPham.addItem(
                    sp.getMaSP() + " - " + sp.getTenSP()
            );
        }
    }

    // Lấy mã hóa đơn đang chọn
    private String getMaHD() {

        Object selected = cboHoaDon.getSelectedItem();

        if (selected == null) {
            return null;
        }

        String value = selected.toString();

        return value.split(" - ", 2)[0];
    }

    // Lấy sản phẩm đang chọn
    private SanPham getSanPhamSelected() {

        Object selected = cboSanPham.getSelectedItem();

        if (selected == null) {
            return null;
        }

        String value = selected.toString();
        String maSP = value.split(" - ", 2)[0];

        for (SanPham sp : danhSachSanPham) {

            if (sp.getMaSP().equals(maSP)) {
                return sp;
            }
        }

        return null;
    }

    // Cập nhật đơn giá
    private void capNhatDonGia() {

        SanPham sp = getSanPhamSelected();

        if (sp != null) {

            txtDonGia.setText(
                    dinhDangTien(sp.getGiaBan())
            );

            tinhThanhTien();

        } else {

            txtDonGia.setText("");
            txtThanhTien.setText("");
        }
    }

    // Tính thành tiền
    private void tinhThanhTien() {

        try {

            String soLuongText = txtSoLuong.getText().trim();
            String donGiaText = txtDonGia.getText().trim();

            if (soLuongText.isEmpty() || donGiaText.isEmpty()) {

                txtThanhTien.setText("");
                return;
            }

            int soLuong = Integer.parseInt(soLuongText);
            double donGia = chuyenTien(donGiaText);

            double thanhTien = soLuong * donGia;

            txtThanhTien.setText(
                    dinhDangTien(thanhTien)
            );

        } catch (NumberFormatException e) {

            txtThanhTien.setText("");
        }
    }

    // Tải chi tiết hóa đơn
    private void loadData() {

        tableModel.setRowCount(0);

        String maHD = getMaHD();

        if (maHD == null) {
            return;
        }

        List<ChiTietHoaDon> danhSach =
                chiTietDAO.getByMaHD(maHD);

        for (ChiTietHoaDon ct : danhSach) {

            SanPham sp = sanPhamDAO.getById(ct.getMaSP());

            String tenSP = "";

            if (sp != null) {
                tenSP = sp.getTenSP();
            }

            double thanhTien = ct.getSoLuong() * ct.getDonGia();

            tableModel.addRow(new Object[]{
                ct.getMaHD(),
                ct.getMaSP(),
                tenSP,
                ct.getSoLuong(),
                dinhDangTien(ct.getDonGia()),
                dinhDangTien(thanhTien)
            });
        }
    }

    // Thêm chi tiết hóa đơn
    private void themChiTiet() {

        String maHD = getMaHD();

        SanPham sp = getSanPhamSelected();

        if (maHD == null || sp == null) {

            JOptionPane.showMessageDialog(
                    this,
                    "Vui long chon hoa don va san pham!"
            );

            return;
        }

        try {

            String soLuongText = txtSoLuong.getText().trim();

            if (soLuongText.isEmpty()) {

                JOptionPane.showMessageDialog(
                        this,
                        "Vui long nhap so luong!"
                );

                return;
            }

            int soLuong = Integer.parseInt(soLuongText);

            if (soLuong <= 0) {

                JOptionPane.showMessageDialog(
                        this,
                        "So luong phai lon hon 0!"
                );

                return;
            }

            if (soLuong > sp.getSoLuongTon()) {

                JOptionPane.showMessageDialog(
                        this,
                        "So luong san pham trong kho khong du!"
                );

                return;
            }

            ChiTietHoaDon ct = new ChiTietHoaDon(
                    maHD,
                    sp.getMaSP(),
                    soLuong,
                    sp.getGiaBan()
            );

            if (chiTietDAO.insert(ct)) {

                capNhatTongTienHoaDon();
                loadData();

                JOptionPane.showMessageDialog(
                        this,
                        "Them chi tiet hoa don thanh cong!"
                );

                lamMoi();

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "Them that bai! Co the san pham da ton tai "
                        + "trong hoa don nay."
                );
            }

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "So luong phai la so nguyen hop le!"
            );
        }
    }

    // Sửa chi tiết hóa đơn
    private void suaChiTiet() {

        String maHD = getMaHD();

        SanPham sp = getSanPhamSelected();

        if (maHD == null || sp == null) {

            JOptionPane.showMessageDialog(
                    this,
                    "Vui long chon hoa don va san pham!"
            );

            return;
        }

        try {

            int soLuong = Integer.parseInt(
                    txtSoLuong.getText().trim()
            );

            if (soLuong <= 0) {

                JOptionPane.showMessageDialog(
                        this,
                        "So luong phai lon hon 0!"
                );

                return;
            }

            if (soLuong > sp.getSoLuongTon()) {

                JOptionPane.showMessageDialog(
                        this,
                        "So luong san pham trong kho khong du!"
                );

                return;
            }

            ChiTietHoaDon ct = new ChiTietHoaDon(
                    maHD,
                    sp.getMaSP(),
                    soLuong,
                    sp.getGiaBan()
            );

            if (chiTietDAO.update(ct)) {

                capNhatTongTienHoaDon();
                loadData();

                JOptionPane.showMessageDialog(
                        this,
                        "Cap nhat chi tiet hoa don thanh cong!"
                );

                lamMoi();

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "Cap nhat chi tiet hoa don that bai!"
                );
            }

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "So luong khong hop le!"
            );
        }
    }

    // Xóa chi tiết hóa đơn
    private void xoaChiTiet() {

        String maHD = getMaHD();

        SanPham sp = getSanPhamSelected();

        if (maHD == null || sp == null) {

            JOptionPane.showMessageDialog(
                    this,
                    "Vui long chon chi tiet can xoa!"
            );

            return;
        }

        int choice = JOptionPane.showConfirmDialog(
                this,
                "Ban co chac chan muon xoa chi tiet nay?",
                "Xac nhan xoa",
                JOptionPane.YES_NO_OPTION
        );

        if (choice == JOptionPane.YES_OPTION) {

            if (chiTietDAO.delete(maHD, sp.getMaSP())) {

                capNhatTongTienHoaDon();
                loadData();

                JOptionPane.showMessageDialog(
                        this,
                        "Xoa chi tiet hoa don thanh cong!"
                );

                lamMoi();

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "Xoa chi tiet hoa don that bai!"
                );
            }
        }
    }

    // Cập nhật tổng tiền hóa đơn
    private void capNhatTongTienHoaDon() {

        String maHD = getMaHD();

        if (maHD == null) {
            return;
        }

        List<ChiTietHoaDon> danhSach =
                chiTietDAO.getByMaHD(maHD);

        double tongTien = 0;

        for (ChiTietHoaDon ct : danhSach) {

            tongTien += ct.getSoLuong() * ct.getDonGia();
        }

        HoaDon hd = hoaDonDAO.getById(maHD);

        if (hd != null) {

            HoaDon hoaDonMoi = new HoaDon(
                    hd.getMaHD(),
                    hd.getNgayLap(),
                    hd.getMaKH(),
                    hd.getMaNV(),
                    tongTien
            );

            hoaDonDAO.update(hoaDonMoi);
        }
    }

    // Hiển thị thông tin lên form
    private void hienThiThongTin() {

        int row = tblChiTiet.getSelectedRow();

        if (row >= 0) {

            String maSP =
                    tableModel.getValueAt(row, 1).toString();

            for (int i = 0; i < cboSanPham.getItemCount(); i++) {

                String item = cboSanPham.getItemAt(i);

                if (item.startsWith(maSP + " - ")) {

                    cboSanPham.setSelectedIndex(i);
                    break;
                }
            }

            txtSoLuong.setText(
                    tableModel.getValueAt(row, 3).toString()
            );

            txtDonGia.setText(
                    tableModel.getValueAt(row, 4).toString()
            );

            txtThanhTien.setText(
                    tableModel.getValueAt(row, 5).toString()
            );
        }
    }

    // Làm mới form
    private void lamMoi() {

        txtSoLuong.setText("");
        txtDonGia.setText("");
        txtThanhTien.setText("");

        tblChiTiet.clearSelection();

        capNhatDonGia();
    }
}
