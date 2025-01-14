package com.ariskurniawan.uaswengcaffe;

import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class TambahActivity extends AppCompatActivity {

    private EditText etNama, etDeskripsi, etHarga;
    private RadioGroup rgKategori;
    private RadioButton rbMakanan, rbMinuman;
    private CheckBox cbCabangUtama, cbPromoDiskon, cbTakeaway;
    private Button btnSimpan;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah);

        // Inisialisasi komponen UI
        etNama = findViewById(R.id.etNama);
        etDeskripsi = findViewById(R.id.etDeskripsi);
        etHarga = findViewById(R.id.etHarga);
        rgKategori = findViewById(R.id.rgKategori);
        rbMakanan = findViewById(R.id.rbMakanan);
        rbMinuman = findViewById(R.id.rbMinuman);
        cbCabangUtama = findViewById(R.id.cbCabangUtama);
        cbPromoDiskon = findViewById(R.id.cbPromoDiskon);
        cbTakeaway = findViewById(R.id.cbTakeaway);
        btnSimpan = findViewById(R.id.btnSimpan);

        // Inisialisasi DatabaseHelper
        databaseHelper = new DatabaseHelper(this);

        // Aksi ketika tombol Simpan diklik
        btnSimpan.setOnClickListener(v -> {
            String nama = etNama.getText().toString().trim();
            String deskripsi = etDeskripsi.getText().toString().trim();
            String hargaStr = etHarga.getText().toString().trim();

            if (nama.isEmpty() || deskripsi.isEmpty() || hargaStr.isEmpty() || rgKategori.getCheckedRadioButtonId() == -1) {
                Toast.makeText(this, "Semua field harus diisi!", Toast.LENGTH_SHORT).show();
                return;
            }

            double harga = Double.parseDouble(hargaStr);
            boolean tersediaCabangUtama = cbCabangUtama.isChecked();
            boolean promoDiskon = cbPromoDiskon.isChecked();
            boolean takeawayTersedia = cbTakeaway.isChecked();

            // Ambil nilai kategori dari RadioButton
            String kategori;
            if (rgKategori.getCheckedRadioButtonId() == R.id.rbMakanan) {
                kategori = "Makanan";
            } else {
                kategori = "Minuman";
            }

            // Simpan data ke database
            long result = databaseHelper.insertProduct(
                    nama, deskripsi, kategori, harga,
                    tersediaCabangUtama, promoDiskon, takeawayTersedia,
                    String.valueOf(System.currentTimeMillis()) // Tanggal sekarang
            );

            if (result > 0) {
                Toast.makeText(this, "Produk berhasil ditambahkan", Toast.LENGTH_SHORT).show();
                finish(); // Kembali ke MainActivity
            } else {
                Toast.makeText(this, "Gagal menambahkan produk", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
