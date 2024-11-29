package edu.neu.final_project_group_4.utils;

import android.net.Uri;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.HashMap;
import java.util.Map;

public class User {
    private FirebaseUser user;
    private FirebaseFirestore db;
    private String userDescription = "Your description is empty...";

    private static class Instance {
        private static final User _instance = new User();
    }

    private User() {
        db = FirebaseFirestore.getInstance();
        fetchCurrentUser();
        fetchUserDescription();
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

    public void fetchUserDescription() {
        if (user != null) {
            db.collection("users").document(user.getUid()).get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            String description = documentSnapshot.getString("description");
                            if (description != null) {
                                userDescription = description;
                            }
                        } else {
                            Log.d("User", "User does not exist");
                        }
                    })
                    .addOnFailureListener(e -> Log.w("User", "Error fetching user description", e));
        }
    }

    public String getUserDescription() {
        return userDescription;
    }

    public void editDescription(String newDescription) {
        if (user != null) {
            db.collection("users").document(user.getUid())
                    .update("description", newDescription)
                    .addOnSuccessListener(unused -> Log.d("User", "User description updated successfully"))
                    .addOnFailureListener(e -> {
                        if (e instanceof FirebaseFirestoreException
                                && ((FirebaseFirestoreException) e).getCode() == FirebaseFirestoreException.Code.NOT_FOUND) {
                            addDescription(newDescription);
                        } else {
                            Log.d("User", "Error updating user description", e);
                        }
                    });
        }
    }

    private void addDescription(String description) {
        if (user != null) {
            Map<String, Object> data = new HashMap<>();
            data.put("description", description);

            db.collection("users").document(user.getUid()).set(data)
                    .addOnSuccessListener(unused -> Log.d("User", "User description added successfully"))
                    .addOnFailureListener(e -> Log.d("User", "Error adding user description", e));
        }
    }
}
