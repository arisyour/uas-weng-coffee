package com.ariskurniawan.uaswengcaffe;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;

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

        recyclerViewProduk = findViewById(R.id.recyclerViewProduk);
        Button btnTambah = findViewById(R.id.btnTambah);

        databaseHelper = new DatabaseHelper(this);
        produkList = new ArrayList<>();

        loadData();

        adapter = new ProdukAdapter(this, produkList);
        recyclerViewProduk.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewProduk.setAdapter(adapter);

        btnTambah.setOnClickListener(v -> {
            // Navigasi ke TambahActivity
            startActivity(new Intent(MainActivity.this, TambahActivity.class));
        });
    }

    private void loadData() {
        produkList.clear();
        Cursor cursor = databaseHelper.getAllProducts();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                produkList.add(new Produk(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getDouble(4),
                        cursor.getInt(5) == 1,
                        cursor.getInt(6) == 1,
                        cursor.getInt(7) == 1,
                        cursor.getString(8)
                ));
            } while (cursor.moveToNext());
            cursor.close();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
        adapter.notifyDataSetChanged();
    }
}
