package com.example.glutenfreeonthego.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.glutenfreeonthego.GooglePlaceModel;
import com.example.glutenfreeonthego.R;
import com.example.glutenfreeonthego.databinding.RestaurantLayoutBinding;

import java.util.List;

public class GooglePlaceAdapter extends RecyclerView.Adapter<GooglePlaceAdapter.ViewHolder> {
    private List<GooglePlaceModel> googlePlaceModels;
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RestaurantLayoutBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.restaurant_layout, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (googlePlaceModels != null) {
            GooglePlaceModel placeModel = googlePlaceModels.get(position);
            holder.binding.setGooglePlaceModel(placeModel);

            // Set click listener
            holder.itemView.setOnClickListener(v -> {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if (googlePlaceModels != null)
            return googlePlaceModels.size();
        else
            return 0;
    }

    public void setGooglePlaceModels(List<GooglePlaceModel> googlePlaceModels) {
        this.googlePlaceModels = googlePlaceModels;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private RestaurantLayoutBinding binding;

        public ViewHolder(@NonNull RestaurantLayoutBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}