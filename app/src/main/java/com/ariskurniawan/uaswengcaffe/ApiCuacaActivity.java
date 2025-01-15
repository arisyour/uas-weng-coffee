package com.ariskurniawan.uaswengcaffe;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApiCuacaActivity extends AppCompatActivity {

    private TextView tempTextView, descriptionTextView;
    private SearchView searchView;
    private TextView humidityTextView, windSpeedTextView, lastUpdatedTextView;
    private ImageView weatherDetailImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_api_cuaca); // Layout baru untuk Activity ini

        // Inisialisasi elemen UI
        tempTextView = findViewById(R.id.tempTextView);
        descriptionTextView = findViewById(R.id.descriptionTextView);
        searchView = findViewById(R.id.searchView);
        humidityTextView = findViewById(R.id.humidityTextView);
        windSpeedTextView = findViewById(R.id.windSpeedTextView);
        weatherDetailImage = findViewById(R.id.weatherDetailImage);
        lastUpdatedTextView = findViewById(R.id.lastUpdatedTextView);

        // Tangkap data dari Intent
        if (getIntent() != null) {
            String temp = getIntent().getStringExtra("temp");
            String description = getIntent().getStringExtra("description");

            tempTextView.setText(String.format("Temperature: %s°C", temp != null ? temp : "--"));
            descriptionTextView.setText(String.format("Condition: %s", description != null ? description : "--"));
        }

        // Tambahkan listener untuk SearchView
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                fetchWeatherData(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    private void fetchWeatherData(String city) {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<CuacaRespon> call = apiService.getWeather("025f1f921ac84a3a98b215917251501", city);

        call.enqueue(new Callback<CuacaRespon>() {
            @Override
            public void onResponse(Call<CuacaRespon> call, Response<CuacaRespon> response) {
                if (response.isSuccessful() && response.body() != null) {
                    CuacaRespon weather = response.body();

                    tempTextView.setText(String.format("Temperature: %.1f°C", weather.current.tempC));
                    descriptionTextView.setText(String.format("Condition: %s", weather.current.condition.text));
                    humidityTextView.setText(String.format("%d%%", weather.current.humidity));
                    windSpeedTextView.setText(String.format("%.1f kph", weather.current.windKph));
                    lastUpdatedTextView.setText(weather.current.lastUpdated);

                    Glide.with(ApiCuacaActivity.this)
                            .load("https:" + weather.current.condition.icon)
                            .into(weatherDetailImage);
                } else {
                    Toast.makeText(ApiCuacaActivity.this, "City not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CuacaRespon> call, Throwable t) {
                Toast.makeText(ApiCuacaActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}