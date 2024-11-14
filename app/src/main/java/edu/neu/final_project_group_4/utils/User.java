package edu.neu.final_project_group_4.utils;

import android.net.Uri;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class User {
    private FirebaseUser user;

    private static class Instance {
        private static final User _instance = new User();
    }

    private User() {
        fetchCurrentUser();
    }

    public static User getInstance() {
        return Instance._instance;
    }

    public void fetchCurrentUser() {
        user = FirebaseAuth.getInstance().getCurrentUser();
    }

    public String getUserId() {
        return user != null ? user.getUid() : "";
    }

    public String getFullName() {
        return user != null ? user.getDisplayName() : "NULL";
    }

    public String getEmail() {
        if (user != null && user.getEmail() != null) {
            return user.getEmail();
        } else {
            return "NULL";
        }
    }

    public Uri getPhotoUrl() {
        return user.getPhotoUrl();
    }

    public void updateFullName(String name) {
        UserProfileChangeRequest update = new UserProfileChangeRequest.Builder()
                .setDisplayName(name)
                .build();

        user.updateProfile(update)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d("User", "Successfully updated full name");
                    }
                });
    }

    public void updateProfilePhoto(String photoUri) {
        UserProfileChangeRequest update = new UserProfileChangeRequest.Builder()
                .setPhotoUri(Uri.parse(photoUri))
                .build();

        user.updateProfile(update)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d("User", "Successfully updated profile photo");
                    }
                });
    }
}
