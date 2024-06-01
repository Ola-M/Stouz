package com.example.stouz.ui.home;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
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
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class HomeFragment extends Fragment {

    private static final int REQUEST_LOCATION_PERMISSION = 1;
    private RecyclerView recyclerView;
    private RestaurantAdapter restaurantAdapter;
    private List<Restaurant> restaurantList;
    private List<Restaurant> filteredList;
    private FusedLocationProviderClient fusedLocationClient;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        SearchView searchView = root.findViewById(R.id.searchView);
        recyclerView = root.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        restaurantList = new ArrayList<>();
        filteredList = new ArrayList<>();

        // Add dummy data
        restaurantList.add(new Restaurant("Restaurant 1", "9 AM - 9 PM", 4.5, "https://example.com/image1.jpg", 50.68743, 16.37173)); // Example coordinates
        restaurantList.add(new Restaurant("Restaurant 2", "10 AM - 8 PM", 4.0, "https://example.com/image2.jpg", 37.774929, -122.419416)); // Example coordinates
        restaurantList.add(new Restaurant("Restaurant 3", "11 AM - 10 PM", 3.5, "https://example.com/image3.jpg", 34.052235, -118.243683)); // Example coordinates
        filteredList.addAll(restaurantList);

        restaurantAdapter = new RestaurantAdapter(getContext(), filteredList);
        recyclerView.setAdapter(restaurantAdapter);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());

        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);
        } else {
            getUserLocation();
        }

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

    private void getUserLocation() {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                sortRestaurantsByProximity(location);
                            }
                        }
                    });
        }
    }

    private void sortRestaurantsByProximity(Location userLocation) {
        for (Restaurant restaurant : restaurantList) {
            float[] results = new float[1];
            Location.distanceBetween(userLocation.getLatitude(), userLocation.getLongitude(), restaurant.getLatitude(), restaurant.getLongitude(), results);
            restaurant.setDistance(results[0]);
        }

        Collections.sort(restaurantList, new Comparator<Restaurant>() {
            @Override
            public int compare(Restaurant r1, Restaurant r2) {
                return Float.compare(r1.getDistance(), r2.getDistance());
            }
        });

        filteredList.clear();
        filteredList.addAll(restaurantList);
        restaurantAdapter.notifyDataSetChanged();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getUserLocation();
            } else {
                Toast.makeText(getContext(), "Location permission is required to show nearby restaurants", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
