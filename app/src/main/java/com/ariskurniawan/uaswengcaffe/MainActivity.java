package com.ariskurniawan.uaswengcaffe;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerViewProduk;
    private ProdukAdapter adapter;
    private DatabaseHelper databaseHelper;
    private List<Produk> produkList;

    private CardView weatherCard;
    private TextView weatherName, weatherTemp, weatherLocation;
    private ImageView weatherImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        weatherCard = findViewById(R.id.weatherCard);
        weatherName = findViewById(R.id.weatherName);
        weatherTemp = findViewById(R.id.weatherTemp);
        weatherLocation = findViewById(R.id.weatherLocation);
        weatherImage = findViewById(R.id.weatherImage);


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

        fetchWeatherData("pontianak");
    }

    private void fetchWeatherData(String city) {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<CuacaRespon> call = apiService.getWeather("025f1f921ac84a3a98b215917251501", city);

        call.enqueue(new Callback<CuacaRespon>() {
            @Override
            public void onResponse(Call<CuacaRespon> call, Response<CuacaRespon> response) {
                if (response.isSuccessful() && response.body() != null) {
                    updateWeatherCard(response.body());
                } else {
                    Toast.makeText(MainActivity.this, "City not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CuacaRespon> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateWeatherCard(CuacaRespon weather) {
        weatherName.setText(weather.current.condition.text);
        weatherTemp.setText(String.format("%.1f°C", weather.current.tempC));
        weatherLocation.setText(weather.location.name);

        Glide.with(this)
                .load("https:" + weather.current.condition.icon)
                .into(weatherImage);

        weatherCard.setOnClickListener(v -> openWeatherDetailFragment(weather));
    }


    private void openWeatherDetailFragment(CuacaRespon weather) {
        Intent intent = new Intent(MainActivity.this, ApiCuacaActivity.class);
        intent.putExtra("temp", String.valueOf(weather.current.tempC));
        intent.putExtra("description", weather.current.condition.text);
        startActivity(intent);
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
