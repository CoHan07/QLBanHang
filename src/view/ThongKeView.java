
package view;

import dao.ThongKeDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import java.util.Map;
import java.util.List;

public class ThongKeView extends JPanel {

    private ThongKeDAO thongKeDAO;

    private JLabel lblDoanhThu;
    private JLabel lblHoaDon;
    private JLabel lblSanPham;
    private JLabel lblSapHet;

    private JPanel chartPanel;

    private DefaultTableModel tableModel;

    private final Color xanh = new Color(35, 90, 160);

    public ThongKeView() {

        thongKeDAO = new ThongKeDAO();

        setLayout(new BorderLayout(15, 15));
        setBackground(new Color(245, 247, 250));

        setBorder(
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        );

        taoGiaoDien();
        capNhatThongKe();
    }

    private void taoGiaoDien() {

        // Tieu de
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setOpaque(false);

        JLabel lblTitle = new JLabel("THONG KE - BAO CAO");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 25));
        lblTitle.setForeground(xanh);

        JButton btnRefresh = new JButton("Lam moi");

        btnRefresh.setFont(new Font("Arial", Font.BOLD, 13));
        btnRefresh.setBackground(xanh);
        btnRefresh.setForeground(Color.WHITE);
        btnRefresh.setFocusPainted(false);

        btnRefresh.addActionListener(e -> capNhatThongKe());

        titlePanel.add(lblTitle, BorderLayout.WEST);
        titlePanel.add(btnRefresh, BorderLayout.EAST);

        add(titlePanel, BorderLayout.NORTH);

        // Cac o thong ke
        JPanel cardPanel = new JPanel(new GridLayout(1, 4, 15, 15));
        cardPanel.setOpaque(false);

        lblDoanhThu = new JLabel("0 VND");
        lblHoaDon = new JLabel("0");
        lblSanPham = new JLabel("0");
        lblSapHet = new JLabel("0");

        cardPanel.add(taoCard("TONG DOANH THU", lblDoanhThu));
        cardPanel.add(taoCard("TONG HOA DON", lblHoaDon));
        cardPanel.add(taoCard("SAN PHAM DA BAN", lblSanPham));
        cardPanel.add(taoCard("SAN PHAM SAP HET", lblSapHet));

        // Bieu do doanh thu
        chartPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                veBieuDo(g);
            }
        };

        chartPanel.setBackground(Color.WHITE);

        chartPanel.setBorder(
                BorderFactory.createTitledBorder(
                        "DOANH THU THEO THANG"
                )
        );

        chartPanel.setPreferredSize(new Dimension(800, 300));

        // Bang Top 5 san pham
        String[] columns = {
            "Ma san pham",
            "Ten san pham",
            "So luong da ban",
            "Doanh thu"
        };

        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable table = new JTable(tableModel);

        table.setFont(new Font("Arial", Font.PLAIN, 13));
        table.setRowHeight(28);

        table.getTableHeader().setFont(
                new Font("Arial", Font.BOLD, 13)
        );

        table.getTableHeader().setBackground(
                new Color(220, 230, 240)
        );

        table.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        JScrollPane tableScroll = new JScrollPane(table);

        tableScroll.setPreferredSize(
                new Dimension(800, 190)
        );

        JPanel topPanel = new JPanel(new BorderLayout(15, 20));
        topPanel.setOpaque(false);

        topPanel.add(cardPanel, BorderLayout.NORTH);
        topPanel.add(chartPanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new BorderLayout(5, 10));
        bottomPanel.setBackground(Color.WHITE);

        bottomPanel.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                new Color(220, 225, 230)
                        ),
                        BorderFactory.createEmptyBorder(10, 10, 10, 10)
                )
        );

        JLabel lblTop5 = new JLabel("TOP 5 SAN PHAM BAN CHAY");
        lblTop5.setFont(new Font("Arial", Font.BOLD, 16));
        lblTop5.setForeground(xanh);

        bottomPanel.add(lblTop5, BorderLayout.NORTH);
        bottomPanel.add(tableScroll, BorderLayout.CENTER);

        JPanel content = new JPanel(new BorderLayout(15, 20));
        content.setOpaque(false);

        content.add(topPanel, BorderLayout.CENTER);
        content.add(bottomPanel, BorderLayout.SOUTH);

        JScrollPane mainScroll = new JScrollPane(content);

        mainScroll.setBorder(null);
        mainScroll.getVerticalScrollBar().setUnitIncrement(16);
        mainScroll.setHorizontalScrollBarPolicy(
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
        );

        add(mainScroll, BorderLayout.CENTER);
    }

    // Tao tung o thong ke
    private JPanel taoCard(String title, JLabel valueLabel) {

        JPanel card = new JPanel(new BorderLayout(5, 15));

        card.setBackground(Color.WHITE);

        card.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                new Color(220, 225, 230)
                        ),
                        BorderFactory.createEmptyBorder(15, 15, 15, 15)
                )
        );

        JLabel lblTitle = new JLabel(title);

        lblTitle.setFont(new Font("Arial", Font.BOLD, 12));
        lblTitle.setForeground(new Color(100, 110, 120));

        valueLabel.setFont(new Font("Arial", Font.BOLD, 20));
        valueLabel.setForeground(xanh);

        card.add(lblTitle, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);

        return card;
    }

    // Cap nhat du lieu
    private void capNhatThongKe() {

        try {

            double doanhThu = thongKeDAO.getTongDoanhThu();

            int hoaDon = thongKeDAO.getTongHoaDon();

            int sanPham = thongKeDAO.getTongSanPhamDaBan();

            int sapHet = thongKeDAO.getSoSanPhamSapHet();

            lblDoanhThu.setText(
                    dinhDangTien(doanhThu) + " VND"
            );

            lblHoaDon.setText(String.valueOf(hoaDon));

            lblSanPham.setText(String.valueOf(sanPham));

            lblSapHet.setText(String.valueOf(sapHet));

            capNhatTop5();

            chartPanel.repaint();

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Khong the tai du lieu thong ke!",
                    "Loi",
                    JOptionPane.ERROR_MESSAGE
            );

            e.printStackTrace();
        }
    }

    // Cap nhat bang Top 5
    private void capNhatTop5() {

        tableModel.setRowCount(0);

        List<Object[]> data =
                thongKeDAO.getTop5SanPhamBanChay();

        for (Object[] row : data) {

            tableModel.addRow(new Object[]{
                row[0],
                row[1],
                row[2],
                dinhDangTien(
                        ((Number) row[3]).doubleValue()
                ) + " VND"
            });
        }
    }

    // Dinh dang tien
    private String dinhDangTien(double tien) {

        DecimalFormatSymbols symbols =
                new DecimalFormatSymbols(new Locale("vi", "VN"));

        symbols.setGroupingSeparator('.');
        symbols.setDecimalSeparator(',');

        DecimalFormat df = new DecimalFormat("#,##0.##", symbols);

        return df.format(tien);
    }

    // Ve bieu do doanh thu
    private void veBieuDo(Graphics g) {

        Map<String, Double> data =
                thongKeDAO.getDoanhThuTheoThang();

        Graphics2D g2 = (Graphics2D) g.create();

        g2.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
        );

        int width = chartPanel.getWidth();
        int height = chartPanel.getHeight();

        int left = 80;
        int right = 30;
        int top = 45;
        int bottom = 65;

        int chartWidth = width - left - right;
        int chartHeight = height - top - bottom;

        if (data.isEmpty()) {

            g2.setFont(new Font("Arial", Font.PLAIN, 16));
            g2.setColor(Color.GRAY);

            g2.drawString(
                    "Chua co du lieu doanh thu",
                    width / 2 - 90,
                    height / 2
            );

            g2.dispose();
            return;
        }

        if (chartWidth <= 0 || chartHeight <= 0) {
            g2.dispose();
            return;
        }

        double max = 0;

        for (double value : data.values()) {
            max = Math.max(max, value);
        }

        if (max <= 0) {
            max = 1;
        }

        // Truc toa do
        g2.setColor(Color.GRAY);

        g2.drawLine(
                left, top,
                left, top + chartHeight
        );

        g2.drawLine(
                left, top + chartHeight,
                left + chartWidth, top + chartHeight
        );

        // Cac vach chia
        g2.setFont(new Font("Arial", Font.PLAIN, 11));

        for (int i = 0; i <= 4; i++) {

            int y = top + chartHeight
                    - (i * chartHeight / 4);

            double value = max * i / 4;

            g2.setColor(new Color(225, 230, 235));

            g2.drawLine(
                    left, y,
                    left + chartWidth, y
            );

            g2.setColor(Color.DARK_GRAY);

            g2.drawString(
                    dinhDangTien(value),
                    5,
                    y + 5
            );
        }

        // Ve cot
        int count = data.size();

        int slotWidth = chartWidth / count;

        int barWidth = Math.max(
                10,
                Math.min(55, slotWidth / 2)
        );

        int index = 0;

        for (Map.Entry<String, Double> entry : data.entrySet()) {

            double value = entry.getValue();

            int barHeight = (int) (
                    value / max * chartHeight
            );

            int x = left + index * slotWidth
                    + (slotWidth - barWidth) / 2;

            int y = top + chartHeight - barHeight;

            g2.setColor(new Color(45, 130, 210));

            g2.fillRoundRect(
                    x, y,
                    barWidth, barHeight,
                    8, 8
            );

            // Nhan thang
            g2.setColor(Color.DARK_GRAY);

            g2.setFont(
                    new Font("Arial", Font.PLAIN, 11)
            );

            FontMetrics fm = g2.getFontMetrics();

            String month = entry.getKey();

            int textX = x
                    + (barWidth - fm.stringWidth(month)) / 2;

            g2.drawString(
                    month,
                    textX,
                    top + chartHeight + 20
            );

            index++;
        }

        g2.dispose();
    }
}
