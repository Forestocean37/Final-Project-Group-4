package edu.neu.final_project_group_4.ui.dashboard;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;

import edu.neu.final_project_group_4.R;

public class ProfileEditActivity extends AppCompatActivity {
    private EditText descriptionBox;
    private Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_edit);

        // Initialize views
        descriptionBox = findViewById(R.id.description_box);
        saveButton = findViewById(R.id.save_button);

        // Load saved description (if any)
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String savedDescription = sharedPreferences.getString("description", "");
        descriptionBox.setText(savedDescription);

        // Save description when "Save" button is clicked
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String description = descriptionBox.getText().toString();

                // Save to SharedPreferences
                SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("description", description);
                editor.apply();

                finish();
            }
        });

    }
}

