package com.example.glutenfreeonthego.utilities;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.example.glutenfreeonthego.GooglePlaceModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class SavedPlacesManager {
    private static final String PREFS_NAME = "saved_places_prefs";
    private static final String KEY_SAVED_PLACES = "saved_places";

    public static void savePlace(Context context, GooglePlaceModel place) {
        List<GooglePlaceModel> savedPlaces = getSavedPlaces(context);
        boolean exists = false;
        for (GooglePlaceModel p : savedPlaces) {
            if (p.getPlaceId().equals(place.getPlaceId())) {
                exists = true;
                break;
            }
        }

        if (!exists) {
            savedPlaces.add(place);
            savePlaces(context, savedPlaces);
            Toast.makeText(context, "Place saved", Toast.LENGTH_SHORT).show();
        }
    }

    public static void removePlace(Context context, GooglePlaceModel place) {
        List<GooglePlaceModel> savedPlaces = getSavedPlaces(context);
        List<GooglePlaceModel> toRemove = new ArrayList<>();
        for (GooglePlaceModel p : savedPlaces) {
            if (p.getPlaceId().equals(place.getPlaceId())) {
                toRemove.add(p);
            }
        }
        savedPlaces.removeAll(toRemove);
        savePlaces(context, savedPlaces);
        Toast.makeText(context, "Place removed", Toast.LENGTH_SHORT).show();
    }

    public static List<GooglePlaceModel> getSavedPlaces(Context context) {
        SharedPreferences sharedPrefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String json = sharedPrefs.getString(KEY_SAVED_PLACES, null);
        if (json == null) {
            return new ArrayList<>();
        }

        Type type = new TypeToken<List<GooglePlaceModel>>() {}.getType();
        List<GooglePlaceModel> places = new Gson().fromJson(json, type);
        return places != null ? places : new ArrayList<>();
    }

    private static void savePlaces(Context context, List<GooglePlaceModel> places) {
        SharedPreferences sharedPrefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        String json = new Gson().toJson(places);
        editor.putString(KEY_SAVED_PLACES, json);
        editor.apply();
    }
}