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

        // Event Navigasi
        btnHome.addActionListener(e -> cardLayout.show(contentPanel, "PAGE_DASHBOARD"));
        btnMobil.addActionListener(e -> cardLayout.show(contentPanel, "PAGE_KENDARAAN"));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new BengkelApp().setVisible(true));
    }
}