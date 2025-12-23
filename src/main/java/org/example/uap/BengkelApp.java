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

    // Global Data Models
    protected DefaultTableModel tableModelKendaraan;
    private DefaultTableModel tableModelServis;
    private DefaultTableModel tableModelLaporan;
    private JTable tableLaporan;

    public BengkelApp() {
        setTitle("Bengkel Pro Manager v1.0");
        setSize(1000, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // --- SIDEBAR MENU ---
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setPreferredSize(new Dimension(220, 0));
        sidebar.setBackground(new Color(33, 37, 41));
        sidebar.setBorder(BorderFactory.createEmptyBorder(20, 15, 20, 15));

        JLabel lblMenu = new JLabel("MENU UTAMA");
        lblMenu.setForeground(Color.WHITE);
        lblMenu.setFont(new Font("SansSerif", Font.BOLD, 16));
        lblMenu.setAlignmentX(Component.CENTER_ALIGNMENT);
        sidebar.add(lblMenu);
        sidebar.add(Box.createRigidArea(new Dimension(0, 30)));

        JButton btnHome = createMenuButton("Dashboard");
        JButton btnMobil = createMenuButton("Data Kendaraan");
        JButton btnServis = createMenuButton("Manajemen Servis");
        JButton btnLaporan = createMenuButton("Riwayat/Laporan");

        sidebar.add(btnHome);
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        sidebar.add(btnMobil);
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        sidebar.add(btnServis);
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        sidebar.add(btnLaporan);

        add(sidebar, BorderLayout.WEST);

        // --- CONTENT AREA ---
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        add(contentPanel, BorderLayout.CENTER);

        // Inisialisasi Halaman
        contentPanel.add(createDashboardPage(), "PAGE_DASHBOARD");
        contentPanel.add(createKendaraanPage(), "PAGE_KENDARAAN");
        contentPanel.add(createServisPage(), "PAGE_SERVIS");
        contentPanel.add(createLaporanPage(), "PAGE_LAPORAN");

        // --- EVENT NAVIGASI ---
        btnHome.addActionListener(e -> cardLayout.show(contentPanel, "PAGE_DASHBOARD"));
        btnMobil.addActionListener(e -> {
            loadDataKendaraan();
            cardLayout.show(contentPanel, "PAGE_KENDARAAN");
        });
        btnServis.addActionListener(e -> cardLayout.show(contentPanel, "PAGE_SERVIS"));
        btnLaporan.addActionListener(e -> {
            loadDataLaporan();
            cardLayout.show(contentPanel, "PAGE_LAPORAN");
        });

        loadDataKendaraan();
    }

    private JButton createMenuButton(String text) {
        JButton btn = new JButton(text);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(true);
        btn.setOpaque(true);
        btn.setBackground(Color.WHITE);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    // --- HALAMAN 1: DASHBOARD ---
    private JPanel createDashboardPage() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(245, 245, 245));

        JLabel header = new JLabel("Bengkel Dashboard");
        header.setFont(new Font("SansSerif", Font.BOLD, 26));
        header.setBorder(BorderFactory.createEmptyBorder(30, 30, 20, 30));
        panel.add(header, BorderLayout.NORTH);

        JPanel statsPanel = new JPanel(new GridLayout(1, 3, 20, 0));
        statsPanel.setBackground(new Color(245, 245, 245));
        statsPanel.setBorder(BorderFactory.createEmptyBorder(0, 30, 30, 30));

        statsPanel.add(createStatCard("Total Kendaraan", "Update on Refresh", new Color(41, 128, 185)));
        statsPanel.add(createStatCard("Status Mekanik", "Tersedia", new Color(192, 57, 43)));
        statsPanel.add(createStatCard("Sistem Database", "Terhubung", new Color(39, 174, 96)));

        panel.add(statsPanel, BorderLayout.NORTH);
        return panel;
    }

    private JPanel createStatCard(String title, String value, Color color) {
        JPanel card = new JPanel(new GridLayout(2, 1, 5, 5));
        card.setBackground(color);
        card.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        JLabel lblT = new JLabel(title); lblT.setForeground(Color.WHITE);
        JLabel lblV = new JLabel(value); lblV.setForeground(Color.WHITE);
        lblV.setFont(new Font("SansSerif", Font.BOLD, 18));
        card.add(lblT); card.add(lblV);
        return card;
    }

    // --- HALAMAN 2: DATA KENDARAAN ---
    private JPanel createKendaraanPage() {
        JPanel panel = new JPanel(new BorderLayout(20, 20));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JLabel lblTitle = new JLabel("Master Data Kendaraan");
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 22));
        panel.add(lblTitle, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridLayout(0, 1, 10, 10));
        formPanel.setPreferredSize(new Dimension(280, 0));

        JTextField txtPlat = new JTextField();
        JTextField txtPemilik = new JTextField();
        JButton btnTambah = new JButton("Simpan Kendaraan");
        btnTambah.setBackground(new Color(40, 167, 69));
        btnTambah.setForeground(Color.WHITE);
        btnTambah.setOpaque(true);
        btnTambah.setBorderPainted(false);

        formPanel.add(new JLabel("Plat Nomor:"));
        formPanel.add(txtPlat);
        formPanel.add(new JLabel("Nama Pemilik:"));
        formPanel.add(txtPemilik);
        formPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        formPanel.add(btnTambah);
        panel.add(formPanel, BorderLayout.WEST);

        String[] cols = {"Plat Nomor", "Nama Pemilik"};
        tableModelKendaraan = new DefaultTableModel(cols, 0);
        JTable table = new JTable(tableModelKendaraan);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);

        btnTambah.addActionListener(e -> {
            String plat = txtPlat.getText().trim();
            String pemilik = txtPemilik.getText().trim();
            if (plat.isEmpty() || pemilik.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Data tidak lengkap!"); return;
            }
            tableModelKendaraan.addRow(new Object[]{plat, pemilik});
            simpanKeFile("kendaraan.txt", plat + "," + pemilik);
            txtPlat.setText(""); txtPemilik.setText("");
            JOptionPane.showMessageDialog(this, "Berhasil Disimpan!");
        });

        return panel;
    }

    // --- HALAMAN 3: MANAJEMEN SERVIS ---
    private JPanel createServisPage() {
        JPanel panel = new JPanel(new BorderLayout(20, 20));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JLabel lblTitle = new JLabel("Form Pendaftaran Servis");
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 22));
        panel.add(lblTitle, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridLayout(0, 1, 8, 8));
        formPanel.setPreferredSize(new Dimension(280, 0));

        JTextField txtPlat = new JTextField();
        JTextField txtKeluhan = new JTextField();
        JTextField txtBiaya = new JTextField();
        JComboBox<String> cbStatus = new JComboBox<>(new String[]{"Antri", "Proses", "Selesai"});
        JButton btnSimpan = new JButton("Catat Servis");
        btnSimpan.setBackground(new Color(0, 123, 255));
        btnSimpan.setForeground(Color.WHITE);
        btnSimpan.setOpaque(true);
        btnSimpan.setBorderPainted(false);

        formPanel.add(new JLabel("Plat Nomor:")); formPanel.add(txtPlat);
        formPanel.add(new JLabel("Keluhan:")); formPanel.add(txtKeluhan);
        formPanel.add(new JLabel("Biaya (Angka):")); formPanel.add(txtBiaya);
        formPanel.add(new JLabel("Status:")); formPanel.add(cbStatus);
        formPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        formPanel.add(btnSimpan);
        panel.add(formPanel, BorderLayout.WEST);

        tableModelServis = new DefaultTableModel(new String[]{"Plat", "Keluhan", "Biaya", "Status"}, 0);
        panel.add(new JScrollPane(new JTable(tableModelServis)), BorderLayout.CENTER);

        btnSimpan.addActionListener(e -> {
            try {
                double biaya = Double.parseDouble(txtBiaya.getText());
                String data = txtPlat.getText() + "," + txtKeluhan.getText() + "," + biaya + "," + cbStatus.getSelectedItem();
                tableModelServis.addRow(data.split(","));
                simpanKeFile("servis.txt", data);
                JOptionPane.showMessageDialog(this, "Servis Dicatat!");
                txtBiaya.setText(""); txtKeluhan.setText("");
            } catch (Exception ex) { JOptionPane.showMessageDialog(this, "Biaya harus angka!"); }
        });

        return panel;
    }

    // --- HALAMAN 4: LAPORAN ---
    private JPanel createLaporanPage() {
        JPanel panel = new JPanel(new BorderLayout(20, 20));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JPanel top = new JPanel(new BorderLayout());
        JLabel lblTitle = new JLabel("Laporan & Riwayat");
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 22));
        top.add(lblTitle, BorderLayout.WEST);

        JTextField txtSearch = new JTextField(15);
        JButton btnSearch = new JButton("Cari Plat");
        JPanel searchP = new JPanel(); searchP.add(txtSearch); searchP.add(btnSearch);
        top.add(searchP, BorderLayout.EAST);
        panel.add(top, BorderLayout.NORTH);

        tableModelLaporan = new DefaultTableModel(new String[]{"Plat", "Keluhan", "Biaya", "Status"}, 0);
        tableLaporan = new JTable(tableModelLaporan);
        panel.add(new JScrollPane(tableLaporan), BorderLayout.CENTER);

        JButton btnSort = new JButton("Urutkan Biaya Termahal");
        btnSort.setBackground(new Color(108, 117, 125));
        btnSort.setForeground(Color.WHITE);
        btnSort.setOpaque(true);
        btnSort.setBorderPainted(false);
        panel.add(btnSort, BorderLayout.SOUTH);

        btnSort.addActionListener(e -> {
            List<Object[]> rows = new ArrayList<>();
            for(int i=0; i<tableModelLaporan.getRowCount(); i++) {
                Object[] r = new Object[4];
                for(int j=0; j<4; j++) r[j] = tableModelLaporan.getValueAt(i, j);
                rows.add(r);
            }
            rows.sort((a,b) -> Double.compare(Double.parseDouble(b[2].toString()), Double.parseDouble(a[2].toString())));
            tableModelLaporan.setRowCount(0);
            for(Object[] r : rows) tableModelLaporan.addRow(r);
        });

        return panel;
    }

    // --- CORE LOGIC ---
    private void simpanKeFile(String filename, String line) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename, true))) {
            bw.write(line); bw.newLine();
        } catch (IOException e) { e.printStackTrace(); }
    }

    private void loadDataKendaraan() {
        if (tableModelKendaraan == null) return;
        tableModelKendaraan.setRowCount(0);
        try (BufferedReader br = new BufferedReader(new FileReader("kendaraan.txt"))) {
            String s; while((s = br.readLine()) != null) tableModelKendaraan.addRow(s.split(","));
        } catch (Exception e) {}
    }

    private void loadDataLaporan() {
        tableModelLaporan.setRowCount(0);
        try (BufferedReader br = new BufferedReader(new FileReader("servis.txt"))) {
            String s; while((s = br.readLine()) != null) tableModelLaporan.addRow(s.split(","));
        } catch (Exception e) {}
    }

    public static void main(String[] args) {
        try { UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName()); } catch (Exception e) {}
        SwingUtilities.invokeLater(() -> new BengkelApp().setVisible(true));
    }
}