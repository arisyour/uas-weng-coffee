package com.ariskurniawan.uaswengcaffe;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {
    @GET("current.json")
    Call<CuacaRespon> getWeather(
            @Query("key") String apiKey,
            @Query("q") String cityName
    );
}
