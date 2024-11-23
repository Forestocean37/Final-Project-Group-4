package edu.neu.final_project_group_4.ui.dashboard;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import edu.neu.final_project_group_4.R;

public class ProfileActivity extends AppCompatActivity {
    private TextView descriptionText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_dashboard);

        // Initialize views
        descriptionText = findViewById(R.id.description_text);

        // Load saved description
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String savedDescription = sharedPreferences.getString("description", "No description provided.");
        descriptionText.setText(savedDescription);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Update the description when returning to this page
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String savedDescription = sharedPreferences.getString("description", "No description provided.");
        descriptionText.setText(savedDescription);
    }

}
