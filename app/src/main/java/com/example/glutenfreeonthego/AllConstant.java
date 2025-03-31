package com.example.glutenfreeonthego;

import com.example.glutenfreeonthego.Places.PlaceModel;

import java.util.ArrayList;
import java.util.Arrays;

public interface AllConstant {

    int STORAGE_REQUEST_CODE = 1000;
    int LOCATION_REQUEST_CODE = 2000;
    String IMAGE_PATH = "/Profile/image_profile.jpg";


    ArrayList<PlaceModel> placesName = new ArrayList<>(
            Arrays.asList(
                    new PlaceModel(1, R.drawable.restaurant, "Halal", "halal"),
                    new PlaceModel(2, R.drawable.restaurant, "Vegan", "vegan"),
                    new PlaceModel(3, R.drawable.restaurant, "Vegetarian", "vegetarian"),
                    new PlaceModel(4, R.drawable.restaurant, "Coeliac-Safe", "coeliac safe")

            )
    );
}