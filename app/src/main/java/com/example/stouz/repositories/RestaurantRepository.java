package com.example.stouz.repositories;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.stouz.models.Restaurant;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class RestaurantRepository {
    private static final String TAG = "RestaurantRepository";
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance("DATABASE_NAME.europe-west1.firebasedatabase.app").getReference();
    private ValueEventListener eventListener;
    private Restaurant restaurant;

    public List<Restaurant> getListRestaurant(){
        List<Restaurant> listRestaurant = new ArrayList<>();
        eventListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listRestaurant.clear();
                for (DataSnapshot itemSnapshot: snapshot.getChildren()){
                    Restaurant restaurantClass = itemSnapshot.getValue(Restaurant.class);
                    restaurantClass.setId(itemSnapshot.getKey());
                    listRestaurant.add(restaurantClass);
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w(TAG, "loadPost:onCancelled", error.toException());
            }
        });
        return listRestaurant;
    }

    public Restaurant getRestaurant(){
        eventListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                restaurant = snapshot.getValue(Restaurant.class);

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w(TAG, "loadPost:onCancelled", error.toException());
            }
        });
        return restaurant;
    }
}
