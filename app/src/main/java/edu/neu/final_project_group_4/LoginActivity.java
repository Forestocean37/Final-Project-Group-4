package edu.neu.final_project_group_4;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult;
import com.google.firebase.auth.ActionCodeSettings;

import java.util.Arrays;
import java.util.List;

import edu.neu.final_project_group_4.utils.User;

public class LoginActivity extends AppCompatActivity {
    private final ActivityResultLauncher<Intent> signInLauncher = registerForActivityResult(
            new FirebaseAuthUIActivityResultContract(),
            this::onSignInResult
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        String link = catchSignInLink();
        startSignInFlow(link);
    }

    private void startSignInFlow(String link) {
        ActionCodeSettings actionCodeSettings = ActionCodeSettings.newBuilder()
                .setAndroidPackageName(
                        "edu.neu.final_project_group_4",
                        true,
                        "28")
                .setHandleCodeInApp(true)
                .setUrl("https://finalprojectgroup4.page.link/sign-up-sign-in")
                .build();

        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder()
                        .enableEmailLinkSignIn()
                        .setActionCodeSettings(actionCodeSettings)
                        .build()
        );

        AuthUI.SignInIntentBuilder builder = AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .setLogo(R.drawable.ic_logo)
                .setTheme(R.style.CustomAuthUITheme);

        if (link != null) {
            builder.setEmailLink(link);
        }

        signInLauncher.launch(builder.build());
    }

    private String catchSignInLink() {
        if (AuthUI.canHandleIntent(getIntent())) {
            Uri link = getIntent().getData();
            if (link != null) {
                Toast.makeText(LoginActivity.this, "Signing in with email link",
                        Toast.LENGTH_SHORT).show();
                Log.d("Login", "Email Link: " + link);
                return link.toString();
            }
        }
        return null;
    }

    private void onSignInResult(FirebaseAuthUIAuthenticationResult result) {
        IdpResponse response = result.getIdpResponse();
        if (result.getResultCode() == RESULT_OK) {
            // Signed in
            User.getInstance().fetchCurrentUser();
            goToMainActivity();
        } else {
            // Back button
            goToWelcomeActivity();
        }
    }

    private void goToMainActivity() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void goToWelcomeActivity() {
        Intent intent = new Intent(LoginActivity.this, WelcomeActivity.class);
        startActivity(intent);
        finish();
    }
}