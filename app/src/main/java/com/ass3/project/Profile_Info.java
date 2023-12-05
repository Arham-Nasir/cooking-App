package com.ass3.project;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.onesignal.OneSignal;

import java.io.IOException;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

public class Profile_Info extends AppCompatActivity {

    androidx.appcompat.widget.AppCompatButton Add_data;
    EditText Name;
    EditText About;
    String fname,lname,Dp;
    String Email;

    String Image;
    CircleImageView ProfilePhoto;
    byte[] bytes;
    Bitmap bitmap;
    Uri dpp;
    int Upload_Count = 0;
    private final int PICK_IMAGE_REQUEST = 22;
    SystemDatabase systemDataBase;

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
        setContentView(R.layout.activity_profile_info);
        getSupportActionBar().hide();

        mAuth = FirebaseAuth.getInstance();
        Email = getIntent().getStringExtra("email");

        systemDataBase = new SystemDatabase(Profile_Info.this);
        ProfilePhoto = findViewById(R.id.profiledp);
        Name = findViewById(R.id.name);
        About = findViewById(R.id.about);
        Add_data = findViewById(R.id.ADD_Data);

//        Uploading Dp
        ProfilePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImage();
            }
        });

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Profiledata");
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        profiledata = new Profiledata();


        // adding on click listener for our button.
        Add_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                fname = Name.getText().toString();
                lname = About.getText().toString();
                if( (TextUtils.isEmpty(fname))  &&  (TextUtils.isEmpty(lname)))
                {
                    if (TextUtils.isEmpty(fname)) {
                        Name.setError("Name can not be Empty");
                        Name.requestFocus();
                        return;
                    }
                    if (TextUtils.isEmpty(lname)) {
                        About.setError("About can not be Empty");
                        About.requestFocus();
                        return;
                    }

                }else
                {
                    addDatatoFirebase();
                }

            }
        });

    }


    private void addDatatoFirebase() {
        final ProgressDialog dialog=new ProgressDialog(this);
        dialog.setTitle("Profile Uploading....");
        dialog.setIcon(R.mipmap.ic_launcher);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(true);
        dialog.show();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        // getting text from our edittext fields.
        String name = Name.getText().toString();
        String about = About.getText().toString();
        String email = Email;

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String Id = currentUser.getUid();
        FirebaseStorage storage=FirebaseStorage.getInstance();
        final StorageReference uploader=storage.getReference("Image1"+new Random().nextInt(50));

        uploader.putFile(dpp).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
                    {
                        uploader.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri){

                                dialog.dismiss();
                                dialog.setCancelable(true);
                                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                FirebaseDatabase db=FirebaseDatabase.getInstance();
                                DatabaseReference root=db.getReference("User_info");
                                Profiledata obj=new Profiledata(uri.toString(),name,about,email,Upload_Count);
                                root.child(Id).setValue(obj);

                                String userId = OneSignal.getDeviceState().getUserId();
                                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                                String Id = currentUser.getUid();
                                FirebaseDatabase onesignal=FirebaseDatabase.getInstance();
                                DatabaseReference ID=onesignal.getReference("User_info").child(Id);
                                ID.child("OneSignal_ID").setValue(userId);

//                                try {
//                                    bitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), dpp);
//                                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
//                                    bitmap.compress(Bitmap.CompressFormat.JPEG,80,stream);
//                                    bytes = stream.toByteArray();
//                                    Image = Base64.getEncoder().encodeToString(bytes);
//                                } catch (IOException e) {
//                                    e.printStackTrace();
//                                }
//                                systemDataBase.AddData(Id,name,about,Upload_Count,Image);

//                                StringRequest request=new StringRequest(Request.Method.POST, "http://172.17.56.136/setProfile.php",
//                                        new Response.Listener<String>() {
//                                            @Override
//                                            public void onResponse(String response) {
//                                                try {
//                                                    JSONObject obj=new JSONObject(response);
//                                                    if(obj.getString("status").equals("1"))
//                                                    {
//                                                        Toast.makeText(Profile_Info.this,"Data added to API",Toast.LENGTH_LONG).show();
//                                                    }
//                                                    else{
//                                                        Toast.makeText(Profile_Info.this,"Error in uploading",Toast.LENGTH_LONG).show();
//                                                    }
//                                                } catch (JSONException e) {
//                                                    e.printStackTrace();
//                                                    Toast.makeText(Profile_Info.this,e.toString(),Toast.LENGTH_LONG).show();
//                                                }
//                                            }
//                                        },
//                                        new Response.ErrorListener() {
//                                            @Override
//                                            public void onErrorResponse(VolleyError error) {
//                                                Toast.makeText(Profile_Info.this,error.toString(),Toast.LENGTH_LONG).show();
//                                            }
//                                        })
//                                {
//                                    @Nullable
//                                    @Override
//                                    protected Map<String, String> getParams() throws AuthFailureError {
//                                        Map<String, String> params=new HashMap<>();
//                                        params.put("id",Id);
//                                        params.put("name",name);
//                                        params.put("about",about);
//                                        params.put("picture",Image);
//                                        return params;
//                                    }
//                                };
//                                RequestQueue queue= Volley.newRequestQueue(Profile_Info.this);
//                                queue.add(request);


                                Toast.makeText(Profile_Info.this, "Profile Setup Successfull", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(Profile_Info.this, MainActivity.class);
                                i.putExtra("email",(email));
                                startActivity(i);




                            }
                        });
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot)
                    {
                        float percent=(100*taskSnapshot.getBytesTransferred())/taskSnapshot.getTotalByteCount();
                        dialog.setMessage("Uploaded :"+(int)percent+" %");
                    }
                });

    }


    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            dpp = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), dpp);
                ProfilePhoto.setImageBitmap(bitmap);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }


}