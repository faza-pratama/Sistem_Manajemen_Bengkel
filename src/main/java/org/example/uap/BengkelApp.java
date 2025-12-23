package org.example.uap;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class BengkelApp extends JFrame {
    protected JPanel contentPanel;
    protected CardLayout cardLayout;

    // Global Data
    protected DefaultTableModel tableModel;
    private DefaultTableModel tableModelServis;
    private JTable tableServis;
    private DefaultTableModel tableModelLaporan;
    private JTable tableLaporan;

    public BengkelApp() {
        setTitle("Bengkel Pro Manager v1.0");
        setSize(950, 650);
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

        // Styling Button Sederhana
        styleButton(btnHome); styleButton(btnMobil); styleButton(btnServis); styleButton(btnLaporan);

        sidebar.add(new JLabel("MENU UTAMA", SwingConstants.CENTER) {{ setForeground(Color.WHITE); setFont(new Font("SansSerif", Font.BOLD, 14)); }});
        sidebar.add(btnHome);
        sidebar.add(btnMobil);
        sidebar.add(btnServis);
        sidebar.add(btnLaporan);
        add(sidebar, BorderLayout.WEST);

        // --- CONTENT AREA ---
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        add(contentPanel, BorderLayout.CENTER);

        // 1. Tambahkan Halaman Dashboard
        contentPanel.add(createDashboardPage(), "PAGE_DASHBOARD");

        // 2. Tambahkan Halaman Kendaraan (INI YANG SEBELUMNYA ERROR/HILANG)
        contentPanel.add(createKendaraanPage(), "PAGE_KENDARAAN");

        // 3. Tambahkan Halaman Servis
        contentPanel.add(createServisPage(), "PAGE_SERVIS");

        // 4. Tambahkan Halaman Laporan
        contentPanel.add(createLaporanPage(), "PAGE_LAPORAN");

        // --- EVENT NAVIGASI ---
        btnHome.addActionListener(e -> cardLayout.show(contentPanel, "PAGE_DASHBOARD"));

        btnMobil.addActionListener(e -> {
            loadDataKendaraan(); // Refresh data saat menu diklik
            cardLayout.show(contentPanel, "PAGE_KENDARAAN");
        });

        btnServis.addActionListener(e -> cardLayout.show(contentPanel, "PAGE_SERVIS"));

        btnLaporan.addActionListener(e -> {
            loadDataLaporan();
            cardLayout.show(contentPanel, "PAGE_LAPORAN");
        });

        // Load data awal jika ada
        loadDataKendaraan();
    }

    // Method Helper untuk Styling
    private void styleButton(JButton btn) {
        btn.setFocusPainted(false);
        btn.setBackground(Color.WHITE);
        btn.setFont(new Font("SansSerif", Font.PLAIN, 12));
    }

    // --- HALAMAN 1: DASHBOARD ---
    private JPanel createDashboardPage() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        JLabel header = new JLabel("Selamat Datang, Admin Bengkel!");
        header.setFont(new Font("SansSerif", Font.BOLD, 24));
        header.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.add(header, BorderLayout.NORTH);

        JPanel statsPanel = new JPanel(new GridLayout(1, 3, 20, 0));
        statsPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 20));
        statsPanel.setBackground(Color.WHITE);

        // Placeholder stats (bisa diupdate real-time dengan logic tambahan)
        statsPanel.add(createStatCard("Total Kendaraan", "Check Data", new Color(52, 152, 219)));
        statsPanel.add(createStatCard("Servis Aktif", "On Progress", new Color(231, 76, 60)));
        statsPanel.add(createStatCard("Status Sistem", "Online", new Color(46, 204, 113)));

        panel.add(statsPanel, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createStatCard(String title, String value, Color color) {
        JPanel card = new JPanel(new GridLayout(2, 1));
        card.setBackground(color);
        card.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        JLabel lblT = new JLabel(title, SwingConstants.CENTER); lblT.setForeground(Color.WHITE);
        JLabel lblV = new JLabel(value, SwingConstants.CENTER); lblV.setFont(new Font("SansSerif", Font.BOLD, 20)); lblV.setForeground(Color.WHITE);
        card.add(lblT); card.add(lblV);
        return card;
    }

    // --- HALAMAN 2: DATA KENDARAAN (Perbaikan Utama) ---
    private JPanel createKendaraanPage() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(new Color(245, 245, 245));

        JLabel lblTitle = new JLabel("Master Data Kendaraan");
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 20));
        panel.add(lblTitle, BorderLayout.NORTH);

        // Input Form
        JPanel formPanel = new JPanel(new GridLayout(6, 1, 5, 5));
        formPanel.setPreferredSize(new Dimension(250, 0));

        JTextField txtPlat = new JTextField();
        JTextField txtPemilik = new JTextField();
        JButton btnTambah = new JButton("Simpan Data Kendaraan");
        btnTambah.setBackground(new Color(46, 204, 113));
        btnTambah.setForeground(Color.WHITE);

        formPanel.add(new JLabel("Plat Nomor:"));
        formPanel.add(txtPlat);
        formPanel.add(new JLabel("Nama Pemilik:"));
        formPanel.add(txtPemilik);
        formPanel.add(new JLabel("")); // Spacer
        formPanel.add(btnTambah);
        panel.add(formPanel, BorderLayout.WEST);

        // Tabel Data Kendaraan
        String[] cols = {"Plat Nomor", "Nama Pemilik"};
        tableModel = new DefaultTableModel(cols, 0);
        JTable tableKendaraan = new JTable(tableModel);
        panel.add(new JScrollPane(tableKendaraan), BorderLayout.CENTER);

        // Logic Tombol Tambah (Dipindahkan dari Constructor)
        btnTambah.addActionListener(e -> {
            String plat = txtPlat.getText().trim();
            String pemilik = txtPemilik.getText().trim();

            if (plat.isEmpty() || pemilik.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Input tidak boleh kosong!", "Peringatan", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Tambah ke Tabel
            tableModel.addRow(new Object[]{plat, pemilik});
            // Simpan ke File
            simpanKeFile(plat, pemilik);

            // Reset
            txtPlat.setText("");
            txtPemilik.setText("");
            JOptionPane.showMessageDialog(this, "Data Kendaraan Berhasil Disimpan!");
        });

        return panel;
    }

    // --- HALAMAN 3: MANAJEMEN SERVIS ---
    private JPanel createServisPage() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel lblTitle = new JLabel("Form Manajemen Servis");
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 20));
        panel.add(lblTitle, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridLayout(10, 1, 5, 5));
        formPanel.setPreferredSize(new Dimension(250, 0));

        JTextField txtPlatServis = new JTextField();
        JTextField txtKeluhan = new JTextField();
        JTextField txtBiaya = new JTextField();
        String[] statusOptions = {"Antri", "Proses", "Selesai"};
        JComboBox<String> cbStatus = new JComboBox<>(statusOptions);

        formPanel.add(new JLabel("Plat Nomor Kendaraan:")); formPanel.add(txtPlatServis);
        formPanel.add(new JLabel("Keluhan:")); formPanel.add(txtKeluhan);
        formPanel.add(new JLabel("Biaya Servis (Rp):")); formPanel.add(txtBiaya);
        formPanel.add(new JLabel("Status:")); formPanel.add(cbStatus);

        JButton btnSimpanServis = new JButton("Simpan Transaksi");
        btnSimpanServis.setBackground(new Color(52, 152, 219));
        btnSimpanServis.setForeground(Color.WHITE);
        formPanel.add(new JLabel("")); formPanel.add(btnSimpanServis);
        panel.add(formPanel, BorderLayout.WEST);

        // Tabel Servis (Sementara/Session)
        String[] cols = {"Plat", "Keluhan", "Biaya", "Status"};
        tableModelServis = new DefaultTableModel(cols, 0);
        tableServis = new JTable(tableModelServis);
        panel.add(new JScrollPane(tableServis), BorderLayout.CENTER);

        btnSimpanServis.addActionListener(e -> {
            try {
                String plat = txtPlatServis.getText().trim();
                String keluhan = txtKeluhan.getText().trim();
                String status = cbStatus.getSelectedItem().toString();
                double biaya = Double.parseDouble(txtBiaya.getText().trim());

                if (plat.isEmpty() || keluhan.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Mohon lengkapi data!");
                    return;
                }

                tableModelServis.addRow(new Object[]{plat, keluhan, String.valueOf(biaya), status});
                simpanServisKeFile(plat, keluhan, String.valueOf(biaya), status);

                txtPlatServis.setText(""); txtKeluhan.setText(""); txtBiaya.setText("");
                JOptionPane.showMessageDialog(this, "Data Servis Berhasil Dicatat!");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Biaya harus berupa angka!", "Error Input", JOptionPane.ERROR_MESSAGE);
            }
        });

        return panel;
    }

    // --- HALAMAN 4: LAPORAN ---
    private JPanel createLaporanPage() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel topPanel = new JPanel(new BorderLayout());
        JLabel lblTitle = new JLabel("Riwayat Servis & Laporan");
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 20));

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JTextField txtSearch = new JTextField(15);
        JButton btnSearch = new JButton("Cari Plat");
        searchPanel.add(new JLabel("Cari Plat: ")); searchPanel.add(txtSearch); searchPanel.add(btnSearch);

        topPanel.add(lblTitle, BorderLayout.WEST);
        topPanel.add(searchPanel, BorderLayout.EAST);
        panel.add(topPanel, BorderLayout.NORTH);

        String[] cols = {"Plat", "Keluhan", "Biaya", "Status"};
        tableModelLaporan = new DefaultTableModel(cols, 0);
        tableLaporan = new JTable(tableModelLaporan);
        panel.add(new JScrollPane(tableLaporan), BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnSortBiaya = new JButton("Urutkan Biaya (Termahal)");
        JButton btnRefresh = new JButton("Muat Ulang Data");
        bottomPanel.add(btnSortBiaya); bottomPanel.add(btnRefresh);
        panel.add(bottomPanel, BorderLayout.SOUTH);

        // Sorting Logic
        btnSortBiaya.addActionListener(e -> {
            List<Object[]> data = new ArrayList<>();
            for (int i = 0; i < tableModelLaporan.getRowCount(); i++) {
                data.add(new Object[]{
                        tableModelLaporan.getValueAt(i, 0),
                        tableModelLaporan.getValueAt(i, 1),
                        tableModelLaporan.getValueAt(i, 2),
                        tableModelLaporan.getValueAt(i, 3)
                });
            }
            data.sort((a, b) -> Double.compare(
                    Double.parseDouble(b[2].toString()),
                    Double.parseDouble(a[2].toString())
            ));
            tableModelLaporan.setRowCount(0);
            for (Object[] row : data) tableModelLaporan.addRow(row);
        });

        // Search Logic
        btnSearch.addActionListener(e -> {
            String query = txtSearch.getText().toLowerCase();
            tableLaporan.clearSelection();
            for (int i = 0; i < tableLaporan.getRowCount(); i++) {
                if (tableLaporan.getValueAt(i, 0).toString().toLowerCase().contains(query)) {
                    tableLaporan.setRowSelectionInterval(i, i);
                    // Scroll ke baris yang ditemukan
                    tableLaporan.scrollRectToVisible(tableLaporan.getCellRect(i, 0, true));
                    return;
                }
            }
            JOptionPane.showMessageDialog(this, "Data tidak ditemukan");
        });

        btnRefresh.addActionListener(e -> loadDataLaporan());

        return panel;
    }

    // --- FILE HANDLING ---
    private void simpanKeFile(String plat, String pemilik) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("kendaraan.txt", true))) {
            writer.write(plat + "," + pemilik);
            writer.newLine();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Gagal menyimpan: " + e.getMessage());
        }
    }

    private void loadDataKendaraan() {
        if (tableModel == null) return; // Guard clause

        tableModel.setRowCount(0); // Reset tabel agar tidak duplikat saat reload
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

    private void simpanServisKeFile(String plat, String keluhan, String biaya, String status) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("servis.txt", true))) {
            writer.write(plat + "," + keluhan + "," + biaya + "," + status);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadDataLaporan() {
        tableModelLaporan.setRowCount(0);
        File file = new File("servis.txt");
        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 4) {
                    tableModelLaporan.addRow(data);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        SwingUtilities.invokeLater(() -> new BengkelApp().setVisible(true));
    }
}