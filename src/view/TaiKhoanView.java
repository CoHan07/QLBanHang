
package view;

import dao.UserDAO;
import model.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class TaiKhoanView extends JPanel {

    private JTextField txtUsername;
    private JTextField txtName;
    private JPasswordField txtPassword;

    private JTable tblTaiKhoan;
    private DefaultTableModel tableModel;

    private JButton btnThem;
    private JButton btnSua;
    private JButton btnXoa;
    private JButton btnLamMoi;

    private UserDAO userDAO;

    public TaiKhoanView() {

        userDAO = new UserDAO();

        initComponents();
        loadData();
    }

    private void initComponents() {

        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel lblTitle = new JLabel(
                "QUAN LY TAI KHOAN",
                SwingConstants.CENTER
        );

        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitle.setForeground(new Color(35, 90, 160));

        add(lblTitle, BorderLayout.NORTH);

        // Panel nhap thong tin
        JPanel inputPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        inputPanel.setBackground(Color.WHITE);

        inputPanel.setBorder(
                BorderFactory.createTitledBorder("Thong tin tai khoan")
        );

        txtUsername = new JTextField();
        txtPassword = new JPasswordField();
        txtName = new JTextField();

        inputPanel.add(new JLabel("Ten dang nhap:"));
        inputPanel.add(txtUsername);

        inputPanel.add(new JLabel("Mat khau:"));
        inputPanel.add(txtPassword);

        inputPanel.add(new JLabel("Ten hien thi:"));
        inputPanel.add(txtName);

        // Panel nut
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

        // Bang tai khoan
        String[] columns = {
            "Ten dang nhap",
            "Mat khau",
            "Ten hien thi"
        };

        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tblTaiKhoan = new JTable(tableModel);
        tblTaiKhoan.setRowHeight(28);

        JScrollPane scrollPane = new JScrollPane(tblTaiKhoan);

        scrollPane.setBorder(
                BorderFactory.createTitledBorder("Danh sach tai khoan")
        );

        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));
        centerPanel.setBackground(Color.WHITE);

        centerPanel.add(topPanel, BorderLayout.NORTH);
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        add(centerPanel, BorderLayout.CENTER);

        // Su kien nut
        btnThem.addActionListener(e -> themTaiKhoan());
        btnSua.addActionListener(e -> suaTaiKhoan());
        btnXoa.addActionListener(e -> xoaTaiKhoan());
        btnLamMoi.addActionListener(e -> lamMoi());

        tblTaiKhoan.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                hienThiThongTin();
            }
        });
    }

    // Hien thi danh sach tai khoan
    private void loadData() {

        tableModel.setRowCount(0);

        List<User> danhSach = userDAO.getAll();

        for (User user : danhSach) {

            tableModel.addRow(new Object[]{
                user.getUsername(),
                user.getPassword(),
                user.getName()
            });
        }
    }

    // Them tai khoan
    private void themTaiKhoan() {

        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword());
        String name = txtName.getText().trim();

        if (username.isEmpty() || password.isEmpty() || name.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Vui long nhap day du thong tin!"
            );

            return;
        }

        User user = new User(username, password, name);

        if (userDAO.insert(user)) {

            JOptionPane.showMessageDialog(
                    this,
                    "Them tai khoan thanh cong!"
            );

            loadData();
            lamMoi();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "Them tai khoan that bai! "
                    + "Ten dang nhap co the da ton tai."
            );
        }
    }

    // Sua tai khoan
    private void suaTaiKhoan() {

        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword());
        String name = txtName.getText().trim();

        if (username.isEmpty() || password.isEmpty() || name.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Vui long nhap day du thong tin!"
            );

            return;
        }

        User user = new User(username, password, name);

        if (userDAO.update(user)) {

            JOptionPane.showMessageDialog(
                    this,
                    "Cap nhat tai khoan thanh cong!"
            );

            loadData();
            lamMoi();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "Cap nhat tai khoan that bai!"
            );
        }
    }

    // Xoa tai khoan
    private void xoaTaiKhoan() {

        String username = txtUsername.getText().trim();

        if (username.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Vui long chon tai khoan can xoa!"
            );

            return;
        }

        int choice = JOptionPane.showConfirmDialog(
                this,
                "Ban co chac chan muon xoa tai khoan nay?",
                "Xac nhan",
                JOptionPane.YES_NO_OPTION
        );

        if (choice == JOptionPane.YES_OPTION) {

            if (userDAO.delete(username)) {

                JOptionPane.showMessageDialog(
                        this,
                        "Xoa tai khoan thanh cong!"
                );

                loadData();
                lamMoi();

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "Xoa tai khoan that bai!"
                );
            }
        }
    }

    // Hien thi thong tin len form
    private void hienThiThongTin() {

        int row = tblTaiKhoan.getSelectedRow();

        if (row >= 0) {

            txtUsername.setText(
                    tableModel.getValueAt(row, 0).toString()
            );

            txtPassword.setText(
                    tableModel.getValueAt(row, 1).toString()
            );

            txtName.setText(
                    tableModel.getValueAt(row, 2).toString()
            );
        }
    }

    // Lam moi form
    private void lamMoi() {

        txtUsername.setText("");
        txtPassword.setText("");
        txtName.setText("");

        tblTaiKhoan.clearSelection();
    }
}
