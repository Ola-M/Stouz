package com.example.stouz.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.stouz.R;
import com.example.stouz.models.Comment;

import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {

    private Context context;
    private List<Comment> commentList;

    public CommentAdapter(Context context, List<Comment> commentList) {
        this.context = context;
        this.commentList = commentList;
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.comment_item, parent, false);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        Comment comment = commentList.get(position);

        holder.commentUserTextView.setText(comment.getUser());
        holder.commentDescriptionTextView.setText(comment.getDescription());
        holder.commentRatingBar.setRating(comment.getRate());

        String imageUrl = comment.getImageUrl();
        if (imageUrl != null && !imageUrl.isEmpty()) {
            holder.commentImageView.setVisibility(View.VISIBLE);
            Glide.with(context)
                    .load(imageUrl)
                    .into(holder.commentImageView);
        } else {
            holder.commentImageView.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return commentList == null ? 0 : commentList.size();
    }

    static class CommentViewHolder extends RecyclerView.ViewHolder {
        TextView commentUserTextView;
        TextView commentDescriptionTextView;
        RatingBar commentRatingBar;
        ImageView commentImageView;

        CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            commentUserTextView = itemView.findViewById(R.id.commentUserTextView);
            commentDescriptionTextView = itemView.findViewById(R.id.commentDescriptionTextView);
            commentRatingBar = itemView.findViewById(R.id.commentRatingBar);
            commentImageView = itemView.findViewById(R.id.commentImageView);
        }
    }
}