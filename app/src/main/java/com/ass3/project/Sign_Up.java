package com.ass3.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

public class Sign_Up extends AppCompatActivity {




    androidx.appcompat.widget.AppCompatButton Signup;
    TextView signin_text;
    EditText Name;
    EditText Email;
    EditText number;
    EditText pass;

    String name,email,Dp,Number,Pass;

    CircleImageView dp;
    byte[] bytes;
    Bitmap bitmap;
    Uri dpp;
    private final int PICK_IMAGE_REQUEST = 22;

    // creating a variable for our
    // Firebase Database.
    FirebaseAuth mAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    Profiledata profiledata;
    //Firebase
    FirebaseStorage storage;
    StorageReference storageReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        getSupportActionBar().hide();

        mAuth = FirebaseAuth.getInstance();
        Email = (EditText) findViewById(R.id.upemail);
        pass = (EditText) findViewById(R.id.uppassword) ;
        signin_text = findViewById(R.id.SignIN_Text);
        Signup = findViewById(R.id.SIGNUP);



        signin_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Sign_Up.this,Sign_In.class);
                startActivity(intent);
            }
        });

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Profiledata");
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();


        Signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    registerNewUser();
            }
        });

    }




    private void registerNewUser() {


        // Take the value of two edit texts in Strings
        String email, password;
        email = Email.getText().toString();
        password = pass.getText().toString();

        // Validations for input email and password
        if (TextUtils.isEmpty(email)) {
            Email.setError("Email can not be Empty");
            Email.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            pass.setError("Password can not be Empty");
            pass.requestFocus();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(),
                                    "Registration Successful!",
                                    Toast.LENGTH_LONG)
                            .show();
                    Intent i = new Intent(Sign_Up.this, Profile_Info.class);
                    i.putExtra("email",(email));
                    startActivity(i);
                } else {

                    // Registration failed
                    Toast.makeText(
                                    getApplicationContext(),
                                    "Registration failed!!"
                                            + task.getException().getMessage(),
                                    Toast.LENGTH_LONG)
                            .show();


                }

            }
        });

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