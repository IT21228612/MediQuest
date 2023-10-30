package com.example.pharmacyapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.ktx.Firebase;

public class RegisterPharmacist extends AppCompatActivity {

    private FirebaseAuth auth;
    private EditText pharmacistName, pharmacistUsername, pharmacistEmail,
            pharmacistAddress, pharmacistQualification, pharmacistLicense, pharmacistPassword;
    private Button registerPharmacist;
    private TextView RegisterRedirectText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_pharmacist);

        auth = FirebaseAuth.getInstance();

        pharmacistName = findViewById(R.id.pharmacistName);
        pharmacistUsername = findViewById(R.id.pharmacistUsername);
        pharmacistEmail = findViewById(R.id.pharmacistEmail);
        pharmacistAddress = findViewById(R.id.pharmacistAddress);
        pharmacistQualification = findViewById(R.id.pharmacistQualification);
        pharmacistLicense = findViewById(R.id.pharmacistLicense);
        pharmacistPassword = findViewById(R.id.pharmacistPassword);
        registerPharmacist = findViewById(R.id.registerPharmacist);
        RegisterRedirectText = findViewById(R.id.RegisterRedirectText);

        registerPharmacist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Pname = pharmacistName.getText().toString().trim();
                String PuserName = pharmacistUsername.getText().toString().trim();
                String Pemail = pharmacistEmail.getText().toString().trim();
                String Paddress = pharmacistAddress.getText().toString().trim();
                String Pqualification = pharmacistQualification.getText().toString().trim();
                String Plicense = pharmacistLicense.getText().toString().trim();
                String Ppassword = pharmacistPassword.getText().toString().trim();

                if(Pname.isEmpty()){
                    pharmacistName.setError("Name cannot be Empty!");
                }
                if(PuserName.isEmpty()){
                    pharmacistUsername.setError("Username cannot be Empty!");
                }
                if(Pemail.isEmpty()){
                    pharmacistUsername.setError("Username cannot be Empty!");
                }
                if(Paddress.isEmpty()){
                    pharmacistEmail.setError("Email cannot be Empty!");
                }
                if(Pqualification.isEmpty()){
                    pharmacistEmail.setError("Email cannot be Empty!");
                }
                if(Plicense.isEmpty()){
                    pharmacistQualification.setError("Qualification cannot be Empty!");
                }
                if(Ppassword.isEmpty()){
                    pharmacistPassword.setError("Email cannot be Empty!");
                }else{
                    auth.createUserWithEmailAndPassword(Pemail,Ppassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(RegisterPharmacist.this, "Signup Successfull!!", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(RegisterPharmacist.this,AddMedicine.class));
                            }else{
                                Toast.makeText(RegisterPharmacist.this, "Registration Faied!" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }



            }
        });

        RegisterRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterPharmacist.this, LoginActivity.class));
            }
        });



    }




}