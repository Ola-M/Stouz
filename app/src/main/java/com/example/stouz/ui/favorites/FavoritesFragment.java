package com.example.stouz.ui.favorites;

import android.os.Bundle;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.appcompat.widget.SearchView;
import com.example.stouz.R;
import com.example.stouz.Restaurant;
import com.example.stouz.RestaurantAdapter;

import java.util.ArrayList;
import java.util.List;

public class FavoritesFragment extends Fragment {

    private RecyclerView recyclerView;
    private RestaurantAdapter restaurantAdapter;
    private List<Restaurant> restaurantList;
    private List<Restaurant> filteredList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_favorites, container, false);

        SearchView searchView = root.findViewById(R.id.searchView);
        recyclerView = root.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        restaurantList = new ArrayList<>();
        filteredList = new ArrayList<>();

        // Add dummy data for favorites
        restaurantList.add(new Restaurant("Restaurant 1", "9 AM - 9 PM", 4.5, "https://example.com/image1.jpg", 40.748817, -73.985428)); // Example coordinates for NYC
        restaurantList.add(new Restaurant("Restaurant 2", "10 AM - 8 PM", 4.0, "https://example.com/image2.jpg", 37.774929, -122.419416)); // Example coordinates for San Francisco
        restaurantList.add(new Restaurant("Restaurant 3", "11 AM - 10 PM", 3.5, "https://example.com/image3.jpg", 34.052235, -118.243683));
        filteredList.addAll(restaurantList);

        // Initialize the adapter
        restaurantAdapter = new RestaurantAdapter(getContext(), filteredList);
        recyclerView.setAdapter(restaurantAdapter);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return false;
            }
        });

        return root;
    }

    private void filter(String text) {
        filteredList.clear();
        for (Restaurant restaurant : restaurantList) {
            if (restaurant.getName().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(restaurant);
            }
        }
        restaurantAdapter.filterList(filteredList);
    }
}
