package com.ariskurniawan.uaswengcaffe;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
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

public class UpdateActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    private EditText etNama, etDeskripsi, etHarga;
    private RadioGroup rgKategori;
    private CheckBox cbPromoDiskon, cbTakeaway;
    private ImageView imgPreview;
    private TextView tvTanggal;
    private Button btnPilihFoto, btnPilihTanggal, btnUpdate;
    private DatabaseHelper databaseHelper;

    private Uri imageUri;
    private String selectedDate;
    private String savedImagePath; // Path gambar yang disimpan
    private int productId;

    private String originalNama, originalDeskripsi, originalKategori, originalFoto, originalTanggal;
    private double originalHarga;
    private boolean originalPromoDiskon, originalTakeawayTersedia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

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
        btnUpdate = findViewById(R.id.btnUpdate);

        databaseHelper = new DatabaseHelper(this);

        // Ambil ID produk dari Intent
        productId = getIntent().getIntExtra("PRODUCT_ID", -1);
        if (productId == -1) {
            Toast.makeText(this, "Data produk tidak ditemukan", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Load data produk
        loadProductData(productId);

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

        // Tombol Update
        btnUpdate.setOnClickListener(v -> updateProduct());
    }

    private void loadProductData(int id) {
        Cursor cursor = databaseHelper.getProductById(id);
        if (cursor != null && cursor.moveToFirst()) {
            originalNama = cursor.getString(cursor.getColumnIndexOrThrow("nama"));
            originalDeskripsi = cursor.getString(cursor.getColumnIndexOrThrow("deskripsi"));
            originalHarga = cursor.getDouble(cursor.getColumnIndexOrThrow("harga"));
            originalKategori = cursor.getString(cursor.getColumnIndexOrThrow("kategori"));
            originalPromoDiskon = cursor.getInt(cursor.getColumnIndexOrThrow("promo_diskon")) == 1;
            originalTakeawayTersedia = cursor.getInt(cursor.getColumnIndexOrThrow("takeaway_tersedia")) == 1;
            originalFoto = cursor.getString(cursor.getColumnIndexOrThrow("foto"));
            originalTanggal = cursor.getString(cursor.getColumnIndexOrThrow("tanggal"));

            // Isi data ke UI
            etNama.setText(originalNama);
            etDeskripsi.setText(originalDeskripsi);
            etHarga.setText(String.valueOf(originalHarga));
            if ("Makanan".equals(originalKategori)) {
                rgKategori.check(R.id.rbMakanan);
            } else if ("Minuman".equals(originalKategori)) {
                rgKategori.check(R.id.rbMinuman);
            }
            cbPromoDiskon.setChecked(originalPromoDiskon);
            cbTakeaway.setChecked(originalTakeawayTersedia);
            if (originalFoto != null && !originalFoto.isEmpty()) {
                savedImagePath = originalFoto;
                Bitmap bitmap = BitmapFactory.decodeFile(savedImagePath);
                imgPreview.setImageBitmap(bitmap);
            }
            selectedDate = originalTanggal;
            tvTanggal.setText(originalTanggal);
        }
        if (cursor != null) cursor.close();
    }

    private void updateProduct() {
        String nama = etNama.getText().toString().trim();
        String deskripsi = etDeskripsi.getText().toString().trim();
        String hargaStr = etHarga.getText().toString().trim();

        if (!validateInput(nama, deskripsi, hargaStr)) return;

        double harga = Double.parseDouble(hargaStr);
        String kategori = rgKategori.getCheckedRadioButtonId() == R.id.rbMakanan ? "Makanan" : "Minuman";
        boolean promoDiskon = cbPromoDiskon.isChecked();
        boolean takeawayTersedia = cbTakeaway.isChecked();
        String foto = savedImagePath;
        String tanggal = selectedDate != null ? selectedDate : originalTanggal;

        if (!nama.equals(originalNama) || !deskripsi.equals(originalDeskripsi) || harga != originalHarga ||
                !kategori.equals(originalKategori) || promoDiskon != originalPromoDiskon ||
                takeawayTersedia != originalTakeawayTersedia || !foto.equals(originalFoto) ||
                !tanggal.equals(originalTanggal)) {

            long result = databaseHelper.updateProduct(productId, nama, deskripsi, kategori, harga,
                    promoDiskon, takeawayTersedia, foto, tanggal);

            if (result > 0) {
                Toast.makeText(this, "Produk berhasil diperbarui", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Gagal memperbarui produk. Silakan coba lagi.", Toast.LENGTH_SHORT).show();
                Log.e("UpdateActivity", "Update failed for product ID: " + productId);
            }
        } else {
            Toast.makeText(this, "Tidak ada perubahan data", Toast.LENGTH_SHORT).show();
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
            Log.e("UpdateActivity", "Gagal membuat direktori untuk gambar");
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

            savedImagePath = imageFile.getAbsolutePath();
            Bitmap bitmap = BitmapFactory.decodeFile(savedImagePath);
            imgPreview.setImageBitmap(bitmap);
            Log.d("UpdateActivity", "Gambar berhasil disimpan: " + savedImagePath);

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Gagal menyimpan gambar. Silakan coba lagi.", Toast.LENGTH_SHORT).show();
        }
    }
}
