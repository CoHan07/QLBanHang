
package view;

import dao.NhanVienDAO;
import model.NhanVien;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class NhanVienView extends JPanel {

    private JTextField txtMaNV;
    private JTextField txtTenNV;
    private JTextField txtChucVu;
    private JTextField txtSDT;
    private JTextField txtTimKiem;

    private JComboBox<String> cboGioiTinh;

    private JTable tblNhanVien;
    private DefaultTableModel tableModel;

    private JButton btnThem;
    private JButton btnSua;
    private JButton btnXoa;
    private JButton btnLamMoi;
    private JButton btnTimKiem;

    private NhanVienDAO nhanVienDAO;

    public NhanVienView() {

        nhanVienDAO = new NhanVienDAO();

        initComponents();
        loadData();
    }

    private void initComponents() {

        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel lblTitle = new JLabel(
                "QUAN LY NHAN VIEN",
                SwingConstants.CENTER
        );

        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitle.setForeground(new Color(35, 90, 160));

        add(lblTitle, BorderLayout.NORTH);

        // Panel nhap thong tin
        JPanel inputPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        inputPanel.setBackground(Color.WHITE);

        inputPanel.setBorder(
                BorderFactory.createTitledBorder("Thong tin nhan vien")
        );

        txtMaNV = new JTextField();
        txtTenNV = new JTextField();
        txtChucVu = new JTextField();
        txtSDT = new JTextField();

        cboGioiTinh = new JComboBox<>(
                new String[]{"Nam", "Nu"}
        );

        inputPanel.add(new JLabel("Ma nhan vien:"));
        inputPanel.add(txtMaNV);

        inputPanel.add(new JLabel("Ten nhan vien:"));
        inputPanel.add(txtTenNV);

        inputPanel.add(new JLabel("Chuc vu:"));
        inputPanel.add(txtChucVu);

        inputPanel.add(new JLabel("Gioi tinh:"));
        inputPanel.add(cboGioiTinh);

        inputPanel.add(new JLabel("So dien thoai:"));
        inputPanel.add(txtSDT);

        // Panel nut chuc nang
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

        // Panel tren
        JPanel topPanel = new JPanel(new BorderLayout(5, 5));
        topPanel.setBackground(Color.WHITE);

        topPanel.add(inputPanel, BorderLayout.CENTER);
        topPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Panel tim kiem
        JPanel searchPanel = new JPanel(
                new FlowLayout(FlowLayout.LEFT, 10, 5)
        );

        searchPanel.setBackground(Color.WHITE);

        txtTimKiem = new JTextField(25);
        btnTimKiem = new JButton("Tim kiem");

        searchPanel.add(new JLabel("Tim theo ten:"));
        searchPanel.add(txtTimKiem);
        searchPanel.add(btnTimKiem);

        // Bang nhan vien
        String[] columns = {
            "Ma NV",
            "Ten nhan vien",
            "Chuc vu",
            "Gioi tinh",
            "So dien thoai"
        };

        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tblNhanVien = new JTable(tableModel);
        tblNhanVien.setRowHeight(28);

        tblNhanVien.getTableHeader().setFont(
                new Font("Arial", Font.BOLD, 13)
        );

        JScrollPane scrollPane = new JScrollPane(tblNhanVien);

        scrollPane.setBorder(
                BorderFactory.createTitledBorder("Danh sach nhan vien")
        );

        // Panel trung tam
        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));
        centerPanel.setBackground(Color.WHITE);

        centerPanel.add(topPanel, BorderLayout.NORTH);
        centerPanel.add(searchPanel, BorderLayout.CENTER);
        centerPanel.add(scrollPane, BorderLayout.SOUTH);

        add(centerPanel, BorderLayout.CENTER);

        // Su kien nut
        btnThem.addActionListener(e -> themNhanVien());
        btnSua.addActionListener(e -> suaNhanVien());
        btnXoa.addActionListener(e -> xoaNhanVien());
        btnLamMoi.addActionListener(e -> lamMoi());
        btnTimKiem.addActionListener(e -> timKiem());

        txtTimKiem.addActionListener(e -> timKiem());

        tblNhanVien.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                hienThiThongTin();
            }
        });
    }

    // Hien thi danh sach nhan vien
    private void loadData() {

        tableModel.setRowCount(0);

        List<NhanVien> danhSach = nhanVienDAO.getAll();

        for (NhanVien nv : danhSach) {

            String gioiTinh = "";

            if (nv.getGioiTinh() != null) {
                gioiTinh = nv.getGioiTinh() ? "Nam" : "Nu";
            }

            tableModel.addRow(new Object[]{
                nv.getMaNV(),
                nv.getTenNV(),
                nv.getChucVu(),
                gioiTinh,
                nv.getSdt()
            });
        }
    }

    // Them nhan vien
    private void themNhanVien() {

        String ma = txtMaNV.getText().trim();
        String ten = txtTenNV.getText().trim();
        String chucVu = txtChucVu.getText().trim();
        String sdt = txtSDT.getText().trim();

        if (ma.isEmpty() || ten.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Ma nhan vien va ten nhan vien khong duoc de trong!"
            );

            return;
        }

        Boolean gioiTinh = cboGioiTinh.getSelectedIndex() == 0;

        NhanVien nv = new NhanVien(
                ma,
                ten,
                chucVu,
                gioiTinh,
                sdt
        );

        if (nhanVienDAO.insert(nv)) {

            JOptionPane.showMessageDialog(
                    this,
                    "Them nhan vien thanh cong!"
            );

            loadData();
            lamMoi();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "Them nhan vien that bai! Kiem tra ma nhan vien."
            );
        }
    }

    // Sua nhan vien
    private void suaNhanVien() {

        String ma = txtMaNV.getText().trim();
        String ten = txtTenNV.getText().trim();
        String chucVu = txtChucVu.getText().trim();
        String sdt = txtSDT.getText().trim();

        if (ma.isEmpty() || ten.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Vui long chon nhan vien va nhap day du thong tin!"
            );

            return;
        }

        Boolean gioiTinh = cboGioiTinh.getSelectedIndex() == 0;

        NhanVien nv = new NhanVien(
                ma,
                ten,
                chucVu,
                gioiTinh,
                sdt
        );

        if (nhanVienDAO.update(nv)) {

            JOptionPane.showMessageDialog(
                    this,
                    "Cap nhat nhan vien thanh cong!"
            );

            loadData();
            lamMoi();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "Cap nhat nhan vien that bai!"
            );
        }
    }

    // Xoa nhan vien
    private void xoaNhanVien() {

        String ma = txtMaNV.getText().trim();

        if (ma.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Vui long chon nhan vien can xoa!"
            );

            return;
        }

        int choice = JOptionPane.showConfirmDialog(
                this,
                "Ban co chac chan muon xoa nhan vien nay?",
                "Xac nhan xoa",
                JOptionPane.YES_NO_OPTION
        );

        if (choice == JOptionPane.YES_OPTION) {

            if (nhanVienDAO.delete(ma)) {

                JOptionPane.showMessageDialog(
                        this,
                        "Xoa nhan vien thanh cong!"
                );

                loadData();
                lamMoi();

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "Khong the xoa nhan vien! "
                        + "Nhan vien co the dang lap hoa don."
                );
            }
        }
    }

    // Tim kiem nhan vien
    private void timKiem() {

        String ten = txtTimKiem.getText().trim();

        if (ten.isEmpty()) {
            loadData();
            return;
        }

        tableModel.setRowCount(0);

        List<NhanVien> danhSach =
                nhanVienDAO.searchByName(ten);

        for (NhanVien nv : danhSach) {

            String gioiTinh = "";

            if (nv.getGioiTinh() != null) {
                gioiTinh = nv.getGioiTinh() ? "Nam" : "Nu";
            }

            tableModel.addRow(new Object[]{
                nv.getMaNV(),
                nv.getTenNV(),
                nv.getChucVu(),
                gioiTinh,
                nv.getSdt()
            });
        }
    }

    // Hien thi thong tin len form
    private void hienThiThongTin() {

        int row = tblNhanVien.getSelectedRow();

        if (row >= 0) {

            txtMaNV.setText(
                    tableModel.getValueAt(row, 0).toString()
            );

            txtTenNV.setText(
                    tableModel.getValueAt(row, 1).toString()
            );

            txtChucVu.setText(
                    tableModel.getValueAt(row, 2) == null
                    ? ""
                    : tableModel.getValueAt(row, 2).toString()
            );

            String gioiTinh = tableModel.getValueAt(row, 3).toString();

            if (gioiTinh.equals("Nam")) {
                cboGioiTinh.setSelectedIndex(0);
            } else {
                cboGioiTinh.setSelectedIndex(1);
            }

            txtSDT.setText(
                    tableModel.getValueAt(row, 4) == null
                    ? ""
                    : tableModel.getValueAt(row, 4).toString()
            );

            txtMaNV.setEditable(false);
        }
    }

    // Lam moi form
    private void lamMoi() {

        txtMaNV.setText("");
        txtTenNV.setText("");
        txtChucVu.setText("");
        txtSDT.setText("");
        txtTimKiem.setText("");

        cboGioiTinh.setSelectedIndex(0);

        txtMaNV.setEditable(true);

        tblNhanVien.clearSelection();
    }
}
