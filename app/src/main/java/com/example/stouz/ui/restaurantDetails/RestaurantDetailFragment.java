package com.example.stouz.ui.restaurantDetails;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.stouz.R;
import com.example.stouz.Restaurant;
import com.example.stouz.RestaurantMenuItem;
import com.example.stouz.RestaurantMenuAdapter;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import java.util.ArrayList;
import java.util.List;

public class RestaurantDetailFragment extends Fragment implements OnMapReadyCallback {

    private static final String TAG = "RestaurantDetailFragment";

    private ImageView imageView;
    private TextView textViewName, textViewHours;
    private RatingBar ratingBar;
    private RecyclerView menuRecyclerView;
    private RestaurantMenuAdapter menuAdapter;
    private List<RestaurantMenuItem> menuList;
    private GoogleMap mMap;
    private LatLng restaurantLocation;
    private AdView adView;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_restaurant_detail, container, false);

        adView = root.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

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

                // Set the restaurant location
                restaurantLocation = new LatLng(restaurant.getLatitude(), restaurant.getLongitude());
                Log.d(TAG, "Restaurant Location: " + restaurantLocation.latitude + ", " + restaurantLocation.longitude);
            }
        }

        menuList = new ArrayList<>();
        // Add dummy data for menu items
        menuList.add(new RestaurantMenuItem("Dish 1", "Description 1", "https://example.com/dish1.jpg"));
        menuList.add(new RestaurantMenuItem("Dish 2", "Description 2", "https://example.com/dish2.jpg"));
        menuList.add(new RestaurantMenuItem("Dish 3", "Description 3", "https://example.com/dish3.jpg"));

        menuAdapter = new RestaurantMenuAdapter(getContext(), menuList);
        menuRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        menuRecyclerView.setAdapter(menuAdapter);

        // Initialize the map
        SupportMapFragment mapFragment = new SupportMapFragment();
        FragmentManager fragmentManager = getChildFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.map_container, mapFragment).commit();
        mapFragment.getMapAsync(this);

        return root;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // Add a marker at the restaurant location and move the camera
        if (restaurantLocation != null) {
            mMap.addMarker(new MarkerOptions().position(restaurantLocation).title("Restaurant Location"));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(restaurantLocation, 15));
            Log.d(TAG, "Marker added at: " + restaurantLocation.latitude + ", " + restaurantLocation.longitude);
        } else {
            // Add a default marker (e.g., for New York City)
            LatLng defaultLocation = new LatLng(40.7128, -74.0060);
            mMap.addMarker(new MarkerOptions().position(defaultLocation).title("Default Location"));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, 15));
            Log.d(TAG, "Default marker added at: " + defaultLocation.latitude + ", " + defaultLocation.longitude);
        }
    }

    @Override
    public void onDestroyView() {
        if (adView != null) {
            adView.destroy();
        }
        super.onDestroyView();
    }
}
