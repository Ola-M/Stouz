package com.example.stouz.ui.restaurantDetails;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.stouz.R;
import com.example.stouz.Restaurant;
import com.example.stouz.RestaurantMenuItem;
import com.example.stouz.RestaurantMenuAdapter;
import java.util.ArrayList;
import java.util.List;

public class RestaurantDetailFragment extends Fragment {

    private ImageView imageView;
    private TextView textViewName, textViewHours;
    private RatingBar ratingBar;
    private RecyclerView menuRecyclerView;
    private RestaurantMenuAdapter menuAdapter;
    private List<RestaurantMenuItem> menuList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_restaurant_detail, container, false);

        imageView = root.findViewById(R.id.imageView);
        textViewName = root.findViewById(R.id.textViewName);
        textViewHours = root.findViewById(R.id.textViewHours);
        ratingBar = root.findViewById(R.id.ratingBar);
        menuRecyclerView = root.findViewById(R.id.menuRecyclerView);

        // Get the restaurant data from arguments
        Bundle args = getArguments();
        if (args != null) {
            Restaurant restaurant = (Restaurant) args.getSerializable("restaurant");
            if (restaurant != null) {
                textViewName.setText(restaurant.getName());
                textViewHours.setText(getString(R.string.opening_hours, restaurant.getOpeningHours()));
                ratingBar.setRating((float) restaurant.getRating());

                Glide.with(getContext())
                        .load(restaurant.getImageUrl())
                        .into(imageView);
            }
        }

        menuList = new ArrayList<>();
        // Add dummy data for menu items
        menuList.add(new RestaurantMenuItem("Dish 1", "Description 1", "https://thekebabshop.com/wp-content/uploads/2023/04/Web-Wrap.png"));
        menuList.add(new RestaurantMenuItem("Dish 2", "Description 2", "https://example.com/dish2.jpg"));
        menuList.add(new RestaurantMenuItem("Dish 3", "Description 3", "https://example.com/dish3.jpg"));

        menuAdapter = new RestaurantMenuAdapter(getContext(), menuList);
        menuRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        menuRecyclerView.setAdapter(menuAdapter);

        return root;
    }
}
