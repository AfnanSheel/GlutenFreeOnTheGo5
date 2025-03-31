package com.example.glutenfreeonthego.Fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.glutenfreeonthego.AllConstant;
import com.example.glutenfreeonthego.AppPermissions;
import com.example.glutenfreeonthego.GooglePlaceModel;
import com.example.glutenfreeonthego.Places.Place;
import com.example.glutenfreeonthego.Places.PlaceModel;
import com.example.glutenfreeonthego.Places.PlacesApiService;
import com.example.glutenfreeonthego.Places.PlacesResponse;
import com.example.glutenfreeonthego.R;
import com.example.glutenfreeonthego.SavedPlace;

import com.example.glutenfreeonthego.adapters.GooglePlaceAdapter;
import com.example.glutenfreeonthego.databinding.FragmentHomeBinding;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.material.chip.Chip;

import org.w3c.dom.Text;

import java.net.CookieHandler;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeFragment extends Fragment implements OnMapReadyCallback {

    private FragmentHomeBinding binding;
    private SnapHelper snapHelper;
    private GoogleMap gMap;
    private List<SavedPlace> savedPlaces = SavedPlacesFragment.savedPlaces;

    private AppPermissions appPermissions;
    private LocationRequest locationRequest;
    private LocationCallback locationCallBack;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private PlacesClient placesClient;
    private boolean isLocationPermissionOk, isTrafficEnable;
    private Location currentLocation;
    private Marker currentMarker;
    private String currentFilter = "gluten-free";

    // ActivityResultLauncher for location permission
    private ActivityResultLauncher<String[]> locationPermissionRequest;

    private GooglePlaceAdapter googlePlaceAdapter;
    private List<GooglePlaceModel> googlePlaceModelList;
    private PlaceModel selectedPlaceModel;
    private CardView reviewBox;
    private LinearLayout ratingContainer;
    private Button submitReviewBtn;
    private TextView reviewTitle;
    private int selectedRating = 0;
    private String currentRestaurantName;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        appPermissions = new AppPermissions();

        // Initialize the location permission launcher
        locationPermissionRequest = registerForActivityResult(
                new ActivityResultContracts.RequestMultiplePermissions(),
                result -> {
                    Boolean fineLocationGranted = result.get(Manifest.permission.ACCESS_FINE_LOCATION);
                    Boolean coarseLocationGranted = result.get(Manifest.permission.ACCESS_COARSE_LOCATION);

                    if (fineLocationGranted != null && fineLocationGranted) {
                        // Precise location access granted
                        setUpGoogleMap();
                    } else if (coarseLocationGranted != null && coarseLocationGranted) {
                        // Only approximate location access granted
                        setUpGoogleMap();
                    } else {
                        // No location access granted
                        Toast.makeText(requireContext(), "Location permission denied", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize the map using SupportMapFragment
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.homeMap);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        for (PlaceModel placeModel : AllConstant.placesName) {
            Chip chip = new Chip(requireContext());
            chip.setText(placeModel.getName());
            chip.setId(placeModel.getId());
            chip.setPadding(8, 8, 8, 8);
            chip.setTextColor(getResources().getColor(R.color.white, null));
            chip.setChipBackgroundColor(getResources().getColorStateList(R.color.buttonGreen, null));
            chip.setChipIcon(ResourcesCompat.getDrawable(getResources(), placeModel.getDrawableId(), null));
            chip.setCheckable(true);
            chip.setCheckedIconVisible(false);

            // Add click listener to each chip
            chip.setOnClickListener(v -> {
                // Reset all chips
                for (int i = 0; i < binding.placesGroup.getChildCount(); i++) {
                    View child = binding.placesGroup.getChildAt(i);
                    if (child instanceof Chip) {
                        ((Chip) child).setChecked(false);
                    }
                }

                // Set the clicked chip as checked
                chip.setChecked(true);

                // Build the filter string based on selection
                StringBuilder filterBuilder = new StringBuilder("gluten-free");

                switch (placeModel.getId()) {
                    case 1: // Halal
                        filterBuilder.append(" halal");
                        break;
                    case 2: // Vegan
                        filterBuilder.append(" vegan");
                        break;
                    case 3: // Vegetarian
                        filterBuilder.append(" vegetarian");
                        break;
                    // Case 4 is just gluten-free, which is already in the builder
                }

                currentFilter = filterBuilder.toString().trim();

                // Refresh the search with new filter
                if (currentLocation != null) {
                    searchGlutenFreeRestaurants(new LatLng(
                            currentLocation.getLatitude(),
                            currentLocation.getLongitude()
                    ));
                }
            });

            binding.placesGroup.addView(chip);
        }

        // Initialize fused location provider client
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity());
        initReviewBox();
    }

    @SuppressLint("PotentialBehaviorOverride")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        gMap = googleMap;

        // Set up marker click listener
        gMap.setOnMarkerClickListener(marker -> {
            if (marker.getTag() != null && marker.getTag().equals(703)) {
                // This is the current location marker
                return false;
            }

            // Show restaurant details dialog
            showRestaurantDetailsDialog(
                    marker.getTitle(),
                    marker.getSnippet(),
                    currentFilter.contains("halal"),
                    currentFilter.contains("vegan"),
                    currentFilter.contains("vegetarian"),
                    new Random().nextInt(5) + 1, // Random hygiene rating for demo
                    marker.getPosition().latitude,
                    marker.getPosition().longitude
            );
            return true;
        });

        if (appPermissions.isLocationOk(requireContext())) {
            setUpGoogleMap();
        } else {
            showLocationPermissionDialog();
        }
    }

    private void showLocationPermissionDialog() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION)) {
            new AlertDialog.Builder(requireContext())
                    .setTitle("Location Permission")
                    .setMessage("This app needs location permission to show nearby gluten-free restaurants")
                    .setPositiveButton("OK", (dialog, which) -> requestLocationPermission())
                    .setNegativeButton("Cancel", null)
                    .create()
                    .show();
        } else {
            requestLocationPermission();
        }
    }

    private void requestLocationPermission() {
        locationPermissionRequest.launch(new String[]{
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
        });
    }

    private void setUpGoogleMap() {
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestLocationPermission();
            return;
        }

        gMap.setMyLocationEnabled(true);
        gMap.getUiSettings().setMyLocationButtonEnabled(true);
        gMap.getUiSettings().setTiltGesturesEnabled(true);

        setUpLocationUpdate();
    }

    private void setUpLocationUpdate() {
        locationRequest = LocationRequest.create();
        locationRequest.setInterval(10000); // Update interval in milliseconds
        locationRequest.setFastestInterval(5000); // Fastest update interval
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        locationCallBack = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult != null) {
                    for (Location location : locationResult.getLocations()) {
                        Log.d("TAG", "onLocationResult: " + location.getLongitude() + " " + location.getLatitude());
                        currentLocation = location;
                        moveCameraToLocation(location);
                    }
                }
                super.onLocationResult(locationResult);
            }
        };

        startLocationUpdates();
    }

    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestLocationPermission();
            return;
        }

        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallBack, Looper.getMainLooper())
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(requireContext(), "Location updates started", Toast.LENGTH_SHORT).show();
                    }
                });

        getCurrentLocation();
    }

    private void getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestLocationPermission();
            return;
        }

        fusedLocationProviderClient.getLastLocation()
                .addOnSuccessListener(location -> {
                    if (location != null) {
                        currentLocation = location;
                        moveCameraToLocation(location);
                        searchGlutenFreeRestaurants(new LatLng(location.getLatitude(), location.getLongitude()));
                    } else {
                        // If last location is not available, start location updates
                        startLocationUpdates();
                    }
                });
    }

    private void moveCameraToLocation(Location location) {
        if (location == null || gMap == null) return;

        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 15);

        MarkerOptions markerOptions = new MarkerOptions()
                .position(latLng)
                .title("Current Location")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));

        if (currentMarker != null) {
            currentMarker.remove();
        }

        currentMarker = gMap.addMarker(markerOptions);
        currentMarker.setTag(703);
        gMap.animateCamera(cameraUpdate);
    }


    private void stopLocationUpdates() {
        if (fusedLocationProviderClient != null && locationCallBack != null) {
            fusedLocationProviderClient.removeLocationUpdates(locationCallBack);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isLocationPermissionOk) {
            startLocationUpdates(); // Only start updates when fragment is visible
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        stopLocationUpdates(); // Stop updates when fragment is not visible
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopLocationUpdates(); // Clean up when fragment is destroyed
    }

    private void searchGlutenFreeRestaurants(LatLng location) {
        // Clear existing markers
        if (gMap != null) {
            gMap.clear();
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://maps.googleapis.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        PlacesApiService service = retrofit.create(PlacesApiService.class);

        String locationString = location.latitude + "," + location.longitude;
        int radius = 5000;
        String type = "restaurant";
        String apiKey = getString(R.string.API_KEY);

        // Ensure the keyword is properly encoded
        String keyword = currentFilter.replace(" ", "+");

        Call<PlacesResponse> call = service.getNearbyPlaces(locationString, radius, type, keyword, apiKey);
        call.enqueue(new Callback<PlacesResponse>() {
            @Override
            public void onResponse(Call<PlacesResponse> call, Response<PlacesResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Place> places = response.body().getResults();

                    if (places.isEmpty()) {
                        Toast.makeText(requireContext(), "No restaurants found with these filters", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // Set up the recycler view with the places
                    setUpRecyclerView(places);

                    // Add markers to the map
                    for (Place place : places) {
                        LatLng restaurantLocation = new LatLng(
                                place.getGeometry().getLocation().getLat(),
                                place.getGeometry().getLocation().getLng()
                        );
                        gMap.addMarker(new MarkerOptions()
                                .position(restaurantLocation)
                                .title(place.getName())
                                .snippet(place.getVicinity()));
                    }
                } else {
                    Toast.makeText(requireContext(), "Failed to fetch restaurants", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PlacesResponse> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(requireContext(), "Network Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }



    // Add this method to HomeFragment
    private void showRestaurantDetailsDialog(String name, String address, boolean isHalal,
                                             boolean isVegan, boolean isVegetarian,
                                             double hygieneRating, double lat, double lng) {
        currentRestaurantName = name;

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle(name);

        String message = "Address: " + address + "\n\n" +
                "Dietary Options:\n" +
                (isHalal ? "✓ Halal\n" : "") +
                (isVegan ? "✓ Vegan\n" : "") +
                (isVegetarian ? "✓ Vegetarian\n" : "") +
                "\nHygiene Rating: " + hygieneRating + "/5";

        builder.setMessage(message);

        builder.setPositiveButton("Save Location", (dialog, which) -> {
            SavedPlace place = new SavedPlace(name, address, isHalal, isVegan,
                    isVegetarian, hygieneRating, lat, lng);
            SavedPlacesFragment.addSavedPlace(place);
            Toast.makeText(requireContext(), "Location saved", Toast.LENGTH_SHORT).show();
        });

        builder.setNeutralButton("Leave Review", (dialog, which) -> {
            reviewTitle.setText("Review for " + name);
            selectedRating = 0;
            updateStarRating();
            reviewBox.setVisibility(View.VISIBLE);
        });

        builder.setNegativeButton("Close", null);

        builder.show();
    }

    private void setUpRecyclerView(List<Place> places) {
        // Convert Places to GooglePlaceModels
        googlePlaceModelList = new ArrayList<>();
        for (Place place : places) {
            GooglePlaceModel model = new GooglePlaceModel();
            model.setName(place.getName());
            model.setVicinity(place.getVicinity());
            googlePlaceModelList.add(model);
        }

        binding.placesRecycleView.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        binding.placesRecycleView.setHasFixedSize(false);

        googlePlaceAdapter = new GooglePlaceAdapter();
        googlePlaceAdapter.setGooglePlaceModels(googlePlaceModelList);
        binding.placesRecycleView.setAdapter(googlePlaceAdapter);

        // Remove existing SnapHelper if one exists
        if (snapHelper != null) {
            snapHelper.attachToRecyclerView(null);
        }

        // Create and attach new SnapHelper
        snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(binding.placesRecycleView);

        binding.placesRecycleView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

                int position = linearLayoutManager.findFirstCompletelyVisibleItemPosition();
                if (position > -1 && googlePlaceModelList != null && position < googlePlaceModelList.size()) {
                    GooglePlaceModel googlePlaceModel = googlePlaceModelList.get(position);
                    // Handle position change if needed
                }
            }
        });

        googlePlaceAdapter.setOnItemClickListener(position -> {
            if (googlePlaceModelList != null && position < googlePlaceModelList.size()) {
                GooglePlaceModel placeModel = googlePlaceModelList.get(position);
                showRestaurantDetailsDialog(
                        placeModel.getName(),
                        placeModel.getAddress(),
                        currentFilter.contains("halal"),
                        currentFilter.contains("vegan"),
                        currentFilter.contains("vegetarian"),
                        new Random().nextInt(5) + 1,
                        currentLocation.getLatitude(),
                        currentLocation.getLongitude()
                );
            }
        });
    }

    private void initReviewBox() {
        reviewBox = getView().findViewById(R.id.reviewBox);
        ratingContainer = getView().findViewById(R.id.ratingContainer);
        submitReviewBtn = getView().findViewById(R.id.submitReviewBtn);
        reviewTitle = getView().findViewById(R.id.reviewTitle);

        // Create star rating UI
        for (int i = 1; i <= 5; i++) {
            ImageView star = new ImageView(requireContext());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(4, 0, 4, 0);
            star.setLayoutParams(params);
            star.setImageResource(R.drawable.star_outline); // You need to add this drawable
            star.setTag(i);

            star.setOnClickListener(v -> {
                selectedRating = (int) v.getTag();
                updateStarRating();
            });

            ratingContainer.addView(star);
        }

        submitReviewBtn.setOnClickListener(v -> {
            if (selectedRating > 0) {
                // Here you would typically send the review to your backend
                Toast.makeText(requireContext(),
                        "Thanks for your " + selectedRating + " star review for " + currentRestaurantName,
                        Toast.LENGTH_SHORT).show();

                // Reset the review box
                selectedRating = 0;
                updateStarRating();
                reviewBox.setVisibility(View.GONE);
            } else {
                Toast.makeText(requireContext(), "Please select a rating", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateStarRating() {
        for (int i = 0; i < ratingContainer.getChildCount(); i++) {
            ImageView star = (ImageView) ratingContainer.getChildAt(i);
            int starValue = (int) star.getTag();

            if (starValue <= selectedRating) {
                star.setImageResource(R.drawable.star_filled); // Filled star
                star.setColorFilter(ContextCompat.getColor(requireContext(), R.color.buttonGreen));
            } else {
                star.setImageResource(R.drawable.star_outline); // Outline star
                star.setColorFilter(ContextCompat.getColor(requireContext(), R.color.black));
            }
        }
    }
}