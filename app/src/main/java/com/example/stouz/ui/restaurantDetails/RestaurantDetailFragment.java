package com.example.stouz.ui.restaurantDetails;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.example.stouz.adapters.CommentAdapter;
import com.example.stouz.models.Comment;
import com.example.stouz.models.Dish;
import com.example.stouz.models.DishCategory;
import com.example.stouz.models.Restaurant;
import com.example.stouz.models.RestaurantMenu;
import com.example.stouz.adapters.RestaurantMenuAdapter;
import com.example.stouz.repositories.RestaurantRepository;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class RestaurantDetailFragment extends Fragment implements OnMapReadyCallback {

    private static final String TAG = "RestaurantDetailFragment";

    private ImageView imageView;
    private TextView textViewName, textViewHours;
    private RatingBar ratingBar;
    private RecyclerView menuRecyclerView;
    private RestaurantMenuAdapter menuAdapter;
    private RecyclerView commentsRecyclerView;

    private GoogleMap mMap;
    private LatLng restaurantLocation;
    private AdView adView;
    private Restaurant restaurant;
    private Button addCommentButton;
    private RestaurantRepository restaurantRepository;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_restaurant_detail, container, false);

        adView = root.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
        restaurantRepository = new RestaurantRepository();
        imageView = root.findViewById(R.id.imageView);
        textViewName = root.findViewById(R.id.textViewName);
        textViewHours = root.findViewById(R.id.textViewHours);
        ratingBar = root.findViewById(R.id.ratingBar);
        menuRecyclerView = root.findViewById(R.id.menuRecyclerView);
        commentsRecyclerView = root.findViewById(R.id.commentsRecyclerView);
        addCommentButton = root.findViewById(R.id.addCommentButton);
        addCommentButton.setOnClickListener(v -> showAddCommentDialog());
        // Get the restaurant data from arguments
        Bundle args = getArguments();
        if (args != null) {
            restaurant = (Restaurant) args.getParcelable("restaurant");
            if (restaurant != null) {
                textViewName.setText(restaurant.getName());
                textViewHours.setText(getString(R.string.opening_hours, restaurant.getOpeningHours()));
                ratingBar.setRating((float) restaurant.getAvgRating());

                Glide.with(getContext())
                        .load(restaurant.getImageUrl())
                        .into(imageView);

                // Set the restaurant location
                restaurantLocation = new LatLng(restaurant.getLatitude(), restaurant.getLongitude());
                Log.d(TAG, "Restaurant Location: " + restaurantLocation.latitude + ", " + restaurantLocation.longitude);
                restaurantRepository.increaseViewsCount(restaurant);

            }
        }


        RestaurantMenu restaurantMenu = restaurant.getMenu();
        List<DishCategory> cagories = restaurantMenu.getCategories();

        menuAdapter = new RestaurantMenuAdapter(getContext(), cagories);
        menuRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        menuRecyclerView.setAdapter(menuAdapter);

        List<Comment> commentList = restaurant.getCommentList();

        CommentAdapter commentAdapter = new CommentAdapter(getContext(), commentList);
        commentsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        commentsRecyclerView.setAdapter(commentAdapter);

        SupportMapFragment mapFragment = new SupportMapFragment();
        FragmentManager fragmentManager = getChildFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.map_container, mapFragment).commit();
        mapFragment.getMapAsync(this);

        return root;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (restaurantLocation != null) {
            mMap.addMarker(new MarkerOptions().position(restaurantLocation).title("Restaurant Location"));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(restaurantLocation, 15));
            Log.d(TAG, "Marker added at: " + restaurantLocation.latitude + ", " + restaurantLocation.longitude);
        } else {
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

    private void showAddCommentDialog() {
        AddCommentDialogFragment dialog = new AddCommentDialogFragment();
        Bundle args = new Bundle();
        args.putString("restaurantId", restaurant.getId());
        args.putSerializable("commentList", (Serializable) restaurant.getCommentList());

        dialog.setArguments(args);

        dialog.show(getChildFragmentManager(), "AddCommentDialog");
    }
}
