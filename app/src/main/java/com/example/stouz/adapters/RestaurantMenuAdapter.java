package com.example.stouz.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.stouz.R;
import com.example.stouz.models.DishCategory;
import com.google.android.material.card.MaterialCardView;
import java.util.List;

public class RestaurantMenuAdapter extends RecyclerView.Adapter<RestaurantMenuAdapter.CategoryViewHolder> {

    private Context context;
    private List<DishCategory> categories;

    public RestaurantMenuAdapter(Context context, List<DishCategory> categories) {
        this.context = context;
        this.categories = categories;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.category_item, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        DishCategory category = categories.get(position);

        holder.categoryNameTextView.setText(category.getName());

        holder.dishesRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        DishAdapter dishAdapter = new DishAdapter(context, category.getDishes());
        holder.dishesRecyclerView.setAdapter(dishAdapter);

        Glide.with(context)
                .load(category.getImageUrl())
                .into(holder.categoryImageView);

        holder.cardView.setOnClickListener(v ->
                holder.dishesRecyclerView.setVisibility(
                        holder.dishesRecyclerView.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE
                )
        );
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    static class CategoryViewHolder extends RecyclerView.ViewHolder {
        MaterialCardView cardView;
        TextView categoryNameTextView;
        RecyclerView dishesRecyclerView;
        ImageView categoryImageView;


        CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.categoryCardView);
            categoryNameTextView = itemView.findViewById(R.id.categoryNameTextView);
            dishesRecyclerView = itemView.findViewById(R.id.dishesRecyclerView);
            categoryImageView = itemView.findViewById(R.id.categoryImageView); // Initialize it
        }
    }
}