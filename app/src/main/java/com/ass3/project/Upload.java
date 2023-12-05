package com.ass3.project;

import static android.app.Activity.RESULT_OK;
import static com.facebook.FacebookSdk.getApplicationContext;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.viewmodel.CreationExtras;

import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

public class Upload extends Fragment implements AdapterView.OnItemSelectedListener {

    //DropDown Menu
    SystemDatabase systemDataBase;
    Spinner spin_diff;
    Spinner spin_cat;
    String[] Difficultyoptions={"Easy","Medium","Difficult"};
    int flags[] = {R.drawable.ic_baseline_layers_24, R.drawable.ic_baseline_layers_24, R.drawable.ic_baseline_layers_24};
    String[] CategoryOptions={"BBQ","Western","Local","Drink","Desert"};
    int flags2[] = {R.drawable.bbq, R.drawable.western, R.drawable.local, R.drawable.drink, R.drawable.deserts};
    //DropDown Menu

    androidx.appcompat.widget.AppCompatButton Add_Dish;
    ImageView Recipephoto;
    EditText RecipeName,RecipeTime,RecipeServing;
    String Difficulty;
    String Category;


    String dishname,dishtime,dishserving,dishdifficulty,dishcategory,dishauthor;
    byte[] bytes;
    Bitmap bitmap;
    Uri dpp;
    final int PICK_IMAGE_REQUEST = 21;
    String Name;
    String UserID;
    String dish_count;


    FirebaseAuth mAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    Profiledata profiledata;
    //Firebase
    FirebaseStorage storage;
    StorageReference storageReference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_upload, container, false);

        systemDataBase = new SystemDatabase(getActivity());
        //DropDown Menu
        spin_diff = (Spinner) view.findViewById(R.id.spinner_difficulty);
        spin_diff.setOnItemSelectedListener(this);
        CustomAdapter customAdapter = new CustomAdapter(getActivity().getApplicationContext(),flags,Difficultyoptions);
        spin_diff.setAdapter(customAdapter);
        spin_diff.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Difficulty = Difficultyoptions[position];
//                Toast.makeText(getActivity().getApplicationContext(),
//                                Difficulty,
//                                Toast.LENGTH_LONG)
//                        .show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spin_cat = (Spinner) view.findViewById(R.id.spinner_category);
        spin_cat.setOnItemSelectedListener(this);
        CategoryCustomAdapter customAdapter2 = new CategoryCustomAdapter(getActivity().getApplicationContext(),flags2,CategoryOptions);
        spin_cat.setAdapter(customAdapter2);
        spin_cat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Category = CategoryOptions[position];
//                Toast.makeText(getActivity().getApplicationContext(),
//                                Category,
//                                Toast.LENGTH_LONG)
//                        .show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        //DropDown Menu

        UserID = FirebaseAuth.getInstance().getCurrentUser().getUid();
//        Toast.makeText(activity, "User Id : " + UserID , Toast.LENGTH_LONG)
//                .show();


//        DatabaseReference updateCount = FirebaseDatabase.getInstance().getReference("User_info").child(UserID);
//        updateCount.orderByChild("fname").equalTo(dishauthor).addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                for(DataSnapshot ds: snapshot.getChildren()){
//                    dish_count = ds.child("upload_count").getValue(String.class);
//                    dish_count = (""+1);
//                    dish_count = (""+1);
//                    Toast.makeText(getActivity().getApplicationContext(), "Dish Count : " + dish_count,
//                                    Toast.LENGTH_LONG)
//                            .show();
//                }
//            }
//
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });


        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("User_info");
        ref.child(UserID)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Name=snapshot.child("fname").getValue().toString();
//                        Toast.makeText(getActivity(), "User Id : " + Name , Toast.LENGTH_LONG)
//                        .show();
                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        throw error.toException();

                    }
                });


        Recipephoto = view.findViewById(R.id.recipeimage);
        RecipeName = view.findViewById(R.id.Recipename);
        RecipeTime = view.findViewById(R.id.Recipetime);
        RecipeServing = view.findViewById(R.id.Recipeserving);
        Add_Dish = view.findViewById(R.id.ADD_Dishes_Info);



//        Uploading Dp
        Recipephoto.setOnClickListener(new View.OnClickListener() {
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


        Add_Dish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dishname = RecipeName.getText().toString();
                dishauthor = Name;
                dishcategory = Category;
                dishdifficulty = Difficulty;
                dishserving = RecipeServing.getText().toString();
                dishtime = RecipeTime.getText().toString();
                    if (TextUtils.isEmpty(dishname)) {
                        RecipeName.setError("Dish name can not be Empty");
                        RecipeName.requestFocus();
                        return;
                    }
                    else if (TextUtils.isEmpty(dishserving)) {
                        RecipeServing.setError("Serving can not be Empty");
                        RecipeServing.requestFocus();
                        return;
                    }
                    else if (TextUtils.isEmpty(dishtime)) {
                        RecipeTime.setError("Dish time can not be Empty");
                        RecipeTime.requestFocus();
                        return;
                    }
                    else
                    {
                        addDatatoFirebase();
                    }

            }
        });




        return view;
    }

    private void addDatatoFirebase() {
        final ProgressDialog dialog=new ProgressDialog(getActivity());
        dialog.setTitle("Recipe Uploading....");
        dialog.setIcon(R.mipmap.ic_launcher);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(true);
        dialog.show();
        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        dishname = RecipeName.getText().toString();
        dishauthor = Name;
        dishcategory = Category;
        dishdifficulty = Difficulty;
        dishserving = RecipeServing.getText().toString();
        dishtime = RecipeTime.getText().toString();


        Calendar C = Calendar.getInstance();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String Id = currentUser.getUid();
        FirebaseStorage storage=FirebaseStorage.getInstance();
        final StorageReference uploader=storage.getReference("Image1"+ C.getTimeInMillis()+"jpg");


        uploader.putFile(dpp).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
                    {
                        uploader.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri){

                                dialog.dismiss();
                                dialog.setCancelable(true);
                                getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                String IdDishID = FirebaseDatabase.getInstance().getReference("Recipe_info").child(Id).push().getKey();
                                DatabaseReference root=FirebaseDatabase.getInstance().getReference("Recipe_info");
                                DishesData obj=new DishesData(Id,IdDishID,uri.toString(),dishname,dishauthor,dishtime,dishdifficulty,dishcategory,dishserving);
                                root.child(Id).child(IdDishID).setValue(obj);

                                DatabaseReference updateCount = FirebaseDatabase.getInstance().getReference("User_info").child(Id);
                                updateCount.child("Upload_Dishes").child(IdDishID).setValue(true);
                                updateCount.child("upload_count").setValue(ServerValue.increment(1));

                                updateCount.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        int Count = ((Long) snapshot.child("upload_count").getValue()).intValue();
                                        systemDataBase.UpdateCount(Id,Count);
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });



//
                                Toast.makeText(getActivity(), "Dish Data Setup Successfull", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(getActivity(), Add_Ingridients.class);
                                i.putExtra("ID",IdDishID);
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            dpp = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), dpp);
                Recipephoto.setImageBitmap(bitmap);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}