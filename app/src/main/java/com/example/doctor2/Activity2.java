package com.example.doctor2;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.TextView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.HashMap;
import java.util.Map;

public class Activity2 extends AppCompatActivity {
    private Button buttonReject;
    private Button buttonAccept;
    TextView textviewName, textviewAge, textviewGender, textviewAlt, textviewSymp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2);

        textviewName = findViewById(R.id.textviewName);
        textviewAge = findViewById(R.id.textviewAge);
        textviewGender = findViewById(R.id.textviewGender);
        textviewAlt = findViewById(R.id.textviewAlt);
        textviewSymp = findViewById(R.id.textviewSymp);

        String name = getIntent().getStringExtra("name");
        String age = getIntent().getStringExtra("age");
        String sex = getIntent().getStringExtra("sex");
        String alt = getIntent().getStringExtra("alt");
        String symp = getIntent().getStringExtra("symp");
        final String key = getIntent().getStringExtra("key");

        textviewName.setText(name);
        textviewAge.setText(age);
        textviewGender.setText(sex);
        textviewAlt.setText(alt);
        textviewSymp.setText(symp);

        buttonReject = findViewById(R.id.RejectBtn);
        buttonAccept = findViewById(R.id.AcceptBtn);

        buttonReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity3(key);
            }
        });

        buttonAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Clear the "Reason" and "Instructions" fields
                clearReasonAndInstructions(key);

                // Handle the "Accept" action by updating the status to "Accepted" in the database
                updateStatus(key, "Accepted");

                // Show a toast message
                Toast.makeText(Activity2.this, "REQUEST ACCEPTED !!!", Toast.LENGTH_SHORT).show();

                // Finish the current activity and return to the previous activity
                finish();
            }
        });
    }

    public void openActivity3(String key) {
        Intent intent = new Intent(Activity2.this, Activity3.class);
        intent.putExtra("key", key);
        startActivity(intent);
    }

    // Function to update the status in the database
    private void updateStatus(String key, String status) {
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference().child("Doctor").child(key);

        // Create a map to update the status
        Map<String, Object> updates = new HashMap<>();
        updates.put("status", status);

        // Update the status in the database
        databaseRef.updateChildren(updates);
    }

    // Function to clear the "Reason" and "Instructions" fields
    private void clearReasonAndInstructions(String key) {
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference().child("Doctor").child(key);

        // Create a map to clear the "Reason" and "Instructions" fields
        Map<String, Object> updates = new HashMap<>();
        updates.put("reason", "");
        updates.put("instruction", "");

        // Update the database to clear the fields
        databaseRef.updateChildren(updates);
    }
}

