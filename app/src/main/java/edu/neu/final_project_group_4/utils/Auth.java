package edu.neu.final_project_group_4.utils;

import android.content.Context;
import android.content.Intent;

import com.firebase.ui.auth.AuthUI;

import edu.neu.final_project_group_4.LoginActivity;

public class Auth {
    public static void signOut(Context context) {
        AuthUI.getInstance()
                .signOut(context)
                .addOnCompleteListener(task -> {
                    User.getInstance().fetchCurrentUser();
                    Intent intent = new Intent(context, LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                });
    }
}
