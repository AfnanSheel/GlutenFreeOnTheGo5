package com.example.glutenfreeonthego.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.glutenfreeonthego.R;
import com.example.glutenfreeonthego.SavedPlace;
import com.example.glutenfreeonthego.adapters.SavedPlacesAdapter;

import java.util.ArrayList;
import java.util.List;

public class SavedPlacesFragment extends Fragment {

    private RecyclerView savedPlacesRecyclerView;
    private SavedPlacesAdapter savedPlacesAdapter;
    public static List<SavedPlace> savedPlaces = new ArrayList<>();

    public SavedPlacesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_saved_places, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        savedPlacesRecyclerView = view.findViewById(R.id.savedPlacesRecyclerView);
        savedPlacesRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        savedPlacesAdapter = new SavedPlacesAdapter(savedPlaces);
        savedPlacesRecyclerView.setAdapter(savedPlacesAdapter);
    }

    // Method to add a new saved place (can be called from HomeFragment)
    public static void addSavedPlace(SavedPlace place) {
        savedPlaces.add(place);
    }

    // Method to clear all saved places
    public static void clearSavedPlaces() {
        savedPlaces.clear();
    }
}