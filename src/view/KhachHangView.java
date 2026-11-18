
package view;

import dao.KhachHangDAO;
import model.KhachHang;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class KhachHangView extends JPanel {

    private JTextField txtMaKH;
    private JTextField txtTenKH;
    private JTextField txtSDT;
    private JTextField txtDiaChi;
    private JTextField txtEmail;
    private JTextField txtTimKiem;

    private JTable tblKhachHang;
    private DefaultTableModel tableModel;

    private JButton btnThem;
    private JButton btnSua;
    private JButton btnXoa;
    private JButton btnLamMoi;
    private JButton btnTimKiem;

    private KhachHangDAO khachHangDAO;

    public KhachHangView() {

        khachHangDAO = new KhachHangDAO();

        initComponents();
        loadData();
    }

    private void initComponents() {

        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel lblTitle = new JLabel(
                "QUAN LY KHACH HANG",
                SwingConstants.CENTER
        );

        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitle.setForeground(new Color(35, 90, 160));

        add(lblTitle, BorderLayout.NORTH);

        // Panel nhap thong tin
        JPanel inputPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        inputPanel.setBackground(Color.WHITE);

        inputPanel.setBorder(
                BorderFactory.createTitledBorder("Thong tin khach hang")
        );

        txtMaKH = new JTextField();
        txtTenKH = new JTextField();
        txtSDT = new JTextField();
        txtDiaChi = new JTextField();
        txtEmail = new JTextField();

        inputPanel.add(new JLabel("Ma khach hang:"));
        inputPanel.add(txtMaKH);

        inputPanel.add(new JLabel("Ten khach hang:"));
        inputPanel.add(txtTenKH);

        inputPanel.add(new JLabel("So dien thoai:"));
        inputPanel.add(txtSDT);

        inputPanel.add(new JLabel("Dia chi:"));
        inputPanel.add(txtDiaChi);

        inputPanel.add(new JLabel("Email:"));
        inputPanel.add(txtEmail);

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

        // Panel nhap lieu va nut
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

        // Gop panel nhap lieu va tim kiem
        JPanel northPanel = new JPanel(new BorderLayout(5, 5));
        northPanel.setBackground(Color.WHITE);

        northPanel.add(topPanel, BorderLayout.CENTER);
        northPanel.add(searchPanel, BorderLayout.SOUTH);

        // Bang khach hang
        String[] columns = {
            "Ma KH",
            "Ten khach hang",
            "So dien thoai",
            "Dia chi",
            "Email"
        };

        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tblKhachHang = new JTable(tableModel);
        tblKhachHang.setRowHeight(28);

        tblKhachHang.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        tblKhachHang.getTableHeader().setFont(
                new Font("Arial", Font.BOLD, 13)
        );

        JScrollPane scrollPane = new JScrollPane(tblKhachHang);

        scrollPane.setBorder(
                BorderFactory.createTitledBorder("Danh sach khach hang")
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
        btnThem.addActionListener(e -> themKhachHang());
        btnSua.addActionListener(e -> suaKhachHang());
        btnXoa.addActionListener(e -> xoaKhachHang());
        btnLamMoi.addActionListener(e -> lamMoi());
        btnTimKiem.addActionListener(e -> timKiem());

        txtTimKiem.addActionListener(e -> timKiem());

        // Su kien chon dong
        tblKhachHang.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                hienThiThongTin();
            }
        });
    }

    // Hien thi danh sach khach hang
    private void loadData() {

        tableModel.setRowCount(0);

        List<KhachHang> danhSach = khachHangDAO.getAll();

        for (KhachHang kh : danhSach) {

            tableModel.addRow(new Object[]{
                kh.getMaKH(),
                kh.getTenKH(),
                kh.getSdt(),
                kh.getDiaChi(),
                kh.getEmail()
            });
        }
    }

    // Them khach hang
    private void themKhachHang() {

        String ma = txtMaKH.getText().trim();
        String ten = txtTenKH.getText().trim();
        String sdt = txtSDT.getText().trim();
        String diaChi = txtDiaChi.getText().trim();
        String email = txtEmail.getText().trim();

        if (ma.isEmpty() || ten.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Ma khach hang va ten khach hang khong duoc de trong!"
            );

            return;
        }

        KhachHang kh = new KhachHang(
                ma,
                ten,
                sdt,
                diaChi,
                email
        );

        if (khachHangDAO.insert(kh)) {

            JOptionPane.showMessageDialog(
                    this,
                    "Them khach hang thanh cong!"
            );

            loadData();
            lamMoi();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "Them khach hang that bai! Kiem tra ma khach hang."
            );
        }
    }

    // Sua khach hang
    private void suaKhachHang() {

        String ma = txtMaKH.getText().trim();
        String ten = txtTenKH.getText().trim();
        String sdt = txtSDT.getText().trim();
        String diaChi = txtDiaChi.getText().trim();
        String email = txtEmail.getText().trim();

        if (ma.isEmpty() || ten.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Vui long chon khach hang va nhap ten khach hang!"
            );

            return;
        }

        KhachHang kh = new KhachHang(
                ma,
                ten,
                sdt,
                diaChi,
                email
        );

        if (khachHangDAO.update(kh)) {

            JOptionPane.showMessageDialog(
                    this,
                    "Cap nhat khach hang thanh cong!"
            );

            loadData();
            lamMoi();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "Cap nhat khach hang that bai!"
            );
        }
    }

    // Xoa khach hang
    private void xoaKhachHang() {

        String ma = txtMaKH.getText().trim();

        if (ma.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Vui long chon khach hang can xoa!"
            );

            return;
        }

        int choice = JOptionPane.showConfirmDialog(
                this,
                "Ban co chac chan muon xoa khach hang nay?",
                "Xac nhan xoa",
                JOptionPane.YES_NO_OPTION
        );

        if (choice == JOptionPane.YES_OPTION) {

            if (khachHangDAO.delete(ma)) {

                JOptionPane.showMessageDialog(
                        this,
                        "Xoa khach hang thanh cong!"
                );

                loadData();
                lamMoi();

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "Khong the xoa khach hang! "
                        + "Khach hang co the dang co hoa don."
                );
            }
        }
    }

    // Tim kiem khach hang
    private void timKiem() {

        String ten = txtTimKiem.getText().trim();

        if (ten.isEmpty()) {
            loadData();
            return;
        }

        tableModel.setRowCount(0);

        List<KhachHang> danhSach =
                khachHangDAO.searchByName(ten);

        for (KhachHang kh : danhSach) {

            tableModel.addRow(new Object[]{
                kh.getMaKH(),
                kh.getTenKH(),
                kh.getSdt(),
                kh.getDiaChi(),
                kh.getEmail()
            });
        }
    }

    // Hien thi thong tin len form
    private void hienThiThongTin() {

        int row = tblKhachHang.getSelectedRow();

        if (row >= 0) {

            txtMaKH.setText(
                    tableModel.getValueAt(row, 0).toString()
            );

            txtTenKH.setText(
                    tableModel.getValueAt(row, 1).toString()
            );

            txtSDT.setText(
                    tableModel.getValueAt(row, 2) == null
                    ? ""
                    : tableModel.getValueAt(row, 2).toString()
            );

            txtDiaChi.setText(
                    tableModel.getValueAt(row, 3) == null
                    ? ""
                    : tableModel.getValueAt(row, 3).toString()
            );

            txtEmail.setText(
                    tableModel.getValueAt(row, 4) == null
                    ? ""
                    : tableModel.getValueAt(row, 4).toString()
            );

            txtMaKH.setEditable(false);
        }
    }

    // Lam moi form
    private void lamMoi() {

        txtMaKH.setText("");
        txtTenKH.setText("");
        txtSDT.setText("");
        txtDiaChi.setText("");
        txtEmail.setText("");
        txtTimKiem.setText("");

        txtMaKH.setEditable(true);

        tblKhachHang.clearSelection();
    }
}
