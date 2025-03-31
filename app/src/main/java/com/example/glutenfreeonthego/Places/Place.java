package com.example.glutenfreeonthego.Places;

public class Place {
    private String name;
    private Geometry geometry;
    private String vicinity;


    public String getName() {
        return name;
    }

    public Geometry getGeometry() {
        return geometry;
    }

    public String getVicinity() {
        return vicinity;
    }

    public static class Geometry {
        private Location location;

        public Location getLocation() {
            return location;
        }

        public static class Location {
            private double lat;
            private double lng;

            public double getLat() {
                return lat;
            }

            public double getLng() {
                return lng;
            }
        }
    }
}

