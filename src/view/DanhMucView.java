
package view;

import dao.DanhMucDAO;
import model.DanhMuc;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class DanhMucView extends JPanel {

    private JTextField txtMaDanhMuc;
    private JTextField txtTenDanhMuc;

    private JTable tblDanhMuc;
    private DefaultTableModel tableModel;

    private JButton btnThem;
    private JButton btnSua;
    private JButton btnXoa;
    private JButton btnLamMoi;

    private DanhMucDAO danhMucDAO;

    public DanhMucView() {
        danhMucDAO = new DanhMucDAO();

        initComponents();
        loadData();
    }

    private void initComponents() {

        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel lblTitle = new JLabel(
                "QUAN LY DANH MUC",
                SwingConstants.CENTER
        );

        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitle.setForeground(new Color(35, 90, 160));

        add(lblTitle, BorderLayout.NORTH);

        // Panel nhap thong tin
        JPanel inputPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        inputPanel.setBackground(Color.WHITE);
        inputPanel.setBorder(
                BorderFactory.createTitledBorder("Thong tin danh muc")
        );

        JLabel lblMa = new JLabel("Ma danh muc:");
        JLabel lblTen = new JLabel("Ten danh muc:");

        txtMaDanhMuc = new JTextField();
        txtTenDanhMuc = new JTextField();

        inputPanel.add(lblMa);
        inputPanel.add(txtMaDanhMuc);
        inputPanel.add(lblTen);
        inputPanel.add(txtTenDanhMuc);

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
        JPanel topPanel = new JPanel(new BorderLayout(10, 10));
        topPanel.setBackground(Color.WHITE);

        topPanel.add(inputPanel, BorderLayout.CENTER);
        topPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Bang danh muc
        String[] columns = {
            "Ma danh muc",
            "Ten danh muc"
        };

        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tblDanhMuc = new JTable(tableModel);
        tblDanhMuc.setRowHeight(28);
        tblDanhMuc.setFont(new Font("Arial", Font.PLAIN, 13));
        tblDanhMuc.getTableHeader().setFont(
                new Font("Arial", Font.BOLD, 13)
        );

        JScrollPane scrollPane = new JScrollPane(tblDanhMuc);

        scrollPane.setBorder(
                BorderFactory.createTitledBorder("Danh sach danh muc")
        );

        // Bo cuc
        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));
        centerPanel.setBackground(Color.WHITE);

        centerPanel.add(topPanel, BorderLayout.NORTH);
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        add(centerPanel, BorderLayout.CENTER);

        // Su kien nut
        btnThem.addActionListener(e -> themDanhMuc());
        btnSua.addActionListener(e -> suaDanhMuc());
        btnXoa.addActionListener(e -> xoaDanhMuc());
        btnLamMoi.addActionListener(e -> lamMoi());

        // Su kien chon dong
        tblDanhMuc.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                hienThiThongTin();
            }
        });
    }

    // Hien thi du lieu
    private void loadData() {

        tableModel.setRowCount(0);

        List<DanhMuc> danhSach = danhMucDAO.getAll();

        for (DanhMuc dm : danhSach) {
            tableModel.addRow(new Object[]{
                dm.getMaDanhMuc(),
                dm.getTenDanhMuc()
            });
        }
    }

    // Them danh muc
    private void themDanhMuc() {

        String ma = txtMaDanhMuc.getText().trim();
        String ten = txtTenDanhMuc.getText().trim();

        if (ma.isEmpty() || ten.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Vui long nhap day du thong tin!"
            );
            return;
        }

        DanhMuc dm = new DanhMuc(ma, ten);

        if (danhMucDAO.insert(dm)) {
            JOptionPane.showMessageDialog(
                    this,
                    "Them danh muc thanh cong!"
            );

            loadData();
            lamMoi();
        } else {
            JOptionPane.showMessageDialog(
                    this,
                    "Them danh muc that bai! Kiem tra ma danh muc."
            );
        }
    }

    // Sua danh muc
    private void suaDanhMuc() {

        String ma = txtMaDanhMuc.getText().trim();
        String ten = txtTenDanhMuc.getText().trim();

        if (ma.isEmpty() || ten.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Vui long chon danh muc va nhap day du thong tin!"
            );
            return;
        }

        DanhMuc dm = new DanhMuc(ma, ten);

        if (danhMucDAO.update(dm)) {
            JOptionPane.showMessageDialog(
                    this,
                    "Cap nhat danh muc thanh cong!"
            );

            loadData();
            lamMoi();
        } else {
            JOptionPane.showMessageDialog(
                    this,
                    "Cap nhat danh muc that bai!"
            );
        }
    }

    // Xoa danh muc
    private void xoaDanhMuc() {

        String ma = txtMaDanhMuc.getText().trim();

        if (ma.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Vui long chon danh muc can xoa!"
            );
            return;
        }

        int choice = JOptionPane.showConfirmDialog(
                this,
                "Ban co chac chan muon xoa danh muc nay?",
                "Xac nhan xoa",
                JOptionPane.YES_NO_OPTION
        );

        if (choice == JOptionPane.YES_OPTION) {

            if (danhMucDAO.delete(ma)) {
                JOptionPane.showMessageDialog(
                        this,
                        "Xoa danh muc thanh cong!"
                );

                loadData();
                lamMoi();

            } else {
                JOptionPane.showMessageDialog(
                        this,
                        "Khong the xoa danh muc! "
                        + "Co the danh muc dang duoc su dung boi san pham."
                );
            }
        }
    }

    // Dua thong tin tu bang len o nhap
    private void hienThiThongTin() {

        int row = tblDanhMuc.getSelectedRow();

        if (row >= 0) {
            txtMaDanhMuc.setText(
                    tableModel.getValueAt(row, 0).toString()
            );

            txtTenDanhMuc.setText(
                    tableModel.getValueAt(row, 1).toString()
            );

            txtMaDanhMuc.setEditable(false);
        }
    }

    // Lam moi
    private void lamMoi() {

        txtMaDanhMuc.setText("");
        txtTenDanhMuc.setText("");

        txtMaDanhMuc.setEditable(true);

        tblDanhMuc.clearSelection();
    }
}
