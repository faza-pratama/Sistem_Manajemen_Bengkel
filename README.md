# Sistem_Manajemen_Bengkel

Bengkel Pro Manager v1.1 🛠️

Aplikasi desktop manajemen bengkel berbasis Java Swing yang dirancang untuk mempermudah administrasi data kendaraan, pendaftaran servis, hingga pelaporan riwayat transaksi secara efisien.

🌟 Fitur Utama

Dashboard Statistik: Menampilkan jumlah total kendaraan terdaftar secara real-time.

Data Kendaraan: Manajemen (Tambah/Hapus) data pemilik dan plat nomor kendaraan.

Manajemen Servis: Pencatatan keluhan pelanggan dan biaya servis dengan validasi input.

Laporan & Riwayat: Monitoring transaksi yang dilengkapi fitur pencarian plat dan pengurutan biaya.

Persistensi Data: Penyimpanan data otomatis ke file lokal (.txt) sehingga data tidak hilang saat aplikasi ditutup.

🛠️ Penggunaan Java API
Sesuai dengan instruksi pengembangan, aplikasi ini memanfaatkan beberapa Java Standard API untuk fungsionalitas tingkat lanjut:

java.util.ArrayList: Digunakan untuk menampung dan memproses data dinamis pada tabel laporan.

java.util.Comparator: Mengatur logika pengurutan (sorting) biaya servis dari termahal ke termurah.

java.io (File I/O): Mengelola penyimpanan permanen pada file eksternal.

📝 Hasil Code Review & Testing
Berdasarkan dokumentasi internal, berikut adalah poin penting hasil review:

Optimasi Variabel: Mengubah penamaan variabel dari generik (seperti txt1) menjadi deskriptif (txtPlat, txtBiaya) untuk keterbacaan kode yang lebih baik.

Penanganan Eksepsi: Implementasi try-catch pada input biaya untuk mencegah aplikasi crash jika pengguna memasukkan karakter non-angka.

Sinkronisasi Data: Fitur hapus data telah diperbaiki sehingga sinkron antara tampilan tabel dan isi file .txt.

🚀 Cara Menjalankan
Pastikan Anda telah menginstal Java Development Kit (JDK) versi 8 atau yang terbaru.

Salin kode sumber ke dalam direktori proyek Anda.

Kompilasi dan jalankan file BengkelApp.java.

Data akan disimpan secara otomatis di direktori yang sama dengan nama file kendaraan.txt dan servis.txt.

📂 Struktur File
BengkelApp.java: File utama aplikasi (GUI & Logic).

kendaraan.txt: Database lokal untuk data kendaraan.

servis.txt: Database lokal untuk riwayat servis dan biaya.