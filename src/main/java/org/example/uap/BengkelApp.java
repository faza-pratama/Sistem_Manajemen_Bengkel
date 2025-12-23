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

        // Event Navigasi
        btnHome.addActionListener(e -> cardLayout.show(contentPanel, "PAGE_DASHBOARD"));
        btnMobil.addActionListener(e -> cardLayout.show(contentPanel, "PAGE_KENDARAAN"));
        btnTambah.addActionListener(e -> {
            String plat = txtPlat.getText().trim();
            String pemilik = txtPemilik.getText().trim();

            // Exception Handling: Validasi input kosong
            if (plat.isEmpty() || pemilik.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Input tidak boleh kosong!", "Peringatan", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Tambah ke Tabel (Read/Update secara visual)
            tableModel.addRow(new Object[]{plat, pemilik});

            // Simpan ke File (Persistensi Data)
            simpanKeFile(plat, pemilik);

            // Reset Form
            txtPlat.setText("");
            txtPemilik.setText("");
            JOptionPane.showMessageDialog(this, "Data Berhasil Disimpan!");
        });
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
    private void simpanKeFile(String plat, String pemilik) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("kendaraan.txt", true))) {
            writer.write(plat + "," + pemilik);
            writer.newLine();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Gagal menyimpan ke file: " + e.getMessage());
        }
    }
    private void loadDataKendaraan() {
        File file = new File("kendaraan.txt");
        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 2) {
                    tableModel.addRow(data);
                }
            }
        } catch (IOException e) {
            System.out.println("Gagal memuat data: " + e.getMessage());
        }
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new BengkelApp().setVisible(true));
    }
}