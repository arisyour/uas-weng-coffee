package com.ariskurniawan.uaswengcaffe;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerViewProduk;
    private ProdukAdapter adapter;
    private DatabaseHelper databaseHelper;
    private List<Produk> produkList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inisialisasi Views
        recyclerViewProduk = findViewById(R.id.recyclerViewProduk);
        Button btnTambah = findViewById(R.id.btnTambah);

        // Inisialisasi Database dan Adapter
        databaseHelper = new DatabaseHelper(this);
        produkList = new ArrayList<>();
        adapter = new ProdukAdapter(this, produkList);
        recyclerViewProduk.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewProduk.setAdapter(adapter);

        // Load data dari database
        loadData();

        // Navigasi ke TambahActivity
        btnTambah.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, TambahActivity.class);
            startActivityForResult(intent, 1); // Gunakan startActivityForResult untuk mengupdate RecyclerView setelah kembali
        });
    }

    private void loadData() {
        produkList.clear();
        Cursor cursor = databaseHelper.getAllProducts();
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    produkList.add(new Produk(
                            cursor.getInt(cursor.getColumnIndexOrThrow("id")), // ID produk
                            cursor.getString(cursor.getColumnIndexOrThrow("nama")), // Nama produk
                            cursor.getString(cursor.getColumnIndexOrThrow("deskripsi")), // Deskripsi produk
                            cursor.getString(cursor.getColumnIndexOrThrow("kategori")), // Kategori produk
                            cursor.getDouble(cursor.getColumnIndexOrThrow("harga")), // Harga produk
                            cursor.getInt(cursor.getColumnIndexOrThrow("promo_diskon")) == 1, // Promo diskon
                            cursor.getInt(cursor.getColumnIndexOrThrow("takeaway_tersedia")) == 1, // Takeaway tersedia
                            cursor.getString(cursor.getColumnIndexOrThrow("foto")), // Foto produk
                            cursor.getString(cursor.getColumnIndexOrThrow("tanggal")) // Tanggal produk
                    ));
                } while (cursor.moveToNext());
            }
            cursor.close();
        }

        // Tampilkan pesan jika tidak ada data
        if (produkList.isEmpty()) {
            Toast.makeText(this, "Belum ada produk yang ditambahkan", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Perbarui data setiap kali aktivitas kembali ke foreground
        loadData();
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            // Perbarui data setelah kembali dari TambahActivity
            loadData();
            adapter.notifyDataSetChanged();
        }
    }
}
