package org.example.uap;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;

public class BengkelApp extends JFrame {
    protected JPanel contentPanel;
    protected CardLayout cardLayout;
    protected DefaultTableModel tableModel; // Global untuk Data Kendaraan

    public BengkelApp() {
        setTitle("Bengkel Pro Manager v1.0");
        setSize(900, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // --- SIDEBAR MENU ---
        JPanel sidebar = new JPanel(new GridLayout(6, 1, 10, 10));
        sidebar.setPreferredSize(new Dimension(200, 0));
        sidebar.setBackground(new Color(33, 37, 41));
        sidebar.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

        JButton btnHome = new JButton("Dashboard");
        JButton btnMobil = new JButton("Data Kendaraan");
        JButton btnServis = new JButton("Manajemen Servis");
        JButton btnLaporan = new JButton("Riwayat/Laporan");

        sidebar.add(new JLabel("MENU UTAMA", SwingConstants.CENTER) {{ setForeground(Color.WHITE); }});
        sidebar.add(btnHome);
        sidebar.add(btnMobil);
        sidebar.add(btnServis);
        sidebar.add(btnLaporan);
        add(sidebar, BorderLayout.WEST);

        // --- CONTENT AREA ---
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        add(contentPanel, BorderLayout.CENTER);
        contentPanel.add(createDashboardPage(), "PAGE_DASHBOARD");
        contentPanel.add(createKendaraanPage(), "PAGE_KENDARAAN");

        // Event Navigasi
        btnHome.addActionListener(e -> cardLayout.show(contentPanel, "PAGE_DASHBOARD"));
        btnMobil.addActionListener(e -> cardLayout.show(contentPanel, "PAGE_KENDARAAN"));
    }

    private JPanel createDashboardPage() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        JLabel header = new JLabel("Selamat Datang, Admin Bengkel!");
        header.setFont(new Font("SansSerif", Font.BOLD, 22));
        header.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.add(header, BorderLayout.NORTH);

        JPanel statsPanel = new JPanel(new GridLayout(1, 3, 20, 0));
        statsPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 20));
        statsPanel.setBackground(Color.WHITE);

        statsPanel.add(createStatCard("Total Kendaraan", "0", new Color(52, 152, 219)));
        statsPanel.add(createStatCard("Servis Aktif", "0", new Color(231, 76, 60)));
        statsPanel.add(createStatCard("Pendapatan", "Rp 0", new Color(46, 204, 113)));

        panel.add(statsPanel, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createStatCard(String title, String value, Color color) {
        JPanel card = new JPanel(new GridLayout(2, 1));
        card.setBackground(color);
        JLabel lblT = new JLabel(title, SwingConstants.CENTER); lblT.setForeground(Color.WHITE);
        JLabel lblV = new JLabel(value, SwingConstants.CENTER); lblV.setFont(new Font("SansSerif", Font.BOLD, 25)); lblV.setForeground(Color.WHITE);
        card.add(lblT); card.add(lblV);
        return card;
    }

    private JPanel createKendaraanPage() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(Color.WHITE);

        JLabel lblTitle = new JLabel("Manajemen Data Kendaraan");
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 20));
        panel.add(lblTitle, BorderLayout.NORTH);

        // FORM INPUT
        JPanel formPanel = new JPanel(new GridLayout(10, 1, 5, 5));
        formPanel.setPreferredSize(new Dimension(250, 0));
        formPanel.setBackground(Color.WHITE);

        JTextField txtPlat = new JTextField();
        JTextField txtPemilik = new JTextField();
        JButton btnTambah = new JButton("Simpan Kendaraan");

        formPanel.add(new JLabel("Plat Nomor:"));
        formPanel.add(txtPlat);
        formPanel.add(new JLabel("Nama Pemilik:"));
        formPanel.add(txtPemilik);
        formPanel.add(new JLabel("")); // Spacer
        formPanel.add(btnTambah);
        panel.add(formPanel, BorderLayout.WEST);

        // TABEL DATA
        String[] columns = {"Plat Nomor", "Nama Pemilik"};
        tableModel = new DefaultTableModel(columns, 0);
        JTable table = new JTable(tableModel);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);

        return panel;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new BengkelApp().setVisible(true));
    }
}