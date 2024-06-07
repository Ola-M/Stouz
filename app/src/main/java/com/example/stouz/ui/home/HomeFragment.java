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
import com.example.stouz.adapters.RestaurantAdapter;
import com.example.stouz.models.Restaurant;
import com.example.stouz.repositories.RestaurantRepository;
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
    public List<Restaurant> restaurantList;
    public List<Restaurant> filteredList;
    public FusedLocationProviderClient fusedLocationClient;
    private static final double ZIELONA_GORA_LATITUDE = 51.9355;
    private static final double ZIELONA_GORA_LONGITUDE = 15.5062;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        SearchView searchView = root.findViewById(R.id.searchView);
        recyclerView = root.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        restaurantList = new ArrayList<>();
        filteredList = new ArrayList<>();

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

    public void filter(String text) {
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
                    .addOnSuccessListener(location -> {
                        if (location != null) {
                            fetchRestaurants(location);
                        } else {
                            Location zielonaGoraLocation = new Location("provider");
                            zielonaGoraLocation.setLatitude(ZIELONA_GORA_LATITUDE);
                            zielonaGoraLocation.setLongitude(ZIELONA_GORA_LONGITUDE);
                            fetchRestaurants(zielonaGoraLocation);
                        }
                    })
                    .addOnFailureListener(e -> {
                        Location zielonaGoraLocation = new Location("provider");
                        zielonaGoraLocation.setLatitude(ZIELONA_GORA_LATITUDE);
                        zielonaGoraLocation.setLongitude(ZIELONA_GORA_LONGITUDE);
                        fetchRestaurants(zielonaGoraLocation);
                    });
        } else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);
        }
    }


    public void fetchRestaurants(Location userLocation) {
        new RestaurantRepository().getRestaurantsList(new RestaurantRepository.DataStatus() {
            @Override
            public void DataIsLoaded(List<Restaurant> restaurants) {
                restaurantList.clear();
                restaurantList.addAll(restaurants);
                sortRestaurantsByProximity(userLocation);
                restaurantAdapter.notifyDataSetChanged();
            }
        });
    }

    public void sortRestaurantsByProximity(Location userLocation) {
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
