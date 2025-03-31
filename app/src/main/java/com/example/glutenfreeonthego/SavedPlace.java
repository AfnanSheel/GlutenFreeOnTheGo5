package com.example.glutenfreeonthego;

public class SavedPlace {
    private String name;
    private String address;
    private boolean isHalal;
    private boolean isVegan;
    private boolean isVegetarian;
    private double hygieneRating;
    private double lat;
    private double lng;

    public SavedPlace(String name, String address, boolean isHalal, boolean isVegan,
                      boolean isVegetarian, double hygieneRating, double lat, double lng) {
        this.name = name;
        this.address = address;
        this.isHalal = isHalal;
        this.isVegan = isVegan;
        this.isVegetarian = isVegetarian;
        this.hygieneRating = hygieneRating;
        this.lat = lat;
        this.lng = lng;
    }

    // Getters and setters
    public String getName() { return name; }
    public String getAddress() { return address; }
    public boolean isHalal() { return isHalal; }
    public boolean isVegan() { return isVegan; }
    public boolean isVegetarian() { return isVegetarian; }
    public double getHygieneRating() { return hygieneRating; }
    public double getLat() { return lat; }
    public double getLng() { return lng; }
}