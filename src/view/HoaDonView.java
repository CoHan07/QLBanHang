
package view;

import dao.HoaDonDAO;
import dao.KhachHangDAO;
import dao.NhanVienDAO;

import model.HoaDon;
import model.KhachHang;
import model.NhanVien;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class HoaDonView extends JPanel {

    private JTextField txtMaHD;
    private JTextField txtNgayLap;
    private JTextField txtTongTien;
    private JTextField txtTimKiem;

    private JComboBox<KhachHang> cboKhachHang;
    private JComboBox<NhanVien> cboNhanVien;

    private JTable tblHoaDon;
    private DefaultTableModel tableModel;

    private JButton btnThem;
    private JButton btnSua;
    private JButton btnXoa;
    private JButton btnLamMoi;
    private JButton btnTimKiem;

    private HoaDonDAO hoaDonDAO;
    private KhachHangDAO khachHangDAO;
    private NhanVienDAO nhanVienDAO;

    public HoaDonView() {

        hoaDonDAO = new HoaDonDAO();
        khachHangDAO = new KhachHangDAO();
        nhanVienDAO = new NhanVienDAO();

        initComponents();

        loadKhachHang();
        loadNhanVien();
        loadData();
    }

    private void initComponents() {

        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);

        setBorder(
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        );

        // Tiêu đề
        JLabel lblTitle = new JLabel(
                "QUAN LY HOA DON",
                SwingConstants.CENTER
        );

        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitle.setForeground(new Color(35, 90, 160));

        add(lblTitle, BorderLayout.NORTH);

        // Panel nhập thông tin
        JPanel inputPanel = new JPanel(
                new GridLayout(5, 2, 10, 10)
        );

        inputPanel.setBackground(Color.WHITE);

        inputPanel.setBorder(
                BorderFactory.createTitledBorder(
                        "Thong tin hoa don"
                )
        );

        txtMaHD = new JTextField();
        txtNgayLap = new JTextField();
        txtTongTien = new JTextField();

        cboKhachHang = new JComboBox<>();
        cboNhanVien = new JComboBox<>();

        inputPanel.add(new JLabel("Ma hoa don:"));
        inputPanel.add(txtMaHD);

        inputPanel.add(new JLabel("Ngay lap (dd/MM/yyyy):"));
        inputPanel.add(txtNgayLap);

        inputPanel.add(new JLabel("Khach hang:"));
        inputPanel.add(cboKhachHang);

        inputPanel.add(new JLabel("Nhan vien:"));
        inputPanel.add(cboNhanVien);

        inputPanel.add(new JLabel("Tong tien:"));
        inputPanel.add(txtTongTien);

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

        // Panel nhập liệu + nút chức năng
        JPanel topPanel = new JPanel(
                new BorderLayout(5, 5)
        );

        topPanel.setBackground(Color.WHITE);

        topPanel.add(inputPanel, BorderLayout.CENTER);
        topPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Panel tìm kiếm
        JPanel searchPanel = new JPanel(
                new FlowLayout(FlowLayout.LEFT, 10, 5)
        );

        searchPanel.setBackground(Color.WHITE);

        txtTimKiem = new JTextField(20);
        btnTimKiem = new JButton("Tim kiem");

        searchPanel.add(new JLabel("Tim theo ma KH:"));
        searchPanel.add(txtTimKiem);
        searchPanel.add(btnTimKiem);

        // Bảng hóa đơn
        String[] columns = {
            "Ma hoa don",
            "Ngay lap",
            "Ma khach hang",
            "Ma nhan vien",
            "Tong tien"
        };

        tableModel = new DefaultTableModel(columns, 0) {

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tblHoaDon = new JTable(tableModel);
        tblHoaDon.setRowHeight(28);

        tblHoaDon.getTableHeader().setFont(
                new Font("Arial", Font.BOLD, 13)
        );

        JScrollPane scrollPane = new JScrollPane(tblHoaDon);

        scrollPane.setBorder(
                BorderFactory.createTitledBorder(
                        "Danh sach hoa don"
                )
        );

        // Panel phía trên: nhập liệu + nút + tìm kiếm
        JPanel northPanel = new JPanel(
                new BorderLayout(5, 5)
        );

        northPanel.setBackground(Color.WHITE);

        northPanel.add(topPanel, BorderLayout.CENTER);
        northPanel.add(searchPanel, BorderLayout.SOUTH);

        // Panel chứa bảng
        JPanel tablePanel = new JPanel(
                new BorderLayout()
        );

        tablePanel.setBackground(Color.WHITE);
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        // Panel chính
        JPanel centerPanel = new JPanel(
                new BorderLayout(10, 10)
        );

        centerPanel.setBackground(Color.WHITE);

        centerPanel.add(northPanel, BorderLayout.NORTH);
        centerPanel.add(tablePanel, BorderLayout.CENTER);

        add(centerPanel, BorderLayout.CENTER);

        // Sự kiện nút
        btnThem.addActionListener(e -> themHoaDon());
        btnSua.addActionListener(e -> suaHoaDon());
        btnXoa.addActionListener(e -> xoaHoaDon());
        btnLamMoi.addActionListener(e -> lamMoi());
        btnTimKiem.addActionListener(e -> timKiem());

        txtTimKiem.addActionListener(e -> timKiem());

        tblHoaDon.getSelectionModel().addListSelectionListener(e -> {

            if (!e.getValueIsAdjusting()) {
                hienThiThongTin();
            }
        });
    }

    // Tải danh sách khách hàng
    private void loadKhachHang() {

        cboKhachHang.removeAllItems();

        List<KhachHang> danhSach = khachHangDAO.getAll();

        for (KhachHang kh : danhSach) {
            cboKhachHang.addItem(kh);
        }
    }

    // Tải danh sách nhân viên
    private void loadNhanVien() {

        cboNhanVien.removeAllItems();

        List<NhanVien> danhSach = nhanVienDAO.getAll();

        for (NhanVien nv : danhSach) {
            cboNhanVien.addItem(nv);
        }
    }

    // Tải danh sách hóa đơn
    private void loadData() {

        tableModel.setRowCount(0);

        List<HoaDon> danhSach = hoaDonDAO.getAll();

        for (HoaDon hd : danhSach) {

            tableModel.addRow(new Object[]{
                hd.getMaHD(),
                hd.getNgayLap(),
                hd.getMaKH(),
                hd.getMaNV(),
                hd.getTongTien()
            });
        }
    }

    // Thêm hóa đơn
    private void themHoaDon() {

        String ma = txtMaHD.getText().trim();
        String ngay = txtNgayLap.getText().trim();
        String tongTienText = txtTongTien.getText().trim();

        if (ma.isEmpty() || ngay.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Ma hoa don va ngay lap khong duoc de trong!"
            );

            return;
        }

        if (cboKhachHang.getSelectedItem() == null
                || cboNhanVien.getSelectedItem() == null) {

            JOptionPane.showMessageDialog(
                    this,
                    "Can co khach hang va nhan vien truoc khi tao hoa don!"
            );

            return;
        }

        try {

            Double tongTien = null;

            if (!tongTienText.isEmpty()) {

                tongTien = Double.parseDouble(tongTienText);

                if (tongTien < 0) {

                    JOptionPane.showMessageDialog(
                            this,
                            "Tong tien khong duoc am!"
                    );

                    return;
                }
            }

            KhachHang kh = (KhachHang) cboKhachHang.getSelectedItem();
            NhanVien nv = (NhanVien) cboNhanVien.getSelectedItem();

            HoaDon hd = new HoaDon(
                    ma,
                    ngay,
                    kh.getMaKH(),
                    nv.getMaNV(),
                    tongTien
            );

            if (hoaDonDAO.insert(hd)) {

                JOptionPane.showMessageDialog(
                        this,
                        "Them hoa don thanh cong!"
                );

                loadData();
                lamMoi();

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "Them hoa don that bai! Kiem tra ma hoa don."
                );
            }

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Tong tien phai la so hop le!"
            );
        }
    }

    // Sửa hóa đơn
    private void suaHoaDon() {

        String ma = txtMaHD.getText().trim();
        String ngay = txtNgayLap.getText().trim();
        String tongTienText = txtTongTien.getText().trim();

        if (ma.isEmpty() || ngay.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Vui long chon hoa don va nhap ngay lap!"
            );

            return;
        }

        try {

            Double tongTien = null;

            if (!tongTienText.isEmpty()) {

                tongTien = Double.parseDouble(tongTienText);

                if (tongTien < 0) {

                    JOptionPane.showMessageDialog(
                            this,
                            "Tong tien khong duoc am!"
                    );

                    return;
                }
            }

            KhachHang kh = (KhachHang) cboKhachHang.getSelectedItem();
            NhanVien nv = (NhanVien) cboNhanVien.getSelectedItem();

            HoaDon hd = new HoaDon(
                    ma,
                    ngay,
                    kh == null ? null : kh.getMaKH(),
                    nv == null ? null : nv.getMaNV(),
                    tongTien
            );

            if (hoaDonDAO.update(hd)) {

                JOptionPane.showMessageDialog(
                        this,
                        "Cap nhat hoa don thanh cong!"
                );

                loadData();
                lamMoi();

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "Cap nhat hoa don that bai!"
                );
            }

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Tong tien phai la so hop le!"
            );
        }
    }

    // Xóa hóa đơn
    private void xoaHoaDon() {

        String ma = txtMaHD.getText().trim();

        if (ma.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Vui long chon hoa don can xoa!"
            );

            return;
        }

        int choice = JOptionPane.showConfirmDialog(
                this,
                "Ban co chac chan muon xoa hoa don nay?",
                "Xac nhan xoa",
                JOptionPane.YES_NO_OPTION
        );

        if (choice == JOptionPane.YES_OPTION) {

            if (hoaDonDAO.delete(ma)) {

                JOptionPane.showMessageDialog(
                        this,
                        "Xoa hoa don thanh cong!"
                );

                loadData();
                lamMoi();

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "Khong the xoa hoa don! "
                        + "Hay kiem tra chi tiet hoa don truoc."
                );
            }
        }
    }

    // Tìm kiếm hóa đơn theo mã khách hàng
    private void timKiem() {

        String maKH = txtTimKiem.getText().trim();

        if (maKH.isEmpty()) {
            loadData();
            return;
        }

        tableModel.setRowCount(0);

        List<HoaDon> danhSach = hoaDonDAO.getByMaKH(maKH);

        for (HoaDon hd : danhSach) {

            tableModel.addRow(new Object[]{
                hd.getMaHD(),
                hd.getNgayLap(),
                hd.getMaKH(),
                hd.getMaNV(),
                hd.getTongTien()
            });
        }
    }

    // Hiển thị thông tin lên form
    private void hienThiThongTin() {

        int row = tblHoaDon.getSelectedRow();

        if (row >= 0) {

            txtMaHD.setText(
                    tableModel.getValueAt(row, 0).toString()
            );

            txtNgayLap.setText(
                    tableModel.getValueAt(row, 1).toString()
            );

            txtTongTien.setText(
                    tableModel.getValueAt(row, 4) == null
                    ? ""
                    : tableModel.getValueAt(row, 4).toString()
            );

            String maKH = tableModel.getValueAt(row, 2) == null
                    ? null
                    : tableModel.getValueAt(row, 2).toString();

            String maNV = tableModel.getValueAt(row, 3) == null
                    ? null
                    : tableModel.getValueAt(row, 3).toString();

            for (int i = 0; i < cboKhachHang.getItemCount(); i++) {

                KhachHang kh = cboKhachHang.getItemAt(i);

                if (maKH != null && kh.getMaKH().equals(maKH)) {
                    cboKhachHang.setSelectedIndex(i);
                    break;
                }
            }

            for (int i = 0; i < cboNhanVien.getItemCount(); i++) {

                NhanVien nv = cboNhanVien.getItemAt(i);

                if (maNV != null && nv.getMaNV().equals(maNV)) {
                    cboNhanVien.setSelectedIndex(i);
                    break;
                }
            }

            txtMaHD.setEditable(false);
        }
    }

    // Làm mới form
    private void lamMoi() {

        txtMaHD.setText("");
        txtNgayLap.setText("");
        txtTongTien.setText("");
        txtTimKiem.setText("");

        txtMaHD.setEditable(true);

        tblHoaDon.clearSelection();

        loadKhachHang();
        loadNhanVien();
    }
}
