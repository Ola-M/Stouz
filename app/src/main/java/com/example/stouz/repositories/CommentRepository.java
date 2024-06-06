package com.example.stouz.repositories;

import com.example.stouz.models.Comment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CommentRepository {
    private static final String TAG = "CommentRepository";
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance("https://stouz-d25dc-default-rtdb.europe-west1.firebasedatabase.app").getReference();
    private FirebaseAuth mAuth;


    public void addComment(String restaurantId, List<Comment> comments, Comment comment){
        comments.add(comment);
        databaseReference.child("restaurants").child(restaurantId).child("commentList").setValue(comments);
    }

    public void addComment(String restaurantId, Comment comment){
        databaseReference.child("restaurants").child(restaurantId).child("commentList").setValue(comment);
    }
}
