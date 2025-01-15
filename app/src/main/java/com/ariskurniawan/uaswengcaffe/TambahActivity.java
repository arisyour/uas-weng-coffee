package com.ariskurniawan.uaswengcaffe;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;

public class TambahActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    private EditText etNama, etDeskripsi, etHarga;
    private RadioGroup rgKategori;
    private CheckBox cbPromoDiskon, cbTakeaway;
    private ImageView imgPreview;
    private TextView tvTanggal;
    private Button btnPilihFoto, btnPilihTanggal, btnSimpan;
    private DatabaseHelper databaseHelper;

    private Uri imageUri;
    private String selectedDate;
    private String savedImagePath; // Path gambar yang disimpan

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah);

        // Inisialisasi UI
        etNama = findViewById(R.id.etNama);
        etDeskripsi = findViewById(R.id.etDeskripsi);
        etHarga = findViewById(R.id.etHarga);
        rgKategori = findViewById(R.id.rgKategori);
        cbPromoDiskon = findViewById(R.id.cbPromoDiskon);
        cbTakeaway = findViewById(R.id.cbTakeaway);
        imgPreview = findViewById(R.id.imgPreview);
        tvTanggal = findViewById(R.id.tvTanggal);
        btnPilihFoto = findViewById(R.id.btnPilihFoto);
        btnPilihTanggal = findViewById(R.id.btnPilihTanggal);
        btnSimpan = findViewById(R.id.btnSimpan);

        databaseHelper = new DatabaseHelper(this);

        // Pilih Foto
        btnPilihFoto.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, PICK_IMAGE_REQUEST);
        });

        // Pilih Tanggal
        btnPilihTanggal.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
                selectedDate = year + "-" + (month + 1) + "-" + dayOfMonth;
                tvTanggal.setText(selectedDate);
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
        });

        // Simpan Data
        btnSimpan.setOnClickListener(v -> saveProduct());
    }

    private void saveProduct() {
        String nama = etNama.getText().toString().trim();
        String deskripsi = etDeskripsi.getText().toString().trim();
        String hargaStr = etHarga.getText().toString().trim();

        if (!validateInput(nama, deskripsi, hargaStr)) return;

        double harga = Double.parseDouble(hargaStr);
        String kategori = rgKategori.getCheckedRadioButtonId() == R.id.rbMakanan ? "Makanan" : "Minuman";
        boolean promoDiskon = cbPromoDiskon.isChecked();
        boolean takeawayTersedia = cbTakeaway.isChecked();

        // Simpan ke database
        long result = databaseHelper.insertProduct(
                nama, deskripsi, kategori, harga,
                promoDiskon, takeawayTersedia, savedImagePath, selectedDate
        );

        if (result > 0) {
            Toast.makeText(this, "Produk berhasil ditambahkan", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Gagal menambahkan produk. Silakan coba lagi.", Toast.LENGTH_SHORT).show();
            Log.e("TambahActivity", "Insert product failed for: " + nama);
        }
    }

    private boolean validateInput(String nama, String deskripsi, String hargaStr) {
        if (nama.isEmpty()) {
            Toast.makeText(this, "Nama produk tidak boleh kosong!", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (deskripsi.isEmpty()) {
            Toast.makeText(this, "Deskripsi produk tidak boleh kosong!", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (hargaStr.isEmpty()) {
            Toast.makeText(this, "Harga produk tidak boleh kosong!", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (rgKategori.getCheckedRadioButtonId() == -1) {
            Toast.makeText(this, "Pilih kategori produk!", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (imageUri == null) {
            Toast.makeText(this, "Pilih gambar produk!", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (selectedDate == null) {
            Toast.makeText(this, "Pilih tanggal produk!", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            saveImageToInternalStorage(imageUri);
        }
    }

    private void saveImageToInternalStorage(Uri uri) {
        File directory = new File(getFilesDir(), "images");
        if (!directory.exists() && !directory.mkdir()) {
            Log.e("TambahActivity", "Gagal membuat direktori untuk gambar");
            return;
        }

        String fileName = "IMG_" + System.currentTimeMillis() + ".jpg";
        File imageFile = new File(directory, fileName);

        try (InputStream inputStream = getContentResolver().openInputStream(uri);
             FileOutputStream outputStream = new FileOutputStream(imageFile)) {

            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }

            savedImagePath = imageFile.getAbsolutePath(); // Simpan path gambar
            Bitmap bitmap = BitmapFactory.decodeFile(savedImagePath);
            imgPreview.setImageBitmap(bitmap);
            Log.d("TambahActivity", "Gambar berhasil disimpan: " + savedImagePath);

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Gagal menyimpan gambar. Silakan coba lagi.", Toast.LENGTH_SHORT).show();
        }
    }
}
