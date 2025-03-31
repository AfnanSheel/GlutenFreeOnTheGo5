package com.example.glutenfreeonthego;

import androidx.annotation.Nullable;

public class GooglePlaceModel {
    private String name;
    private String vicinity;
    private String icon;
    private String placeId;
    private Float rating;
    private Geometry geometry;
    private boolean saved;
    private boolean isHalal;
    private boolean isVegan;
    private boolean isVegetarian;
    private Integer hygieneRating;
    private String address;

    public GooglePlaceModel() {
    }

    // Getters and Setters
    public String getName() {
        return name != null ? name : "No Name";
    }

    public String getAddress() {
        return address;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVicinity(String vicinity) {
        return this.vicinity != null ? this.vicinity : "No Address";
    }

    public void setVicinity(String vicinity) {
        this.vicinity = vicinity;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public Float getRating() {
        return rating;
    }

    public void setRating(Float rating) {
        this.rating = rating;
    }

    public Geometry getGeometry() {
        return geometry;
    }

    public void setGeometry(Geometry geometry) {
        this.geometry = geometry;
    }

    public boolean isSaved() {
        return saved;
    }

    public void setSaved(boolean saved) {
        this.saved = saved;
    }

    public boolean isHalal() {
        return isHalal;
    }

    public void setHalal(boolean halal) {
        isHalal = halal;
    }

    public boolean isVegan() {
        return isVegan;
    }

    public void setVegan(boolean vegan) {
        isVegan = vegan;
    }

    public boolean isVegetarian() {
        return isVegetarian;
    }

    public void setVegetarian(boolean vegetarian) {
        isVegetarian = vegetarian;
    }

    public Integer getHygieneRating() {
        return hygieneRating;
    }

    public void setHygieneRating(Integer hygieneRating) {
        this.hygieneRating = hygieneRating;
    }

    public static class Geometry {
        private Location location;

        public Location getLocation() {
            return location;
        }

        public void setLocation(Location location) {
            this.location = location;
        }

        public static class Location {
            private double lat;
            private double lng;

            public double getLat() {
                return lat;
            }

            public void setLat(double lat) {
                this.lat = lat;
            }

            public double getLng() {
                return lng;
            }

            public void setLng(double lng) {
                this.lng = lng;
            }
        }
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        GooglePlaceModel that = (GooglePlaceModel) obj;
        return placeId != null && placeId.equals(that.placeId);
    }

    @Override
    public int hashCode() {
        return placeId != null ? placeId.hashCode() : 0;
    }
}