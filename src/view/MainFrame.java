
package view;

import model.User;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    private JPanel contentPanel;
    private JPanel menuPanel;

    private User currentUser;

    public MainFrame(User user) {

        this.currentUser = user;

        initComponents();
    }

    private void initComponents() {

        setTitle("PHAN MEM QUAN LY BAN HANG");
        setSize(1200, 750);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Thanh tieu de
        JPanel headerPanel = new JPanel(new BorderLayout());

        headerPanel.setBackground(new Color(35, 90, 160));
        headerPanel.setPreferredSize(new Dimension(1200, 65));

        JLabel lblTitle = new JLabel("  QUAN LY BAN HANG");

        lblTitle.setFont(new Font("Arial", Font.BOLD, 22));
        lblTitle.setForeground(Color.WHITE);

        JLabel lblUser = new JLabel(
                "Xin chao, " + currentUser.getName() + "   "
        );

        lblUser.setFont(new Font("Arial", Font.BOLD, 14));
        lblUser.setForeground(Color.WHITE);

        headerPanel.add(lblTitle, BorderLayout.WEST);
        headerPanel.add(lblUser, BorderLayout.EAST);

        add(headerPanel, BorderLayout.NORTH);

        // Menu ben trai
        menuPanel = new JPanel();

        menuPanel.setLayout(new GridLayout(10, 1, 5, 5));
        menuPanel.setBackground(new Color(235, 240, 245));

        menuPanel.setPreferredSize(new Dimension(220, 0));

        addMenuButton(menuPanel, "Trang chu", "home");
        addMenuButton(menuPanel, "Danh muc", "category");
        addMenuButton(menuPanel, "San pham", "product");
        addMenuButton(menuPanel, "Khach hang", "customer");
        addMenuButton(menuPanel, "Nhan vien", "employee");
        addMenuButton(menuPanel, "Hoa don", "invoice");
        addMenuButton(menuPanel, "Chi tiet hoa don", "detail");
        addMenuButton(menuPanel, "Tai khoan", "account");
        addMenuButton(menuPanel, "Thong ke - Bao cao", "statistics");
        addMenuButton(menuPanel, "Dang xuat", "logout");

        add(menuPanel, BorderLayout.WEST);

        // Khu vuc hien thi
        contentPanel = new JPanel(new BorderLayout());

        contentPanel.setBackground(Color.WHITE);

        add(contentPanel, BorderLayout.CENTER);

        openFunction("home");
    }

    // Tao nut menu
    private void addMenuButton(
            JPanel panel,
            String text,
            String action
    ) {

        JButton button = new JButton(text);

        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setFocusPainted(false);

        button.setBackground(Color.WHITE);
        button.setForeground(new Color(35, 70, 110));

        button.setBorder(
                BorderFactory.createEmptyBorder(12, 10, 12, 10)
        );

        button.addActionListener(e -> openFunction(action));

        panel.add(button);
    }

    // Chuyen doi giao dien
    private void openFunction(String action) {

        contentPanel.removeAll();

        switch (action) {

            case "home":

                JLabel lblWelcome = new JLabel(
                        "<html><center>"
                        + "<h1>CHAO MUNG DEN VOI PHAN MEM</h1>"
                        + "<h2>QUAN LY BAN HANG</h2>"
                        + "<p>Chuc ban lam viec hieu qua!</p>"
                        + "</center></html>",
                        SwingConstants.CENTER
                );

                lblWelcome.setFont(new Font("Arial", Font.PLAIN, 22));

                contentPanel.add(lblWelcome, BorderLayout.CENTER);

                break;

            case "category":

                contentPanel.add(
                        new DanhMucView(),
                        BorderLayout.CENTER
                );

                break;

            case "product":

                contentPanel.add(
                        new SanPhamView(),
                        BorderLayout.CENTER
                );

                break;

            case "customer":

                contentPanel.add(
                        new KhachHangView(),
                        BorderLayout.CENTER
                );

                break;

            case "employee":

                contentPanel.add(
                        new NhanVienView(),
                        BorderLayout.CENTER
                );

                break;

            case "invoice":

                contentPanel.add(
                        new HoaDonView(),
                        BorderLayout.CENTER
                );

                break;

            case "detail":

                contentPanel.add(
                        new ChiTietHoaDonView(),
                        BorderLayout.CENTER
                );

                break;

            case "account":

                contentPanel.add(
                        new TaiKhoanView(),
                        BorderLayout.CENTER
                );

                break;

            case "statistics":

                contentPanel.add(
                        new ThongKeView(),
                        BorderLayout.CENTER
                );

                break;

            case "logout":

                int choice = JOptionPane.showConfirmDialog(
                        this,
                        "Ban co chac chan muon dang xuat?",
                        "Xac nhan dang xuat",
                        JOptionPane.YES_NO_OPTION
                );

                if (choice == JOptionPane.YES_OPTION) {

                    new Login().setVisible(true);

                    dispose();
                }

                return;

            default:

                break;
        }

        contentPanel.revalidate();
        contentPanel.repaint();
    }
}
