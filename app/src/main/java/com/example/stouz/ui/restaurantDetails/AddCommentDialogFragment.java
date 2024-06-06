package com.example.stouz.ui.restaurantDetails;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.DialogFragment;

import android.Manifest;
import com.example.stouz.R;
import com.example.stouz.models.Comment;
import com.example.stouz.repositories.CommentRepository;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class AddCommentDialogFragment extends DialogFragment {

    private EditText commentEditText;
    private RatingBar ratingBar;
    private Button submitButton;
    private ImageView commentImageView; // For image upload (optional)
    private static final int REQUEST_CAMERA_PERMISSION = 2; // Unique request code
    private Uri selectedImageUri;
    private String currentPhotoPath;
    private Uri photoURI;
    private CommentRepository commentRepository;
    private String restaurantId;
    private List<Comment> commentList;

    // Interface to communicate with the RestaurantDetailFragment
    public interface AddCommentListener {
        void onCommentAdded(Comment comment);
    }

    private AddCommentListener listener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (getParentFragment() instanceof AddCommentListener) {
            listener = (AddCommentListener) getParentFragment();
        } else if (getActivity() instanceof AddCommentListener) {
            listener = (AddCommentListener) getActivity();
        } else {
            throw new RuntimeException(context.toString() + " must implement AddCommentListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_add_comment, container, false);
        commentRepository = new CommentRepository();

        commentEditText = view.findViewById(R.id.commentEditText);
        ratingBar = view.findViewById(R.id.ratingBar);
        submitButton = view.findViewById(R.id.submitButton);
        commentImageView = view.findViewById(R.id.commentImageView); // Optional
        Bundle args = getArguments();
        if (args != null) {
            restaurantId = args.getString("restaurantId");
            commentList = (List<Comment>) args.getSerializable("commentList");
        }
        Button selectImageButton = view.findViewById(R.id.selectImageButton);
        selectImageButton.setOnClickListener(v -> dispatchTakePictureIntent()); // Call the camera intent

        submitButton.setOnClickListener(v -> {
            String commentText = commentEditText.getText().toString().trim();
            int rating = (int) ratingBar.getRating();

            if (photoURI != null) {
                uploadImageToFirebaseStorage(restaurantId, commentText, rating);
            } else {
                // Create comment without image URL
                Comment newComment = new Comment(UUID.randomUUID().toString(), FirebaseAuth.getInstance().getCurrentUser().getEmail(), commentText, rating, null);
                newComment.setImageUrl("");
                commentRepository.addComment(restaurantId, commentList, newComment); // Use the appropriate addComment method
                listener.onCommentAdded(newComment);
                dismiss();
            }
        });

        return view;
    }

    private void dispatchTakePictureIntent() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted, request it
            ActivityCompat.requestPermissions(requireActivity(),
                    new String[]{Manifest.permission.CAMERA},
                    REQUEST_CAMERA_PERMISSION);
        } else {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(requireActivity().getPackageManager()) != null) {
                File photoFile = null;
                try {
                    photoFile = createImageFile();
                } catch (IOException ex) {
                    Log.e("AddCommentDialog", "Error creating image file: " + ex.getMessage());
                }

                if (photoFile != null) {
                    photoURI = FileProvider.getUriForFile(requireContext(),
                            "com.example.stouz.fileprovider", // Replace with your authority
                            photoFile);
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    cameraActivityResultLauncher.launch(takePictureIntent);
                }
            }
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private final ActivityResultLauncher<Intent> cameraActivityResultLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == requireActivity().RESULT_OK) {
                    commentImageView.setImageURI(photoURI);
                }
            });

    private void uploadImageToFirebaseStorage(String restaurantId, String commentText, int rating) {
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        StorageReference photoRef = storageRef.child("comments/" + photoURI.getLastPathSegment());
        UploadTask uploadTask = photoRef.putFile(photoURI);

        uploadTask.addOnSuccessListener(taskSnapshot -> photoRef.getDownloadUrl().addOnSuccessListener(uri -> {
            String imageUrl = uri.toString();
            Comment newComment = new Comment(UUID.randomUUID().toString(), FirebaseAuth.getInstance().getCurrentUser().getEmail(), commentText, rating, imageUrl);
            newComment.setImageUrl(imageUrl);
            commentRepository.addComment(restaurantId, commentList, newComment);
            listener.onCommentAdded(newComment);
            dismiss();
        })).addOnFailureListener(exception -> {
            Log.e("AddCommentDialog", "Error uploading image: " + exception.getMessage());
            Toast.makeText(requireContext(), "Failed to upload image", Toast.LENGTH_SHORT).show();
        });
    }
}