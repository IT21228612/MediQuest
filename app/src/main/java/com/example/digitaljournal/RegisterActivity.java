package com.example.digitaljournal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.identity.android.legacy.Utility;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {

    EditText FullName,UserName,Email,Password;
    Button createAccountBtn;
    TextView loginBtnTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        FullName = findViewById(R.id.editTextFullName);
        UserName = findViewById(R.id.editTextUsername);
        Email = findViewById(R.id.editTextEmail);
        Password = findViewById(R.id.editTextPassword);

        createAccountBtn = findViewById(R.id.create_account_btn);
        loginBtnTextView = findViewById(R.id.login_text_view_btn);

        createAccountBtn.setOnClickListener(v-> createAccount());
        loginBtnTextView.setOnClickListener(v-> finish());


    }

     void createAccount() {

         String fullName = FullName.getText().toString();
         String username  = UserName.getText().toString();
         String email  = Email.getText().toString();
         String password  = Password.getText().toString();

         boolean isValidated = validateData(email,password);

         if(!isValidated){
             return;
         }

         createAccountInFirebase(email,password);



     }

    void createAccountInFirebase(String email,String password){


        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(RegisterActivity.this,
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()){
                            //creating acc is done
                            Toast.makeText(RegisterActivity.this,"Successfully create account,Check email to verify",Toast.LENGTH_SHORT).show();
                            firebaseAuth.getCurrentUser().sendEmailVerification();
                            firebaseAuth.signOut();
                            finish();
                        }else{
                            //failure
                            Toast.makeText(RegisterActivity.this,task.getException().getLocalizedMessage(),Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );



    }




    boolean validateData(String email,String password){
        //validate the data that are input by user.

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            Email.setError("Email is invalid");
            return false;
        }
        if(password.length()<6){
            Password.setError("Password length is invalid");
            return false;
        }

        return true;
    }

}