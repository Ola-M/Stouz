package com.example.stouz.ui.favorites;

import android.os.Bundle;
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
import com.example.stouz.adapters.RestaurantAdapter;
import com.example.stouz.models.Restaurant;
import com.example.stouz.repositories.RestaurantRepository;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

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

        restaurantAdapter = new RestaurantAdapter(getContext(), filteredList);
        recyclerView.setAdapter(restaurantAdapter);
        fetchFavoriteRestaurants();

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
        if (restaurantAdapter == null) {
            return;
        }
        filteredList.clear();
        for (Restaurant restaurant : restaurantList) {
            if (restaurant.getName().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(restaurant);
            }
        }
        restaurantAdapter.filterList(filteredList);
    }

    private void fetchFavoriteRestaurants() {
        new RestaurantRepository().getRestaurantsList(new RestaurantRepository.DataStatus() {
            @Override
            public void DataIsLoaded(List<Restaurant> restaurants) {
                restaurantList.clear();
                for (Restaurant restaurant : restaurants) {
                    if (restaurant.getUserFavorites() != null && restaurant.getUserFavorites().contains(FirebaseAuth.getInstance().getCurrentUser().getEmail())) {
                        restaurantList.add(restaurant);
                    }
                }
                filteredList.clear();
                filteredList.addAll(restaurantList);
                restaurantAdapter.notifyDataSetChanged();
            }
        });
    }
}
