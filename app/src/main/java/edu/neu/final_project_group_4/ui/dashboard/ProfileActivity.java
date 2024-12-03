package edu.neu.final_project_group_4.ui.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Button;
import android.net.Uri;

import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;

import edu.neu.final_project_group_4.utils.User;
import edu.neu.final_project_group_4.R;

public class ProfileActivity extends AppCompatActivity {
    private TextView descriptionText;
    private ImageView profileImage;
    private Button editProfileButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_dashboard);

        // Initialize views
        descriptionText = findViewById(R.id.description_text);
        profileImage = findViewById(R.id.profile_image);
        editProfileButton = findViewById(R.id.edit_profile_button);

        // Fetch and display the user profile information
        updateProfile();

        // Navigate to ProfileEditActivity on clicking Edit button
        editProfileButton.setOnClickListener(view -> {
            Intent intent = new Intent(ProfileActivity.this, ProfileEditActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Refresh the profile information after returning
        updateProfile();
    }

    private void updateProfile() {
        User user = User.getInstance();

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
