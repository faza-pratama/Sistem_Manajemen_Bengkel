package org.example.uap;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;

public class BengkelApp extends JFrame {
    protected JPanel contentPanel;
    protected CardLayout cardLayout;
    protected DefaultTableModel tableModel; // Global untuk Data Kendaraan

    private DefaultTableModel tableModelServis;
    private JTable tableServis;
    private DefaultTableModel tableModelLaporan;
    private JTable tableLaporan;

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

        contentPanel.add(createServisPage(), "PAGE_SERVIS");
        btnServis.addActionListener(e -> cardLayout.show(contentPanel, "PAGE_SERVIS"));

        contentPanel.add(createLaporanPage(), "PAGE_LAPORAN");
        btnLaporan.addActionListener(e -> {
            loadDataLaporan(); // Update data setiap kali halaman dibuka
            cardLayout.show(contentPanel, "PAGE_LAPORAN");
        });

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
    private JPanel createServisPage() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(new Color(240, 240, 240));

        JLabel lblTitle = new JLabel("Form Manajemen Servis");
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 20));
        panel.add(lblTitle, BorderLayout.NORTH);

        // FORM INPUT (KIRI)
        JPanel formPanel = new JPanel(new GridLayout(10, 1, 5, 5));
        formPanel.setPreferredSize(new Dimension(250, 0));

        JTextField txtPlatServis = new JTextField();
        JTextField txtKeluhan = new JTextField();
        JTextField txtBiaya = new JTextField();
        String[] statusOptions = {"Antri", "Proses", "Selesai"};
        JComboBox<String> cbStatus = new JComboBox<>(statusOptions);

        formPanel.add(new JLabel("Plat Nomor Kendaraan:"));
        formPanel.add(txtPlatServis);
        formPanel.add(new JLabel("Keluhan:"));
        formPanel.add(txtKeluhan);
        formPanel.add(new JLabel("Biaya Servis (Rp):"));
        formPanel.add(txtBiaya);
        formPanel.add(new JLabel("Status:"));
        formPanel.add(cbStatus);

        JButton btnSimpanServis = new JButton("Simpan Transaksi");
        btnSimpanServis.setBackground(new Color(52, 152, 219));
        btnSimpanServis.setForeground(Color.WHITE);
        formPanel.add(new JLabel(""));
        formPanel.add(btnSimpanServis);
        panel.add(formPanel, BorderLayout.WEST);

        // LOGIKA TOMBOL (Exception Handling & File Handling)
        btnSimpanServis.addActionListener(e -> {
            try {
                String plat = txtPlatServis.getText().trim();
                String keluhan = txtKeluhan.getText().trim();
                String status = cbStatus.getSelectedItem().toString();

                // Validasi Angka: Memicu NumberFormatException jika input bukan angka
                double biaya = Double.parseDouble(txtBiaya.getText().trim());

                if (plat.isEmpty() || keluhan.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Mohon lengkapi data!");
                    return;
                }

                tableModelServis.addRow(new Object[]{plat, keluhan, biaya, status});
                simpanServisKeFile(plat, keluhan, String.valueOf(biaya), status); // Persistensi

                // Reset Form
                txtPlatServis.setText(""); txtKeluhan.setText(""); txtBiaya.setText("");
                JOptionPane.showMessageDialog(this, "Data Servis Berhasil Dicatat!");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Biaya harus berupa angka!", "Error Input", JOptionPane.ERROR_MESSAGE);
            }
        });

        // TABEL SERVIS
        String[] cols = {"Plat", "Keluhan", "Biaya", "Status"};
        tableModelServis = new DefaultTableModel(cols, 0);
        tableServis = new JTable(tableModelServis);
        panel.add(new JScrollPane(tableServis), BorderLayout.CENTER);

        return panel;
    }
    private JPanel createLaporanPage() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // HEADER & PENCARIAN
        JPanel topPanel = new JPanel(new BorderLayout());
        JLabel lblTitle = new JLabel("Riwayat Servis & Laporan");
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 20));

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JTextField txtSearch = new JTextField(15);
        JButton btnSearch = new JButton("Cari Plat");
        searchPanel.add(new JLabel("Cari Plat: "));
        searchPanel.add(txtSearch);
        searchPanel.add(btnSearch);

        topPanel.add(lblTitle, BorderLayout.WEST);
        topPanel.add(searchPanel, BorderLayout.EAST);
        panel.add(topPanel, BorderLayout.NORTH);

        // TABEL
        String[] cols = {"Plat", "Keluhan", "Biaya", "Status"};
        tableModelLaporan = new DefaultTableModel(cols, 0);
        tableLaporan = new JTable(tableModelLaporan);
        panel.add(new JScrollPane(tableLaporan), BorderLayout.CENTER);

        // PANEL FOOTER (SORTING)
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnSortBiaya = new JButton("Urutkan Biaya (Termahal)");
        JButton btnRefresh = new JButton("Muat Ulang Data");
        bottomPanel.add(btnSortBiaya);
        bottomPanel.add(btnRefresh);
        panel.add(bottomPanel, BorderLayout.SOUTH);

        // LOGIKA SORTING (Menggunakan Comparator & ArrayList)
        btnSortBiaya.addActionListener(e -> {
            java.util.List<Object[]> data = new java.util.ArrayList<>();
            for (int i = 0; i < tableModelLaporan.getRowCount(); i++) {
                data.add(new Object[]{
                        tableModelLaporan.getValueAt(i, 0),
                        tableModelLaporan.getValueAt(i, 1),
                        tableModelLaporan.getValueAt(i, 2),
                        tableModelLaporan.getValueAt(i, 3)
                });
            }
            // Sorting Descending berdasarkan Biaya
            data.sort((a, b) -> Double.compare(
                    Double.parseDouble(b[2].toString()),
                    Double.parseDouble(a[2].toString())
            ));
            tableModelLaporan.setRowCount(0);
            for (Object[] row : data) tableModelLaporan.addRow(row);
        });

        // LOGIKA SEARCHING (Filter Sederhana)
        btnSearch.addActionListener(e -> {
            String query = txtSearch.getText().toLowerCase();
            for (int i = 0; i < tableLaporan.getRowCount(); i++) {
                if (tableLaporan.getValueAt(i, 0).toString().toLowerCase().contains(query)) {
                    tableLaporan.setRowSelectionInterval(i, i);
                    return;
                }
            }
        });

        btnRefresh.addActionListener(e -> loadDataLaporan());

        return panel;
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
    private void loadDataLaporan() {
        tableModelLaporan.setRowCount(0);
        try (BufferedReader reader = new BufferedReader(new FileReader("servis.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                tableModelLaporan.addRow(line.split(","));
            }
        } catch (IOException e) { /* File mungkin belum ada */ }
    }
    private void simpanServisKeFile(String plat, String keluhan, String biaya, String status) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("servis.txt", true))) {
            writer.write(plat + "," + keluhan + "," + biaya + "," + status);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new BengkelApp().setVisible(true));
    }
}