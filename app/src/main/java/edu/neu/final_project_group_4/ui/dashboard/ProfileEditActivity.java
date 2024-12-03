package edu.neu.final_project_group_4.ui.dashboard;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import edu.neu.final_project_group_4.LoginActivity;
import edu.neu.final_project_group_4.MainActivity;
import edu.neu.final_project_group_4.R;
import edu.neu.final_project_group_4.utils.User;

public class ProfileEditActivity extends AppCompatActivity {
    private EditText userFullName;
    private EditText descriptionBox;
    private ImageView profileImage;
    private Button saveButton;
    private Button cancelButton;
    private Button uploadImageButton;
    private Uri selectedImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_edit);

        // Initialize views
        userFullName = findViewById(R.id.name_box);
        descriptionBox = findViewById(R.id.description_box);
        profileImage = findViewById(R.id.profile_picture);
        saveButton = findViewById(R.id.save_button);
        cancelButton = findViewById(R.id.cancel_button);
        uploadImageButton = findViewById(R.id.upload_picture_button);

        User user = User.getInstance();
        // Load current description and profile image
        userFullName.setText(user.getFullName());
        descriptionBox.setText(user.getUserDescription());
        Uri photoUri = user.getPhotoUrl();
        if (photoUri != null) {
            Glide.with(this).load(photoUri).into(profileImage);
        } else {
            profileImage.setImageResource(R.drawable.profile_image);
        }

        // Handle image selection
        ActivityResultLauncher<Intent> imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        selectedImageUri = result.getData().getData();
                        if (selectedImageUri != null) {
                            profileImage.setImageURI(selectedImageUri);
                            Log.d("ProfileEdit", "Selected Photo: " + selectedImageUri.toString());
                        }
                    }
                });

        uploadImageButton.setOnClickListener(view -> {
            // Create an intent to open the file chooser for images
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*"); // Allow only image files to be selected

            // Launch the image picker activity
            imagePickerLauncher.launch(intent);
        });

        // Save profile changes
        saveButton.setOnClickListener(view -> {
            String newFullName = userFullName.getText().toString();
            user.updateFullName(newFullName);

            String newDescription = descriptionBox.getText().toString();
            user.editDescription(newDescription);

            if (selectedImageUri != null) {
                user.updateProfilePhoto(selectedImageUri);
                Log.d("ProfileEdit", "Photo Url: " + selectedImageUri.toString());
            }

            Toast.makeText(this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
            whenFinish();
        });

        // Cancel without saving
        cancelButton.setOnClickListener(view -> finish());
    }

    private void whenFinish() {
        User.getInstance().fetchUserDescription();
        Intent intent = new Intent(ProfileEditActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
