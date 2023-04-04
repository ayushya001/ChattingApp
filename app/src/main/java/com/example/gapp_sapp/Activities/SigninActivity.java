package com.example.gapp_sapp.Activities;

import static android.content.ContentValues.TAG;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.gapp_sapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SigninActivity extends AppCompatActivity {

    TextView signuppage;
    EditText email, password;
    MaterialButton signin;
    FirebaseAuth mauth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        mauth = FirebaseAuth.getInstance();
        signuppage = findViewById(R.id.signuppage);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        signin = findViewById(R.id.signin);

        mauth = FirebaseAuth.getInstance();
        signuppage = findViewById(R.id.signuppage);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        signin = findViewById(R.id.signin);


        signuppage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), SignupActivity.class));
            }
        });
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sigingin();
            }
        });
    }

    private void sigingin() {


        if (email.getText().toString().trim().isEmpty()) {
            email.setError("Enter your email Id");
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email.getText().toString().trim()).matches()) {
            Toast.makeText(SigninActivity.this, "Email id is not valid", Toast.LENGTH_SHORT).show();
        } else if (password.getText().toString().isEmpty()) {
            password.setError("Password must not be Blank");
        } else {
            ProgressDialog d  = new ProgressDialog(this);
            d.setTitle("Signing in..");
            d.setCanceledOnTouchOutside(false);
            String Email = email.getText().toString();
            String Password = password.getText().toString();
            d.show();
            mauth.signInWithEmailAndPassword(Email,Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (d.isShowing()){
                        d.cancel();
                    }
                    if (task.isSuccessful()){
                        Toast.makeText(SigninActivity.this, "Welcome", Toast.LENGTH_SHORT).show();
                        Log.d("idss", "onComplete: "+mauth.getUid());
                        startActivity(new Intent(getApplicationContext(), ConversationActivity.class));
                        finish();

                    }


                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    d.cancel();
                    Toast.makeText(SigninActivity.this, "Something went wrong ", Toast.LENGTH_SHORT).show();
                    Log.d("onfail", "onFailure: "+e.toString());

                }
            });

        }


    }
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mauth.getCurrentUser();
        if (currentUser != null) {
            startActivity(new Intent(getApplicationContext(), ConversationActivity.class));
        }
    }
}