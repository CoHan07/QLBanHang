
package view;

import dao.UserDAO;
import model.User;

import javax.swing.*;
import java.awt.*;

public class Login extends JFrame {

    private JTextField txtUsername;
    private JPasswordField txtPassword;

    private JButton btnLogin;
    private JButton btnExit;

    private UserDAO userDAO;

    public Login() {

        userDAO = new UserDAO();

        initComponents();
    }

    private void initComponents() {

        setTitle("Dang nhap - Quan ly ban hang");
        setSize(450, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Panel chinh
        JPanel mainPanel = new JPanel();

        mainPanel.setLayout(new BorderLayout(10, 10));
        mainPanel.setBackground(Color.WHITE);

        mainPanel.setBorder(
                BorderFactory.createEmptyBorder(25, 35, 25, 35)
        );

        // Tieu de
        JLabel lblTitle = new JLabel(
                "QUAN LY BAN HANG",
                SwingConstants.CENTER
        );

        lblTitle.setFont(
                new Font("Arial", Font.BOLD, 23)
        );

        lblTitle.setForeground(
                new Color(35, 90, 160)
        );

        JLabel lblSubtitle = new JLabel(
                "DANG NHAP HE THONG",
                SwingConstants.CENTER
        );

        lblSubtitle.setFont(
                new Font("Arial", Font.PLAIN, 14)
        );

        JPanel titlePanel = new JPanel(
                new GridLayout(2, 1, 5, 5)
        );

        titlePanel.setBackground(Color.WHITE);

        titlePanel.add(lblTitle);
        titlePanel.add(lblSubtitle);

        // Form dang nhap
        JPanel formPanel = new JPanel(
                new GridLayout(4, 1, 5, 5)
        );

        formPanel.setBackground(Color.WHITE);

        JLabel lblUsername = new JLabel("Ten dang nhap:");
        txtUsername = new JTextField();

        JLabel lblPassword = new JLabel("Mat khau:");
        txtPassword = new JPasswordField();

        lblUsername.setFont(
                new Font("Arial", Font.PLAIN, 14)
        );

        lblPassword.setFont(
                new Font("Arial", Font.PLAIN, 14)
        );

        formPanel.add(lblUsername);
        formPanel.add(txtUsername);
        formPanel.add(lblPassword);
        formPanel.add(txtPassword);

        // Nut chuc nang
        JPanel buttonPanel = new JPanel(
                new GridLayout(1, 2, 15, 0)
        );

        buttonPanel.setBackground(Color.WHITE);

        btnLogin = new JButton("Dang nhap");
        btnExit = new JButton("Thoat");

        btnLogin.setFont(
                new Font("Arial", Font.BOLD, 14)
        );

        btnExit.setFont(
                new Font("Arial", Font.BOLD, 14)
        );

        btnLogin.setBackground(
                new Color(35, 90, 160)
        );

        btnLogin.setForeground(Color.WHITE);

        btnExit.setBackground(
                new Color(220, 70, 70)
        );

        btnExit.setForeground(Color.WHITE);

        btnLogin.setFocusPainted(false);
        btnExit.setFocusPainted(false);

        buttonPanel.add(btnLogin);
        buttonPanel.add(btnExit);

        // Ghep giao dien
        mainPanel.add(titlePanel, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);

        // Su kien nut
        btnLogin.addActionListener(e -> login());

        btnExit.addActionListener(e -> {
            int choice = JOptionPane.showConfirmDialog(
                    this,
                    "Ban co chac chan muon thoat?",
                    "Xac nhan",
                    JOptionPane.YES_NO_OPTION
            );

            if (choice == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });

        // Nhan Enter de dang nhap
        txtPassword.addActionListener(e -> login());

        txtUsername.addActionListener(e -> txtPassword.requestFocus());
    }

    // Xu ly dang nhap
    private void login() {

        String username = txtUsername.getText().trim();

        String password = new String(
                txtPassword.getPassword()
        );

        // Kiem tra du lieu
        if (username.isEmpty() || password.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Vui long nhap day du ten dang nhap va mat khau!",
                    "Thong bao",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        // Kiem tra tai khoan trong SQL Server
        User user = userDAO.login(username, password);

        if (user != null) {

            JOptionPane.showMessageDialog(
                    this,
                    "Dang nhap thanh cong!\nXin chao "
                    + user.getName(),
                    "Thong bao",
                    JOptionPane.INFORMATION_MESSAGE
            );

            // Mo giao dien chinh
            new MainFrame(user).setVisible(true);

            // Dong giao dien dang nhap
            dispose();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "Ten dang nhap hoac mat khau khong chinh xac!",
                    "Dang nhap that bai",
                    JOptionPane.ERROR_MESSAGE
            );

            txtPassword.setText("");
            txtPassword.requestFocus();
        }
    }

    // Ham main
    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            new Login().setVisible(true);
        });
    }
}
