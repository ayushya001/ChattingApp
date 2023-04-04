package com.example.gapp_sapp.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.gapp_sapp.Models.UserModel;
import com.example.gapp_sapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class SignupActivity extends AppCompatActivity {
    EditText name,fullname,email,password,cpassword;
    Button signupbtn;
    private FirebaseAuth mAuth;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        mAuth = FirebaseAuth.getInstance();
        TextView signinpage = findViewById(R.id.signinpage);
        name = findViewById(R.id.name);
        fullname = findViewById(R.id.fullName);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        cpassword = findViewById(R.id.cpassword);
        signupbtn = findViewById(R.id.signin);
        signupbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isvalid();
            }
        });
    }

    private void isvalid() {
        if (name.getText().toString().trim().isEmpty()){
            name.setError("name must not be blank");
        }else if (fullname.getText().toString().trim().isEmpty()){
            fullname.setError("Full name must not be empty");
        }else if (email.getText().toString().isEmpty()){
            email.setError("gmail must not be empty");
        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()){
            email.setError("Gmail is invalid");

        }else if (password.getText().toString().trim().isEmpty())
        {
            password.setError("password must not be empty");
        } else if (password.getText().toString().length()<6){
            password.setError("Password should be at least 6 characters ");
        }
        else if (cpassword.getText().toString().trim().isEmpty()){
            cpassword.setError("Confirm your password");
        }

        else if (!cpassword.getText().toString().trim().equals(password.getText().toString().trim())){
            cpassword.setError("password and confirm password must be same");
        }
        else {
            ProgressDialog d = new ProgressDialog(this);
            d.setTitle("Signingup");
            d.setCanceledOnTouchOutside(false);
            d.show();
            String Password = password.getText().toString();
            String Email = email.getText().toString();
            mAuth.createUserWithEmailAndPassword(Email,Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        UserModel user = new UserModel();
                        user.setEmail(email.getText().toString());
                        user.setFullname(fullname.getText().toString());
                        user.setId(mAuth.getUid());
                        user.setLastmessage(0);
                        user.setName(name.getText().toString());
                        user.setPassword(password.getText().toString());
                        db.collection("Users").document(mAuth.getUid()).set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                if(d.isShowing()){
                                    d.cancel();
                                }
                                Toast.makeText(SignupActivity.this, "Signing up Succeessfully", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                                finish();

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(SignupActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();

                            }
                        });
                    }
                    else{
                        Toast.makeText(SignupActivity.this, "Error", Toast.LENGTH_SHORT).show();
                        d.cancel();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(SignupActivity.this, e.toString(), Toast.LENGTH_SHORT).show();

                }
            });



        }
    }
}