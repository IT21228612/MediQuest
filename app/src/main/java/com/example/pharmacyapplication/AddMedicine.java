package com.example.pharmacyapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddMedicine extends AppCompatActivity {

     EditText medicineName, medicineDosage, medicinemanufacturer, medicineQuentity,
            medicinePricePerUnit, medicineMdate, medicineEdate;
     Button medicineAdd, medicineView;
     DatabaseReference databaseMedicine;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_medicine);

        medicineName = findViewById(R.id.medicineName);
        medicineDosage = findViewById(R.id.medicineDosage);
        medicinemanufacturer = findViewById(R.id.medicinemanufacturer);
        medicineQuentity = findViewById(R.id.medicineQuentity);
        medicinePricePerUnit = findViewById(R.id.medicinePricePerUnit);
        medicineMdate = findViewById(R.id.medicineMdate);
        medicineEdate = findViewById(R.id.medicineEdate);
        medicineAdd = findViewById(R.id.medicineAdd);
        medicineView = findViewById(R.id.medicineView);
        databaseMedicine = FirebaseDatabase.getInstance().getReference();


        medicineAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InsertData();
            }
        });

        medicineView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AddMedicine.this, medicineList.class));
            }
        });


    }

    private void InsertData() {
        String mediName = medicineName.getText().toString();
        String mediDosage = medicineDosage.getText().toString();
        String medimanufacturer = medicinemanufacturer.getText().toString();
        String mediQuentity = medicineQuentity.getText().toString();
        String mediPricePerUnit = medicinePricePerUnit.getText().toString();
        String mediMdate = medicineMdate.getText().toString();
        String mediEdate = medicineEdate.getText().toString();
        String id = databaseMedicine.push().getKey();

        Medicine medicine = new Medicine(mediName,mediDosage,medimanufacturer,mediQuentity,mediPricePerUnit,mediMdate,mediEdate);
        databaseMedicine.child("medicine").child(id).setValue(medicine)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(AddMedicine.this, "Medicine Inserted!!", Toast.LENGTH_SHORT).show();
                        }

                    }
                });




    }
}