package com.example.doctor2;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.HashMap;
import java.util.Map;

public class Activity3 extends AppCompatActivity {

    Button btnToast;
    Button rejCancel;
    EditText rejectionReason, doctorInstruction;
    DatabaseReference databaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_3);

        // Initialize UI elements
        rejectionReason = findViewById(R.id.rejectionReason);
        doctorInstruction = findViewById(R.id.doctorInstruction);

        // Get the "key" from the intent
        String key = getIntent().getStringExtra("key");

        // Check if the key is valid
        if (key != null && !key.isEmpty()) {
            // Form the database reference using the key
            databaseRef = FirebaseDatabase.getInstance().getReference().child("Doctor").child(key);
        } else {
            // Handle the case where the key is missing
            Toast.makeText(Activity3.this, "Key is missing.", Toast.LENGTH_SHORT).show();
            // You might want to return to the previous activity or handle this case accordingly.
        }

        // Initialize and set click listeners for buttons
        btnToast = findViewById(R.id.rejectedbtn);
        btnToast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check for form validation
                if (validateForm()) {
                    // Update the database data and set the status to "Rejected"
                    updateDocData("Rejected");
                    // Show a toast message
                    Toast.makeText(Activity3.this, "PATIENT REQUEST REJECTED !!!", Toast.LENGTH_SHORT).show();
                    // Navigate to another activity (e.g., ViewAllRequests)
                    Intent intent = new Intent(Activity3.this, ViewAllRequests.class);
                    startActivity(intent);
                }
            }
        });

        rejCancel = findViewById(R.id.rejectionCancelbtn);
        rejCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to another activity (e.g., ViewAllRequests)
                Intent intent2 = new Intent(Activity3.this, ViewAllRequests.class);
                startActivity(intent2);
            }
        });
    }

    private void updateDocData(String status) {
        // Get data from EditText fields
        String reason = rejectionReason.getText().toString().trim();
        String instruction = doctorInstruction.getText().toString().trim();

        // Create a map to update data in the database
        Map<String, Object> updates = new HashMap<>();
        updates.put("reason", reason);
        updates.put("instruction", instruction);
        updates.put("status", status); // Set the status to "Rejected"

        // Check if the database reference is valid and update the data
        if (databaseRef != null) {
            databaseRef.updateChildren(updates);
        } else {
            Toast.makeText(Activity3.this, "Database reference is missing.", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean validateForm() {
        String reason = rejectionReason.getText().toString().trim();
        String instruction = doctorInstruction.getText().toString().trim();

        if (reason.isEmpty() || instruction.isEmpty()) {
            // Display a custom error message if any field is empty
            if (reason.isEmpty()) {
                Toast.makeText(Activity3.this, "Please provide a reason for rejection.", Toast.LENGTH_SHORT).show();
            }
            if (instruction.isEmpty()) {
                Toast.makeText(Activity3.this, "Please provide doctor's instructions.", Toast.LENGTH_SHORT).show();
            }
            return false;
        }

        return true;
    }
}
