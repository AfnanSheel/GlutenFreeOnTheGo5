package com.example.glutenfreeonthego.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.glutenfreeonthego.R;
import com.example.glutenfreeonthego.SavedPlace;

import java.util.List;

public class SavedPlacesAdapter extends RecyclerView.Adapter<SavedPlacesAdapter.SavedPlaceViewHolder> {

    private List<SavedPlace> savedPlaces;

    public SavedPlacesAdapter(List<SavedPlace> savedPlaces) {
        this.savedPlaces = savedPlaces;
    }

    @NonNull
    @Override
    public SavedPlaceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_saved_place, parent, false);
        return new SavedPlaceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SavedPlaceViewHolder holder, int position) {
        SavedPlace place = savedPlaces.get(position);
        holder.nameTextView.setText(place.getName());
        holder.addressTextView.setText(place.getAddress());

        StringBuilder dietaryOptions = new StringBuilder();
        if (place.isHalal()) dietaryOptions.append("Halal, ");
        if (place.isVegan()) dietaryOptions.append("Vegan, ");
        if (place.isVegetarian()) dietaryOptions.append("Vegetarian");

        // Remove trailing comma if exists
        String options = dietaryOptions.toString();
        if (options.endsWith(", ")) {
            options = options.substring(0, options.length() - 2);
        }

        holder.dietaryOptionsTextView.setText(options);
        holder.hygieneRatingTextView.setText("Hygiene Rating: " + place.getHygieneRating() + "/5");
    }

    @Override
    public int getItemCount() {
        return savedPlaces.size();
    }

    static class SavedPlaceViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        TextView addressTextView;
        TextView dietaryOptionsTextView;
        TextView hygieneRatingTextView;

        public SavedPlaceViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            addressTextView = itemView.findViewById(R.id.addressTextView);
            dietaryOptionsTextView = itemView.findViewById(R.id.dietaryOptionsTextView);
            hygieneRatingTextView = itemView.findViewById(R.id.hygieneRatingTextView);
        }
    }
}