package com.example.stouz.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.stouz.R;
import com.example.stouz.models.Dish;
import java.util.List;

public class DishAdapter extends RecyclerView.Adapter<DishAdapter.DishViewHolder> {
    private Context context;
    private List<Dish> dishList;

    public DishAdapter(Context context, List<Dish> dishList) {
        this.context = context;
        this.dishList = dishList;
    }

    @NonNull
    @Override
    public DishViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.dish_item, parent, false);
        return new DishViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DishViewHolder holder, int position) {
        Dish dish = dishList.get(position);

        holder.dishNameTextView.setText(dish.getName());
        holder.dishDescriptionTextView.setText(dish.getDescription());
        holder.dishPriceTextView.setText(String.format("%.2f", dish.getPrice())); // Format price

        // Load image using Glide (if available)
        Glide.with(context)
                .load(dish.getImageUrl())
                .into(holder.dishImageView);
    }

    @Override
    public int getItemCount() {
        return dishList.size();
    }

    static class DishViewHolder extends RecyclerView.ViewHolder {
        TextView dishNameTextView;
        TextView dishDescriptionTextView;
        TextView dishPriceTextView;
        ImageView dishImageView;

        DishViewHolder(@NonNull View itemView) {
            super(itemView);
            dishNameTextView = itemView.findViewById(R.id.dishNameTextView);
            dishDescriptionTextView = itemView.findViewById(R.id.dishDescriptionTextView);
            dishPriceTextView = itemView.findViewById(R.id.dishPriceTextView);
            dishImageView = itemView.findViewById(R.id.dishImageView);
        }
    }
}