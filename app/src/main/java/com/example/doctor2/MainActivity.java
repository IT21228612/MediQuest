package com.example.doctor2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    EditText etName, etAge, etAlt, etsymp;
    Spinner spinnersex;
    Button btnInsertData;

    DatabaseReference DoctorDbRef;

    Button preq;

    Button btnDelete;

    Button viewAll;

    Button reportGen;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etName = findViewById(R.id.etName);
        etAge = findViewById(R.id.etAge);
        etAlt = findViewById(R.id.etAlt);
        etsymp = findViewById(R.id.etsymp);
        spinnersex = findViewById(R.id.spinnersex);
        btnInsertData = findViewById(R.id.btnInsertData);


        DoctorDbRef = FirebaseDatabase.getInstance().getReference().child("Doctor");

        viewAll = (Button) findViewById(R.id.viewAll);
        viewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent3 = new Intent(MainActivity.this,ViewAllRequests.class);
                startActivity(intent3);
            }
        });




        btnInsertData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertDocData();
            }
        });

        //report
        reportGen = (Button) findViewById(R.id.reportGen);
        reportGen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent4 = new Intent(MainActivity.this,GenerateReportActivity.class);
                startActivity(intent4);
            }
        });



    }





    private void insertDocData() {
        String name = etName.getText().toString().trim();
        String Age = etAge.getText().toString().trim();
        String Alt = etAlt.getText().toString().trim();
        String symp = etsymp.getText().toString().trim();
        String sex = spinnersex.getSelectedItem().toString();

        if (name.isEmpty()) {
            etName.setError("Name is required");
            etName.requestFocus();
            return;
        }

        if (Age.isEmpty()) {
            etAge.setError("Age is required");
            etAge.requestFocus();
            return;
        }

        int age;
        try {
            age = Integer.parseInt(Age);
        } catch (NumberFormatException e) {
            etAge.setError("Age must be a valid number");
            etAge.requestFocus();
            return;
        }

        if (sex.equals("Select Gender")) {
            Toast.makeText(this, "Please select a gender", Toast.LENGTH_SHORT).show();
            return;
        }

        if (Alt.isEmpty()) {
            etAlt.setError("Alternatives are required");
            etAlt.requestFocus();
            return;
        }

        if (symp.isEmpty()) {
            etsymp.setError("Symptoms are required");
            etsymp.requestFocus();
            return;
        }

        Doctor doctor = new Doctor(name, Age, Alt, symp, sex,"","","Pending");
        DoctorDbRef.push().setValue(doctor);

        // Clear the form fields
        etName.setText("");
        etAge.setText("");
        etAlt.setText("");
        etsymp.setText("");
        spinnersex.setSelection(0);

        Toast.makeText(this, "Data inserted", Toast.LENGTH_SHORT).show();
    }

}

