package com.ass3.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Sign_In extends AppCompatActivity {

    EditText Email,Password;
    androidx.appcompat.widget.AppCompatButton signin_button;
    TextView forgetPass;
    String email, password;
    TextView SignUP;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        getSupportActionBar().hide();


        Email = findViewById(R.id.Email_signin);
        Password = findViewById(R.id.password_signin);
        signin_button = findViewById(R.id.SIGN_IN);
        forgetPass = findViewById(R.id.forgetpassword);
        SignUP = findViewById(R.id.Signup);

        mAuth = FirebaseAuth.getInstance();
        
        signin_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginuser();
            }
        });

        SignUP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Sign_In.this,Sign_Up.class);
                startActivity(intent);
            }
        });
        forgetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Sign_In.this, Reset_Password.class);
                startActivity(intent);
            }
        });



    }

    private void loginuser() {


        // Take the value of two edit texts in Strings

        email = Email.getText().toString();
        password = Password.getText().toString();

        // Validations for input email and password
        if (TextUtils.isEmpty(email)) {
            Email.setError("Email can not be Empty");
            Email.requestFocus();
            return;
        }
        else if (TextUtils.isEmpty(password)) {
            Password.setError("Password can not be Empty");
            Password.requestFocus();
            return;
        }else{
            mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(getApplicationContext(),
                                        "Login Successful!",
                                        Toast.LENGTH_LONG)
                                .show();
                        Intent i = new Intent(Sign_In.this, MainActivity.class);
                        startActivity(i);
                    }else {

                        // Registration failed
                        Toast.makeText(
                                        getApplicationContext(),
                                        "Wrong Credentials"
                                                + task.getException().getMessage(),
                                        Toast.LENGTH_LONG)
                                .show();


                    }


                }
            });
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            Intent i = new Intent(Sign_In.this, MainActivity.class);
            i.putExtra("email",(email));
            startActivity(i);

        }
    }
    @Override
    public void onBackPressed()
    {
        new AlertDialog.Builder(this)
                .setIcon(R.drawable.ic_baseline_exit_to_app_24)
                .setCancelable(false)
                .setTitle("Confirm Exit..?")
                .setMessage("Are you sure you want to Exit?")
                .setPositiveButton(Html.fromHtml("<font color='#FFEB3B'>Yes</font>"), new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finishAffinity();
                    }

                })
                .setNegativeButton(Html.fromHtml("<font color='#FFEB3B'>No</font>"), null)
                .show();
    }
}