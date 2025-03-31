package com.example.glutenfreeonthego.Places;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface PlacesApiService {
    @GET("maps/api/place/nearbysearch/json")
    Call<PlacesResponse> getNearbyPlaces(
            @Query("location") String location,
            @Query("radius") int radius,
            @Query("type") String type,
            @Query("keyword") String keyword,
            @Query("key") String apiKey
    );
}