package com.ass3.project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class Add_Description extends AppCompatActivity {
    LinearLayout layoutList;
    Button buttonAdd;
    Button buttonSubmitList;
    String ID;
    int item_No = 1;

    int check =0;
    ArrayList<Description> DescList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_description);
        getSupportActionBar().hide();

        ID = getIntent().getStringExtra("ID");
        layoutList = findViewById(R.id.layout_list_desc);
        buttonAdd = findViewById(R.id.button_add_desc);
        buttonSubmitList = findViewById(R.id.button_submit_desc);

        addView();
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                item_No = item_No+1;
                addView();

            }
        });

        buttonSubmitList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkIfValidAndRead()){

                    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                    String Id = currentUser.getUid();

                    FirebaseDatabase db=FirebaseDatabase.getInstance();
                    DatabaseReference root=db.getReference("Recipe_info");
                    root.child(Id).child(ID).child("description").setValue(DescList);

                    Intent intent = new Intent(Add_Description.this,MainActivity.class);
//                    intent.putExtra("ID",ID);
//                    Bundle bundle = new Bundle();
//                    bundle.putSerializable("list",DescList);
//                    intent.putExtras(bundle);
                    startActivity(intent);
                    finish();

                }
            }
        });


    }


    private boolean checkIfValidAndRead() {
        DescList.clear();
        boolean result = true;

        for(int i=0;i<layoutList.getChildCount();i++){

            View descView = layoutList.getChildAt(i);

            EditText editTextName = (EditText) descView.findViewById(R.id.desc_steps);
            Description description = new Description();

            if(!editTextName.getText().toString().equals("")){
                description.setDetails(editTextName.getText().toString());

            }else {
                result = false;
                break;
            }




            DescList.add(description);

        }

        if(DescList.size()==0){
            result = false;
            Toast.makeText(this, "Add Descripton First!", Toast.LENGTH_SHORT).show();
        }else if(!result){
            Toast.makeText(this, "Enter All Details Correctly!", Toast.LENGTH_SHORT).show();
        }


        return result;
    }

    private void addView() {

        final View DescView = getLayoutInflater().inflate(R.layout.row_description,null,false);

        EditText editTextName = (EditText)DescView.findViewById(R.id.desc_steps);
        TextView item_no = (TextView) DescView.findViewById(R.id.Step_no);
        ImageView imageClose = (ImageView)DescView.findViewById(R.id.image_remove_desc);

        item_no.setText(""+item_No);


        imageClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                removeView(DescView);

            }
        });

        layoutList.addView(DescView);

    }

    private void removeView(View view){

        layoutList.removeView(view);

    }

    @Override
    public void onBackPressed()
    {
        Toast.makeText(this, "You can't go back before completion", Toast.LENGTH_SHORT).show();
//        super.onBackPressed();
//        moveTaskToBack(true);
    }

}