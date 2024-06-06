package com.example.stouz.ui.restaurantDetails;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class AddCommentDialogFragment extends DialogFragment {

    private EditText commentEditText;
    private RatingBar ratingBar;
    private Button submitButton;
    private ImageView commentImageView;
    private static final int REQUEST_CAMERA_PERMISSION = 2;
    private static final int REQUEST_STORAGE_PERMISSION = 3;
    private Uri photoURI;
    private String currentPhotoPath;
    private CommentRepository commentRepository;
    private String restaurantId;
    private ArrayList<Comment> commentList;

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
        commentImageView = view.findViewById(R.id.commentImageView);

        checkPermissions();

        Bundle args = getArguments();
        if (args != null) {
            restaurantId = args.getString("restaurantId");
            commentList = args.getParcelableArrayList("commentList");
        }

        Button selectImageButton = view.findViewById(R.id.selectImageButton);
        selectImageButton.setOnClickListener(v -> showImageSourceDialog());

        submitButton.setOnClickListener(v -> {
            String commentText = commentEditText.getText().toString().trim();
            int rating = (int) ratingBar.getRating();

            if (photoURI != null) {
                uploadImageToFirebaseStorage(restaurantId, commentText, rating);
            } else {
                Comment newComment = new Comment(UUID.randomUUID().toString(), FirebaseAuth.getInstance().getCurrentUser().getEmail(), commentText, rating, null);
                newComment.setImageUrl("");
                commentRepository.addComment(restaurantId, commentList, newComment);
                listener.onCommentAdded(newComment);
                dismiss();
            }
        });

        return view;
    }

    private void checkPermissions() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(requireActivity(),
                    new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_CAMERA_PERMISSION);
        }
    }

    private void showImageSourceDialog() {
        String[] options = {"Camera", "Gallery"};
        new android.app.AlertDialog.Builder(getContext())
                .setTitle("Select Image Source")
                .setItems(options, (dialog, which) -> {
                    if (which == 0) {
                        dispatchTakePictureIntent();
                    } else if (which == 1) {
                        dispatchPickPictureIntent();
                    }
                }).show();
    }

    private void dispatchTakePictureIntent() {
        Log.d("AddCommentDialog", "dispatchTakePictureIntent called");
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            Log.d("AddCommentDialog", "Camera permission not granted, requesting permission");
            ActivityCompat.requestPermissions(requireActivity(),
                    new String[]{Manifest.permission.CAMERA},
                    REQUEST_CAMERA_PERMISSION);
        } else {
            Log.d("AddCommentDialog", "Camera permission granted, checking camera availability");
            if (isCameraAvailable(requireContext())) {
                Log.d("AddCommentDialog", "Camera is available, launching camera intent");
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                // List all activities that can handle this intent
                PackageManager packageManager = requireActivity().getPackageManager();
                List<ResolveInfo> activities = packageManager.queryIntentActivities(takePictureIntent, PackageManager.MATCH_DEFAULT_ONLY);
                Log.d("AddCommentDialog", "Number of activities that can handle camera intent: " + activities.size());
                for (ResolveInfo activity : activities) {
                    Log.d("AddCommentDialog", "Activity: " + activity.activityInfo.name);
                }

                if (takePictureIntent.resolveActivity(packageManager) != null) {
                    Log.d("AddCommentDialog", "Camera intent resolved");
                    File photoFile = null;
                    try {
                        photoFile = createImageFile();
                    } catch (IOException ex) {
                        Log.e("AddCommentDialog", "Error creating image file: " + ex.getMessage());
                    }

                    if (photoFile != null) {
                        Log.d("AddCommentDialog", "Photo file created: " + photoFile.getAbsolutePath());
                        try {
                            photoURI = FileProvider.getUriForFile(requireContext(),
                                    requireContext().getPackageName() + ".fileprovider",
                                    photoFile);
                            Log.d("AddCommentDialog", "FileProvider URI: " + photoURI.toString());
                            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                            cameraActivityResultLauncher.launch(takePictureIntent);
                        } catch (IllegalArgumentException e) {
                            Log.e("AddCommentDialog", "FileProvider URI error: " + e.getMessage());
                        }
                    } else {
                        Log.e("AddCommentDialog", "Photo file is null");
                    }
                } else {
                    Log.e("AddCommentDialog", "Camera intent not resolved");
                }
            } else {
                Log.e("AddCommentDialog", "No camera available on this device");
                Toast.makeText(requireContext(), "No camera available on this device", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void dispatchPickPictureIntent() {
        Intent pickPictureIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickPictureIntent.setType("image/*");
        galleryActivityResultLauncher.launch(pickPictureIntent);
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
                if (result.getResultCode() == Activity.RESULT_OK) {
                    commentImageView.setImageURI(photoURI);
                }
            });

    private final ActivityResultLauncher<Intent> galleryActivityResultLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    Uri selectedImageUri = result.getData().getData();
                    if (selectedImageUri != null) {
                        photoURI = selectedImageUri;
                        commentImageView.setImageURI(photoURI);
                    }
                }
            });

    private void uploadImageToFirebaseStorage(String restaurantId, String commentText, int rating) {
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        StorageReference photoRef = storageRef.child("comments/" + UUID.randomUUID().toString() + ".jpg");
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                dispatchTakePictureIntent();
            } else {
                Toast.makeText(requireContext(), "Camera permission is required to take pictures", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean isCameraAvailable(Context context) {
        PackageManager packageManager = context.getPackageManager();
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        return intent.resolveActivity(packageManager) != null;
    }

}
