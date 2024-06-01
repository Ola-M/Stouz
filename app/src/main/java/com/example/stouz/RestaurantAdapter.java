package com.example.stouz;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.List;

public class RestaurantAdapter extends RecyclerView.Adapter<RestaurantAdapter.RestaurantViewHolder> {

    private Context context;
    private List<Restaurant> restaurantList;

    public RestaurantAdapter(Context context, List<Restaurant> restaurantList) {
        this.context = context;
        this.restaurantList = restaurantList;
    }

    @NonNull
    @Override
    public RestaurantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_restaurant, parent, false);
        return new RestaurantViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RestaurantViewHolder holder, int position) {
        Restaurant restaurant = restaurantList.get(position);
        holder.textViewName.setText(restaurant.getName());
        holder.textViewHours.setText(restaurant.getOpeningHours());
        holder.textViewRating.setText(String.valueOf(restaurant.getRating()));

        // Calculate and display distance
        float distance = restaurant.getDistance();
        if (distance > 1000) {
            holder.textViewDistance.setText(String.format("%.1f km", distance / 1000));
        } else {
            holder.textViewDistance.setText(String.format("%.0f m", distance));
        }

        // Set rating color based on value
        double rating = restaurant.getRating();
        if (rating >= 4.0) {
            holder.textViewRating.setTextColor(ContextCompat.getColor(context, R.color.green));
        } else if (rating >= 3.0) {
            holder.textViewRating.setTextColor(ContextCompat.getColor(context, R.color.orange));
        } else {
            holder.textViewRating.setTextColor(ContextCompat.getColor(context, R.color.red));
        }

        // Load image using Glide
        Glide.with(context)
                .load(restaurant.getImageUrl())
                .into(holder.imageView);

        // Set favorite button click listener
        holder.buttonFavorite.setOnClickListener(v -> {
            holder.buttonFavorite.setSelected(!holder.buttonFavorite.isSelected());
            holder.buttonFavorite.setImageResource(
                    holder.buttonFavorite.isSelected() ?
                            android.R.drawable.btn_star_big_on :
                            android.R.drawable.btn_star_big_off
            );
        });

        // Add stars based on the rating
        holder.ratingStars.removeAllViews();
        int fullStars = (int) rating;
        int halfStars = (rating - fullStars >= 0.5) ? 1 : 0;
        int emptyStars = 5 - fullStars - halfStars;

        for (int i = 0; i < fullStars; i++) {
            ImageView star = new ImageView(context);
            star.setImageResource(android.R.drawable.star_on);
            holder.ratingStars.addView(star);
        }

        if (halfStars > 0) {
            ImageView star = new ImageView(context);
            star.setImageResource(android.R.drawable.star_on); // Use a half-star image if available
            holder.ratingStars.addView(star);
        }

        for (int i = 0; i < emptyStars; i++) {
            ImageView star = new ImageView(context);
            star.setImageResource(android.R.drawable.star_off);
            holder.ratingStars.addView(star);
        }

        // Set the card click listener to navigate to the detail fragment
        holder.itemView.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putSerializable("restaurant", restaurant);
            Navigation.findNavController(holder.itemView).navigate(R.id.navigation_restaurant_detail, bundle);
        });
    }

    @Override
    public int getItemCount() {
        return restaurantList.size();
    }

    public void filterList(List<Restaurant> filteredList) {
        restaurantList = filteredList;
        notifyDataSetChanged();
    }

    static class RestaurantViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textViewName, textViewHours, textViewRating, textViewDistance;
        ImageButton buttonFavorite;
        LinearLayout ratingStars;

        public RestaurantViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            textViewName = itemView.findViewById(R.id.textViewName);
            textViewHours = itemView.findViewById(R.id.textViewHours);
            textViewRating = itemView.findViewById(R.id.textViewRating);
            textViewDistance = itemView.findViewById(R.id.textViewDistance); // Add a TextView for distance
            buttonFavorite = itemView.findViewById(R.id.buttonFavorite);
            ratingStars = itemView.findViewById(R.id.ratingStars);
        }
    }
}
