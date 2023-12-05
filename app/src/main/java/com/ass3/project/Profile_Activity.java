package com.ass3.project;

import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class Profile_Activity extends AppCompatActivity {

    SystemDatabase systemDataBase;
    CircleImageView Profile_pic;
    EditText username,about;
    TextView no_of_dishes;
    String UserID;
    ImageView back,editname,editabout;
    ArrayList<String> profiledata = new ArrayList<>();
//    String profiledata;
    Boolean check = false;
    androidx.appcompat.widget.AppCompatButton update_Button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        getSupportActionBar().hide();

        systemDataBase = new SystemDatabase(Profile_Activity.this);
        Profile_pic = findViewById(R.id.profilepicture);
        username = findViewById(R.id.User_Name);
        about = findViewById(R.id.About);
        no_of_dishes = findViewById(R.id.Uploaded_dishes);
        back = findViewById(R.id.backbutton);
        editname = findViewById(R.id.editName);
        editabout = findViewById(R.id.editAbout);
        update_Button = findViewById(R.id.updateProfile);

        UserID = FirebaseAuth.getInstance().getCurrentUser().getUid();


//        profiledata = systemDataBase.getData(UserID);

//        username.setText(profiledata.get(0));
//        about.setText(profiledata.get(1));
//        no_of_dishes.setText(profiledata.get(2));
//        String details = profiledata.get(3);
//        byte[] y = Base64.getDecoder().decode(details);
//        Profile_pic.setImageBitmap(BitmapFactory.decodeByteArray(y, 0, y.length));



        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String Id = currentUser.getUid();
        final FirebaseDatabase Update_Name = FirebaseDatabase.getInstance();
        DatabaseReference Ref = Update_Name.getReference("User_info").child(Id);
        Ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                no_of_dishes.setText(snapshot.child("upload_count").getValue().toString());
                username.setText(snapshot.child("fname").getValue().toString());
                about.setText(snapshot.child("lname").getValue().toString());
                String link = snapshot.child("dp").getValue(String.class);
                Picasso.get().load(link).into(Profile_pic);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });






//        StringRequest request=new StringRequest(Request.Method.POST, "http://172.17.56.136/getProfile.php",
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        try {
//                            JSONObject obj=new JSONObject(response);
//                            if(obj.getString("status").equals("1"))
//                            {
////                                username.setText(obj.getString("name"));
////                                about.setText(obj.getString("about"));
//////                                no_of_dishes.setText(obj.getString("dishescount"));
////                                String details = obj.getString("picture");
////                                byte[] y = Base64.getDecoder().decode(details);
////                                Profile_pic.setImageBitmap(BitmapFactory.decodeByteArray(y, 0, y.length));
//                                Toast.makeText(Profile_Activity.this,"Data added By API",Toast.LENGTH_LONG).show();
//                            }
//                            else{
//                                Toast.makeText(Profile_Activity.this,"Error in Getting data from API",Toast.LENGTH_LONG).show();
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                            Toast.makeText(Profile_Activity.this,e.toString(),Toast.LENGTH_LONG).show();
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Toast.makeText(Profile_Activity.this,error.toString(),Toast.LENGTH_LONG).show();
//                    }
//                })
//        {
//            @Nullable
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String, String> params=new HashMap<>();
//                params.put("id",Id);
//                return params;
//            }
//        };
//        RequestQueue queue= Volley.newRequestQueue(Profile_Activity.this);
//        queue.add(request);




        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        editname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                username.requestFocus();
                username.setEnabled(true);
                username.setFocusable(true);
                showKeyboard(username);
                check = true;

            }
        });

        editabout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                about.requestFocus();
                about.setEnabled(true);
                about.setFocusable(true);
                showKeyboard(about);
                check = true;
            }
        });

        update_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(check == false)
                {
                    Toast.makeText(getApplicationContext(), "Nothing Updated!", Toast.LENGTH_LONG).show();
                }
                else
                {
                    check = false;
                    String Name = username.getText().toString();
                    String About = about.getText().toString();
                    username.setEnabled(false);
                    about.setEnabled(false);
                    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                    String Id = currentUser.getUid();
                    final FirebaseDatabase Update_Name = FirebaseDatabase.getInstance();
                    DatabaseReference Ref = Update_Name.getReference("User_info").child(Id);
                    Ref.child("fname").setValue(Name);
                    Ref.child("lname").setValue(About);


                    systemDataBase.UpdateData(Id,Name,About);

                    Toast.makeText(getApplicationContext(), "Update Successfully!", Toast.LENGTH_LONG).show();

                }


            }
        });



    }
    @Override
    public void onBackPressed()
    {
        super.onBackPressed();

    }
    public void showKeyboard(final EditText ettext){
        ettext.requestFocus();
        ettext.postDelayed(new Runnable(){
                               @Override public void run(){
                                   InputMethodManager keyboard=(InputMethodManager)getSystemService(getApplicationContext().INPUT_METHOD_SERVICE);
                                   keyboard.showSoftInput(ettext,0);
                               }
                           }
                ,200);
    }
}