package com.example.doctor2;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class GenerateReportActivity extends AppCompatActivity {

    private Spinner nameSpinner;
    private Button generateReportButton;
    private WebView reportTextView;

    private DatabaseReference databaseReference;

    // Map to store names and corresponding keys
    private Map<String, String> nameToKeyMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_report);

        nameSpinner = findViewById(R.id.nameSpinner);
        generateReportButton = findViewById(R.id.generateReportButton);
        reportTextView = findViewById(R.id.reportWebView);

        // Initialize the Firebase database reference
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Doctor");

        // Create an array to store names (use ArrayAdapter)
        final ArrayAdapter<String> namesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item);
        namesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Add the default value "Select Patient Name"
        namesAdapter.add("Select Patient Name");

        nameSpinner.setAdapter(namesAdapter);

        // Fetch names from Firebase
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                namesAdapter.clear();
                nameToKeyMap.clear();

                // Add the default value "Select Patient Name"
                namesAdapter.add("Select Patient's Name");

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    // Assuming each child key is a unique identifier (key) for a person
                    String personKey = snapshot.getKey();
                    String name = snapshot.child("name").getValue(String.class);
                    namesAdapter.add(name);

                    // Store the key for each person in the map
                    nameToKeyMap.put(name, personKey);
                }

                namesAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors here
            }
        });

        // ...
        generateReportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get the selected name from the Spinner
                String selectedName = nameSpinner.getSelectedItem().toString();
                // Check if the user selected the default value
                if (selectedName.equals("Select Patient's Name")) {
                    Toast.makeText(GenerateReportActivity.this, "Please Select the Patient Name !!!", Toast.LENGTH_SHORT).show();
                } else {
                    // Get the key for the selected person from the map
                    String personKey = nameToKeyMap.get(selectedName);

                    // Fetch person's details from the database using personKey
                    databaseReference.child(personKey).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            // Modify this section to retrieve the specific fields you need
                            String name = dataSnapshot.child("name").getValue(String.class);
                            String age = dataSnapshot.child("age").getValue(String.class);
                            String gender = dataSnapshot.child("sex").getValue(String.class);
                            String alt = dataSnapshot.child("alt").getValue(String.class);
                            String symp = dataSnapshot.child("symp").getValue(String.class);

                            // Add fields for Reason, Instruction, and Status
                            String reason = dataSnapshot.child("reason").getValue(String.class);
                            String instruction = dataSnapshot.child("instruction").getValue(String.class);
                            String status = dataSnapshot.child("status").getValue(String.class);

                            // Generate the report as HTML
                            String reportHtml = "<html><body>" +
                                    "<h1 style='text-align:center;'>PATIENT REPORT</h1>" +
                                    "<p><strong>Name:</strong> " + name + "</p>" +
                                    "<p><strong>Age:</strong> " + age + "</p>" +
                                    "<p><strong>Gender:</strong> " + gender + "</p>" +
                                    "<p><strong>Alternative:</strong> " + alt + "</p>" +
                                    "<p><strong>Symptoms:</strong> " + symp + "</p>" +
                                    "<p><strong>Reason for Rejection:</strong> " + reason + "</p>" +
                                    "<p><strong>Dr Instruction:</strong> " + instruction + "</p>" +
                                    "<p><strong>Status:</strong> " + status + "</p>" +
                                    "</body></html>";

                            // Load the report into the WebView
                            reportTextView.loadData(reportHtml, "text/html", "UTF-8");
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            // Handle errors here
                        }
                    });
                }
            }
        });
        // ...
    }
}
