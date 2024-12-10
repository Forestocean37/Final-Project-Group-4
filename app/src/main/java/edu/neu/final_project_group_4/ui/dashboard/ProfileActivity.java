package edu.neu.final_project_group_4.ui.dashboard;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import edu.neu.final_project_group_4.R;
import edu.neu.final_project_group_4.utils.User;

public class ProfileActivity extends AppCompatActivity {
    private TextView descriptionText;
    private ImageView profileImage;
    private TextView userFullName;
    private Button editProfileButton;

    // Declare ActivityResultLauncher
    private final ActivityResultLauncher<Intent> editProfileLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    // Immediately refresh profile data after returning from ProfileEditActivity
                    refreshProfileData();
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_dashboard);

        // Initialize views
        descriptionText = findViewById(R.id.description_text);
        profileImage = findViewById(R.id.profile_image);
        userFullName = findViewById(R.id.profile_user_full_name);
        editProfileButton = findViewById(R.id.edit_profile_button);

        // Launch ProfileEditActivity when Edit button is clicked
        editProfileButton.setOnClickListener(view -> {
            Intent intent = new Intent(ProfileActivity.this, ProfileEditActivity.class);
            editProfileLauncher.launch(intent); // Use the launcher instead of startActivityForResult
        });

        // Refresh profile data on activity start
        refreshProfileData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Refresh the profile information
        refreshProfileData();
    }

    private void refreshProfileData() {
        User user = User.getInstance();

        // Update full name
        userFullName.setText(user.getFullName());

        // Update description
        user.fetchUserDescription();
        descriptionText.setText(user.getUserDescription());

        // Update profile image
        Uri photoUri = user.getPhotoUrl();
        if (photoUri != null) {
            Glide.with(this).load(photoUri).into(profileImage);
        } else {
            profileImage.setImageResource(R.drawable.profile_image); // Placeholder image
        }
    }
}
