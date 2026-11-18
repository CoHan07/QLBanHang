
package view;

import dao.SanPhamDAO;
import dao.DanhMucDAO;
import model.SanPham;
import model.DanhMuc;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.Locale;

public class SanPhamView extends JPanel {

    private JTextField txtMaSP;
    private JTextField txtTenSP;
    private JTextField txtGiaBan;
    private JTextField txtSoLuong;
    private JTextField txtTimKiem;

    private JComboBox<DanhMuc> cboDanhMuc;

    private JTable tblSanPham;
    private DefaultTableModel tableModel;

    private JButton btnThem;
    private JButton btnSua;
    private JButton btnXoa;
    private JButton btnLamMoi;
    private JButton btnTimKiem;

    private SanPhamDAO sanPhamDAO;
    private DanhMucDAO danhMucDAO;

    public SanPhamView() {

        sanPhamDAO = new SanPhamDAO();
        danhMucDAO = new DanhMucDAO();

        initComponents();
        loadDanhMuc();
        loadData();
    }

    // Dinh dang gia ban
    private String dinhDangGia(double gia) {
        return String.format(Locale.US, "%.0f", gia);
    }

    private void initComponents() {

        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel lblTitle = new JLabel(
                "QUAN LY SAN PHAM",
                SwingConstants.CENTER
        );

        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitle.setForeground(new Color(35, 90, 160));

        add(lblTitle, BorderLayout.NORTH);

        // Panel nhap thong tin
        JPanel inputPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        inputPanel.setBackground(Color.WHITE);
        inputPanel.setBorder(
                BorderFactory.createTitledBorder("Thong tin san pham")
        );

        txtMaSP = new JTextField();
        txtTenSP = new JTextField();
        txtGiaBan = new JTextField();
        txtSoLuong = new JTextField();

        cboDanhMuc = new JComboBox<>();

        inputPanel.add(new JLabel("Ma san pham:"));
        inputPanel.add(txtMaSP);

        inputPanel.add(new JLabel("Ten san pham:"));
        inputPanel.add(txtTenSP);

        inputPanel.add(new JLabel("Gia ban:"));
        inputPanel.add(txtGiaBan);

        inputPanel.add(new JLabel("So luong ton:"));
        inputPanel.add(txtSoLuong);

        inputPanel.add(new JLabel("Danh muc:"));
        inputPanel.add(cboDanhMuc);

        // Panel nut chuc nang
        JPanel buttonPanel = new JPanel(
                new FlowLayout(FlowLayout.CENTER, 10, 10)
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

        txtTimKiem = new JTextField(20);
        btnTimKiem = new JButton("Tim kiem");

        searchPanel.add(new JLabel("Tim theo ten:"));
        searchPanel.add(txtTimKiem);
        searchPanel.add(btnTimKiem);

        // Panel phia tren
        JPanel northPanel = new JPanel(new BorderLayout(5, 5));
        northPanel.setBackground(Color.WHITE);

        northPanel.add(topPanel, BorderLayout.CENTER);
        northPanel.add(searchPanel, BorderLayout.SOUTH);

        // Bang san pham
        String[] columns = {
            "Ma SP",
            "Ten san pham",
            "Gia ban",
            "So luong ton",
            "Ma danh muc",
            "Ten danh muc"
        };

        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tblSanPham = new JTable(tableModel);
        tblSanPham.setRowHeight(28);
        tblSanPham.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        tblSanPham.getTableHeader().setFont(
                new Font("Arial", Font.BOLD, 13)
        );

        tblSanPham.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        JScrollPane scrollPane = new JScrollPane(tblSanPham);

        scrollPane.setBorder(
                BorderFactory.createTitledBorder("Danh sach san pham")
        );

        // Panel chua bang
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(Color.WHITE);
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        // Panel trung tam
        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));
        centerPanel.setBackground(Color.WHITE);

        centerPanel.add(northPanel, BorderLayout.NORTH);
        centerPanel.add(tablePanel, BorderLayout.CENTER);

        add(centerPanel, BorderLayout.CENTER);

        // Su kien nut
        btnThem.addActionListener(e -> themSanPham());
        btnSua.addActionListener(e -> suaSanPham());
        btnXoa.addActionListener(e -> xoaSanPham());
        btnLamMoi.addActionListener(e -> lamMoi());
        btnTimKiem.addActionListener(e -> timKiem());

        txtTimKiem.addActionListener(e -> timKiem());

        tblSanPham.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                hienThiThongTin();
            }
        });
    }

    // Tai danh sach danh muc
    private void loadDanhMuc() {

        cboDanhMuc.removeAllItems();

        List<DanhMuc> danhSach = danhMucDAO.getAll();

        for (DanhMuc dm : danhSach) {
            cboDanhMuc.addItem(dm);
        }
    }

    // Tai du lieu san pham
    private void loadData() {

        tableModel.setRowCount(0);

        List<SanPham> danhSach = sanPhamDAO.getAll();

        for (SanPham sp : danhSach) {

            String tenDanhMuc = "";

            for (int i = 0; i < cboDanhMuc.getItemCount(); i++) {

                DanhMuc dm = cboDanhMuc.getItemAt(i);

                if (dm.getMaDanhMuc().equals(sp.getMaDanhMuc())) {
                    tenDanhMuc = dm.getTenDanhMuc();
                    break;
                }
            }

            tableModel.addRow(new Object[]{
                sp.getMaSP(),
                sp.getTenSP(),
                dinhDangGia(sp.getGiaBan()),
                sp.getSoLuongTon(),
                sp.getMaDanhMuc(),
                tenDanhMuc
            });
        }
    }

    // Them san pham
    private void themSanPham() {

        try {

            String ma = txtMaSP.getText().trim();
            String ten = txtTenSP.getText().trim();
            String giaText = txtGiaBan.getText().trim();
            String soLuongText = txtSoLuong.getText().trim();

            DanhMuc dm = (DanhMuc) cboDanhMuc.getSelectedItem();

            if (ma.isEmpty() || ten.isEmpty()
                    || giaText.isEmpty() || soLuongText.isEmpty()) {

                JOptionPane.showMessageDialog(
                        this,
                        "Vui long nhap day du thong tin!"
                );
                return;
            }

            if (dm == null) {
                JOptionPane.showMessageDialog(
                        this,
                        "Vui long tao danh muc truoc khi them san pham!"
                );
                return;
            }

            double gia = Double.parseDouble(giaText);
            int soLuong = Integer.parseInt(soLuongText);

            if (gia < 0 || soLuong < 0) {
                JOptionPane.showMessageDialog(
                        this,
                        "Gia ban va so luong khong duoc am!"
                );
                return;
            }

            SanPham sp = new SanPham(
                    ma,
                    ten,
                    gia,
                    soLuong,
                    dm.getMaDanhMuc()
            );

            if (sanPhamDAO.insert(sp)) {

                JOptionPane.showMessageDialog(
                        this,
                        "Them san pham thanh cong!"
                );

                loadData();
                lamMoi();

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "Them san pham that bai! Kiem tra ma san pham."
                );
            }

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Gia ban phai la so va so luong phai la so nguyen!"
            );
        }
    }

    // Sua san pham
    private void suaSanPham() {

        try {

            String ma = txtMaSP.getText().trim();
            String ten = txtTenSP.getText().trim();

            DanhMuc dm = (DanhMuc) cboDanhMuc.getSelectedItem();

            if (ma.isEmpty() || ten.isEmpty()
                    || txtGiaBan.getText().trim().isEmpty()
                    || txtSoLuong.getText().trim().isEmpty()) {

                JOptionPane.showMessageDialog(
                        this,
                        "Vui long chon san pham va nhap day du thong tin!"
                );
                return;
            }

            if (dm == null) {
                JOptionPane.showMessageDialog(
                        this,
                        "Chua co danh muc san pham!"
                );
                return;
            }

            double gia = Double.parseDouble(
                    txtGiaBan.getText().trim()
            );

            int soLuong = Integer.parseInt(
                    txtSoLuong.getText().trim()
            );

            if (gia < 0 || soLuong < 0) {
                JOptionPane.showMessageDialog(
                        this,
                        "Gia ban va so luong khong duoc am!"
                );
                return;
            }

            SanPham sp = new SanPham(
                    ma,
                    ten,
                    gia,
                    soLuong,
                    dm.getMaDanhMuc()
            );

            if (sanPhamDAO.update(sp)) {

                JOptionPane.showMessageDialog(
                        this,
                        "Cap nhat san pham thanh cong!"
                );

                loadData();
                lamMoi();

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "Cap nhat san pham that bai!"
                );
            }

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Gia ban hoac so luong khong hop le!"
            );
        }
    }

    // Xoa san pham
    private void xoaSanPham() {

        String ma = txtMaSP.getText().trim();

        if (ma.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Vui long chon san pham can xoa!"
            );
            return;
        }

        int choice = JOptionPane.showConfirmDialog(
                this,
                "Ban co chac chan muon xoa san pham nay?",
                "Xac nhan xoa",
                JOptionPane.YES_NO_OPTION
        );

        if (choice == JOptionPane.YES_OPTION) {

            if (sanPhamDAO.delete(ma)) {

                JOptionPane.showMessageDialog(
                        this,
                        "Xoa san pham thanh cong!"
                );

                loadData();
                lamMoi();

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "Khong the xoa san pham! "
                        + "San pham co the dang nam trong hoa don."
                );
            }
        }
    }

    // Tim kiem san pham
    private void timKiem() {

        String ten = txtTimKiem.getText().trim();

        if (ten.isEmpty()) {
            loadData();
            return;
        }

        tableModel.setRowCount(0);

        List<SanPham> danhSach = sanPhamDAO.searchByName(ten);

        for (SanPham sp : danhSach) {

            String tenDanhMuc = "";

            for (int i = 0; i < cboDanhMuc.getItemCount(); i++) {

                DanhMuc dm = cboDanhMuc.getItemAt(i);

                if (dm.getMaDanhMuc().equals(sp.getMaDanhMuc())) {
                    tenDanhMuc = dm.getTenDanhMuc();
                    break;
                }
            }

            tableModel.addRow(new Object[]{
                sp.getMaSP(),
                sp.getTenSP(),
                dinhDangGia(sp.getGiaBan()),
                sp.getSoLuongTon(),
                sp.getMaDanhMuc(),
                tenDanhMuc
            });
        }
    }

    // Hien thi thong tin len form
    private void hienThiThongTin() {

        int row = tblSanPham.getSelectedRow();

        if (row >= 0) {

            txtMaSP.setText(
                    tableModel.getValueAt(row, 0).toString()
            );

            txtTenSP.setText(
                    tableModel.getValueAt(row, 1).toString()
            );

            txtGiaBan.setText(
                    tableModel.getValueAt(row, 2).toString()
            );

            txtSoLuong.setText(
                    tableModel.getValueAt(row, 3).toString()
            );

            String maDanhMuc =
                    tableModel.getValueAt(row, 4).toString();

            for (int i = 0; i < cboDanhMuc.getItemCount(); i++) {

                DanhMuc dm = cboDanhMuc.getItemAt(i);

                if (dm.getMaDanhMuc().equals(maDanhMuc)) {
                    cboDanhMuc.setSelectedIndex(i);
                    break;
                }
            }

            txtMaSP.setEditable(false);
        }
    }

    // Lam moi form
    private void lamMoi() {

        txtMaSP.setText("");
        txtTenSP.setText("");
        txtGiaBan.setText("");
        txtSoLuong.setText("");
        txtTimKiem.setText("");

        txtMaSP.setEditable(true);

        tblSanPham.clearSelection();

        loadDanhMuc();
        loadData();
    }
}
