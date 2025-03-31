package com.example.glutenfreeonthego.`interface`

import com.example.glutenfreeonthego.GooglePlaceModel

interface NearLocationInterface {

    fun onSaveClick(googlePlaceModel: GooglePlaceModel)

    fun onDirectionClick(googlePlaceModel: GooglePlaceModel)
}