package com.ass3.project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
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
import java.util.List;

public class Add_Ingridients extends AppCompatActivity {

    LinearLayout layoutList;
    Button buttonAdd;
    Button buttonSubmitList;
    String ID;
    int item_No = 1;

    ArrayList<Ingridients> ingridientsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_ingridients);
        getSupportActionBar().hide();



        ID = getIntent().getStringExtra("ID");
        layoutList = findViewById(R.id.layout_list);
        buttonAdd = findViewById(R.id.button_add);
        buttonSubmitList = findViewById(R.id.button_submit_list);

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
                    root.child(Id).child(ID).child("ingredients_name").setValue(ingridientsList);

                    Intent intent = new Intent(Add_Ingridients.this,Add_Description.class);
                    intent.putExtra("ID",ID);
//                    Bundle bundle = new Bundle();
//                    bundle.putSerializable("list",ingridientsList);
//                    intent.putExtras(bundle);
                    startActivity(intent);
                    finish();

                }
            }
        });

    }





    private boolean checkIfValidAndRead() {
        ingridientsList.clear();
        boolean result = true;

        for(int i=0;i<layoutList.getChildCount();i++){

            View ingredientview = layoutList.getChildAt(i);

            EditText editTextName = (EditText)ingredientview.findViewById(R.id.edit_ingridient_name);
            EditText editTextAmount = (EditText)ingredientview.findViewById(R.id.edit_ingridient_amount);
            Ingridients ingridients = new Ingridients();

            if(!editTextName.getText().toString().equals("")){
                ingridients.setName(editTextName.getText().toString());
            }else {
                result = false;
                break;
            }

            if(!editTextAmount.getText().toString().equals("")){
                ingridients.setAmount(editTextAmount.getText().toString());
            }else {
                result = false;
                break;
            }


            ingridientsList.add(ingridients);

        }

        if(ingridientsList.size()==0){
            result = false;
            Toast.makeText(this, "Add Ingridients First!", Toast.LENGTH_SHORT).show();
        }else if(!result){
            Toast.makeText(this, "Enter All Details Correctly!", Toast.LENGTH_SHORT).show();
        }


        return result;
    }

    private void addView() {

        final View IngridientView = getLayoutInflater().inflate(R.layout.row_ingridient,null,false);

        EditText editTextName = (EditText)IngridientView.findViewById(R.id.edit_ingridient_name);
        EditText editTextAmount = (EditText)IngridientView.findViewById(R.id.edit_ingridient_amount);
        TextView item_no = (TextView) IngridientView.findViewById(R.id.item_no);
        ImageView imageClose = (ImageView)IngridientView.findViewById(R.id.image_remove);

        item_no.setText(""+item_No);


        imageClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                removeView(IngridientView);

            }
        });

        layoutList.addView(IngridientView);

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