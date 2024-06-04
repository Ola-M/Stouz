package com.example.stouz.repositories;

import android.util.Log;
import androidx.annotation.NonNull;
import com.example.stouz.models.Comment;
import com.example.stouz.models.Restaurant;
import com.example.stouz.models.RestaurantMenu;
import com.google.common.reflect.TypeToken;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class RestaurantRepository {
    private static final String TAG = "RestaurantRepository";
    private DatabaseReference databaseReference;

    public RestaurantRepository() {
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://stouz-d25dc-default-rtdb.europe-west1.firebasedatabase.app");
        databaseReference = database.getReference().child("restaurants");
    }

    public void getRestaurantsList(final DataStatus dataStatus) {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Restaurant> restaurantList = new ArrayList<>();
                Gson gson = new Gson();

                for (DataSnapshot itemSnapshot : snapshot.getChildren()) {
                    String restaurantJson = gson.toJson(itemSnapshot.getValue());

                    Type restaurantType = new TypeToken<Restaurant>() {}.getType();
                    Restaurant restaurant = gson.fromJson(restaurantJson, restaurantType);

                    if (restaurant != null) {
                        restaurant.setId(itemSnapshot.getKey());
                        restaurantList.add(restaurant);
                    }
                }

                dataStatus.DataIsLoaded(restaurantList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w(TAG, "loadPost:onCancelled", error.toException());
            }
        });
    }

    public interface DataStatus {
        void DataIsLoaded(List<Restaurant> restaurants);
    }
}
