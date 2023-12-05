package com.ass3.project;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.onesignal.OneSignal;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Dish_Details extends AppCompatActivity {

    ImageView Recipe_Image,dish_fav,dish_unfav,Download_Dish;
    androidx.cardview.widget.CardView Back,Download_button;
    TextView Dish_name,Dish_Category,Dish_Time,Dish_Difficulty,Dish_Calories,Dish_Serving,Dish_Author;
    RecyclerView recyclerIngridient,recylerDescription;
    IngridientAdapter ingridientAdapter;
    ArrayList<Ingridients> ingridientsList = new ArrayList<>();
    String ID,Father;
    RecyclerView recyclerDESC;
    ArrayList<Description> descList = new ArrayList<>();
    Desc_Adapter desc_adapter;
    String Unfav_index;
    Boolean favourite = false;
    int size;
    Favourite fav = new Favourite();
    String ID_get;
    String Myname;
    Boolean check = true;
    String User_ID;
    String OneID;
    String dishName,dishauthor;
    ArrayList<Fav_Dishes> favList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dish_details);
        getSupportActionBar().hide();
        recyclerDESC = findViewById(R.id.show_Description);
        Recipe_Image = findViewById(R.id.Recipe_image);
        dish_fav = findViewById(R.id.dish_fav);
        dish_unfav = findViewById(R.id.dish_unfav);
        Back = findViewById(R.id.bak_to_home);
        Download_button = findViewById(R.id.download_dish);
        Dish_name = findViewById(R.id.Dish_Name);
        Dish_Category = findViewById(R.id.Dish_cat);
        Dish_Time = findViewById(R.id.Dish_Time);
        Dish_Difficulty = findViewById(R.id.Dish_Difficulty);
//        Dish_Calories = findViewById(R.id.Dish_Calories);
        Dish_Serving = findViewById(R.id.Dish_Servings);
        recyclerIngridient = findViewById(R.id.show_Ingridients);
        Dish_Author = findViewById(R.id.Dish_Author);
        Download_Dish = findViewById(R.id.downloadDish);






        

        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        ID = getIntent().getStringExtra("ID");
        Father = getIntent().getStringExtra("father");

        LinearLayoutManager layoutManager = new LinearLayoutManager(this,RecyclerView.VERTICAL,false);
        recyclerIngridient.setLayoutManager(layoutManager);
        ingridientAdapter = new IngridientAdapter(ingridientsList);
        recyclerIngridient.setAdapter(ingridientAdapter);

        LinearLayoutManager layoutManager2 = new LinearLayoutManager(this,RecyclerView.VERTICAL,false);
        recyclerDESC.setLayoutManager(layoutManager2);
        desc_adapter = new Desc_Adapter(descList,Dish_Details.this);
        recyclerDESC.setAdapter(desc_adapter);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String Id = currentUser.getUid();
        DatabaseReference root2 = FirebaseDatabase.getInstance().getReference("User_info").child(Id);
        root2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Myname = snapshot.child("fname").getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        User_ID = Father;



        final FirebaseDatabase GETDishes = FirebaseDatabase.getInstance();
        DatabaseReference Ref = GETDishes.getReference("Recipe_info");

        DatabaseReference GetData = Ref.child(User_ID).child(ID);
        GetData.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dishName = snapshot.child("name").getValue(String.class);
                dishauthor = snapshot.child("author").getValue(String.class);


//                Toast.makeText(Dish_Details.this,User_ID+ID+dishName,Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        show_data();
        Get_Ingridients();
        Get_Description();





        dish_unfav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dish_unfav.setVisibility(View.INVISIBLE);
                dish_fav.setVisibility(View.VISIBLE);
                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                String Id = currentUser.getUid();
                FirebaseDatabase db=FirebaseDatabase.getInstance();
                DatabaseReference root=db.getReference("User_info").child(Id);
                root.child("Favourite_Dishes").child(ID).setValue(true);


                DatabaseReference root2 = db.getReference("User_info").child(Father);
                root2.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        OneID = snapshot.child("OneSignal_ID").getValue(String.class);
                        Toast.makeText(Dish_Details.this,"Dish Added to Favourite",Toast.LENGTH_LONG).show();
                        try {
                            JSONObject notificationContent = new JSONObject(
                                    "{'contents':{'en':'" + Myname + " like your Recipe : "+ dishName +"'},"+
                                            "'include_player_ids':['" + OneID + "']," +
                                            "'headings':{'en': '" + "Cooking App" + "'}}");
                            OneSignal.postNotification(notificationContent, null);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Toast.makeText(Dish_Details.this,"Notification send to : "+dishauthor,Toast.LENGTH_LONG).show();
//                        Favourite fragment = new Favourite();
//                        FragmentManager fragmentManager = getSupportFragmentManager();
//                        fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


            }


        });

        dish_fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dish_unfav.setVisibility(View.VISIBLE);
                dish_fav.setVisibility(View.INVISIBLE);
                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                String Id = currentUser.getUid();
                FirebaseDatabase db=FirebaseDatabase.getInstance();
                DatabaseReference root=db.getReference("User_info").child(Id);
                root.child("Favourite_Dishes").child(ID).removeValue();

                Toast.makeText(Dish_Details.this,"Dish removed from your Favourites",Toast.LENGTH_LONG).show();
            }
        });



        Download_Dish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                String Id = currentUser.getUid();
                FirebaseDatabase db=FirebaseDatabase.getInstance();
                DatabaseReference root=db.getReference("User_info").child(Id);
                root.child("Download_Dishes").child(ID).setValue(true);


                        OneID = OneSignal.getDeviceState().getUserId();
//                        Toast.makeText(Dish_Details.this,"Id Added"+OneID,Toast.LENGTH_LONG).show();
                        try {
                            JSONObject notificationContent = new JSONObject(
                                    "{'contents':{'en':'"  + "Recipe : "+ dishName + "  Downloading..."+"'},"+
                                            "'include_player_ids':['" + OneID + "']," +
                                            "'headings':{'en': '" + "Cooking App" + "'}}");
                            OneSignal.postNotification(notificationContent, null);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
//                        Toast.makeText(Dish_Details.this,"Notification send",Toast.LENGTH_LONG).show();




            }
        });




    }

//    private String getUserID() {
//
//        final FirebaseDatabase GetID = FirebaseDatabase.getInstance();
//        DatabaseReference Ref = GetID.getReference("Recipe_info");
//
//        Ref.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                for(DataSnapshot ds : snapshot.getChildren()) {
//                    ID_get = ds.getKey().toString();
//
//
//                    DatabaseReference Getid = FirebaseDatabase.getInstance().getReference("Recipe_info").child(ID_get);
//                    Getid.addListenerForSingleValueEvent(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@NonNull DataSnapshot ds) {
//                            for(DataSnapshot db : ds.getChildren()) {
//
//                                if(db.getKey() == ID){
//                                    check = false;
//                                    break;
//                                }
//
//                            }
//                        }
//
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError error) {
//
//                        }
//                    });
//                    if(check == false) {
//                        break;
//                    }
//
//                }
//
//                Toast.makeText(Dish_Details.this,"Dish Id : "+ ID_get,Toast.LENGTH_LONG).show();
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//        return ID_get;
//
//    }


    private void Get_Description() {

        final FirebaseDatabase GETDishes = FirebaseDatabase.getInstance();
        DatabaseReference Ref = GETDishes.getReference("Recipe_info").child(User_ID).child(ID).child("description");

        Ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ingridientsList.clear();
                for(DataSnapshot ds : snapshot.getChildren()) {
                    String ID_get = ds.getKey().toString();
//                    Toast.makeText(getApplicationContext(), "Desc_Id : " + ID_get, Toast.LENGTH_SHORT).show();
//                        ID_List.add(ID_get);
                    DatabaseReference GetData = GETDishes.getReference("Recipe_info").child(User_ID).child(ID).child("description").child(ID_get);
                    GetData.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot ds) {
                            String string = ds.child("details").getValue(String.class);
//                            Toast.makeText(getApplicationContext(), "String : " + string, Toast.LENGTH_SHORT).show();
                            descList.add(new Description(string));
//                                dishlist.add(new DishesData(Id,image,name,author,time,difficulty,category,serving));
                            desc_adapter.notifyDataSetChanged();

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                throw error.toException();

            }
        });


    }

    private void Get_Ingridients() {

        final FirebaseDatabase GETDishes = FirebaseDatabase.getInstance();
        DatabaseReference Ref = GETDishes.getReference("Recipe_info").child(User_ID).child(ID).child("ingredients_name");
        Ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ingridientsList.clear();
                for(DataSnapshot ds : snapshot.getChildren()) {
                    String ID_get = ds.getKey().toString();
//                    Toast.makeText(getApplicationContext(), "ID_get : " + ID_get, Toast.LENGTH_SHORT).show();
//                        ID_List.add(ID_get);

                    DatabaseReference GetData = GETDishes.getReference("Recipe_info").child(User_ID).child(ID).child("ingredients_name").child(ID_get);
                    GetData.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot ds) {
                                String amount = ds.child("amount").getValue(String.class);
                                String name = ds.child("name").getValue(String.class);
//                                Toast.makeText(getApplicationContext(), "name : " + name, Toast.LENGTH_SHORT).show();
                                ingridientsList.add(new Ingridients(name,amount));
//                                dishlist.add(new DishesData(Id,image,name,author,time,difficulty,category,serving));

                            ingridientAdapter.notifyDataSetChanged();

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                throw error.toException();

            }
        });




    }




    private void show_data() {
        final FirebaseDatabase GETDishes = FirebaseDatabase.getInstance();
        DatabaseReference Ref = GETDishes.getReference("Recipe_info");

                    DatabaseReference GetData = Ref.child(User_ID).child(ID);
                    GetData.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot ds) {

                                String name = ds.child("name").getValue(String.class);
                                String author = ds.child("author").getValue(String.class);
                                String category = ds.child("category").getValue(String.class);
                                String difficulty = ds.child("difficulty").getValue(String.class);
                                String serving = ds.child("serving").getValue(String.class);
                                String time = ds.child("time").getValue(String.class);
                                String image = ds.child("image").getValue(String.class);

//                                Toast.makeText(getApplicationContext(), "Id : " + name, Toast.LENGTH_SHORT).show();

//                                String link = snapshot.child("dp").getValue(String.class);
//                                Picasso.get().load(link).into(drawer_pic);
                                Dish_name.setText(name);
                                Dish_Category.setText(category);
                                Dish_Difficulty.setText(difficulty);
                                Dish_Serving.setText(serving);
                                Dish_Time.setText(time);
                                Dish_Author.setText(author);
                                Picasso.get().load(image).into(Recipe_Image);

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });





    }




}