package com.ass3.project;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class Download extends Fragment {

    ImageView menu_button;
    RecyclerView DownRV;
    DownloadAdapter DownAdapter;
    List<DishesData> Downlist;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    TextView drawer_name,emptytext;
    FirebaseAuth mAuth;
    CircleImageView drawer_pic;
    String UserID,Name;
    CircleImageView Profile_photo;
    com.airbnb.lottie.LottieAnimationView emptyanim;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_download, container, false);

        DownRV = view.findViewById(R.id.downRv);
        menu_button = view.findViewById(R.id.menu_icon_down);
        drawerLayout = view.findViewById(R.id.drawer_down);
        navigationView = view.findViewById(R.id.nav_down);
        drawer_name = navigationView.getHeaderView(0).findViewById(R.id.profilename);
        drawer_pic = navigationView.getHeaderView(0).findViewById(R.id.profilepic);
        Profile_photo = view.findViewById(R.id.profilepic_down);
        emptytext = view.findViewById(R.id.emptytext);
        emptyanim = view.findViewById(R.id.emptyanim);


        UserID = FirebaseAuth.getInstance().getCurrentUser().getUid();
//        Toast.makeText(activity, "User Id : " + UserID , Toast.LENGTH_LONG)
//                .show();

        Name = drawer_name.getText().toString();

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("User_info");

        ref.child(UserID)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Name=snapshot.child("fname").getValue().toString();
                        String link = snapshot.child("dp").getValue(String.class);
                        Picasso.get().load(link).into(drawer_pic);
                        Picasso.get().load(link).into(Profile_photo);
                        drawer_name.setText(Name);
                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        throw error.toException();

                    }
                });


        show_download_dishes();

        Downlist = new ArrayList<>();

        DownAdapter = new DownloadAdapter(Downlist, getActivity());
        RecyclerView.LayoutManager lm = new LinearLayoutManager(getActivity());
        DownRV.setLayoutManager(lm);
        DownRV.setAdapter(DownAdapter);


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if(id == R.id.nav_logout){
                    mAuth = FirebaseAuth.getInstance();
                    mAuth.signOut();
                    Intent intent = new Intent(getActivity(), Sign_In.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    getActivity().finish();
                }
                else if(id == R.id.nav_profile)
                {
                    Intent intent = new Intent(getActivity(), Profile_Activity.class);
                    startActivity(intent);
                }
                else if (id == R.id.nav_pass)
                {
                    Intent intent = new Intent(getActivity(), Reset_Password.class);
                    startActivity(intent);

                }
                return true ;
            }
        });

        Profile_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),Profile_Activity.class);
                startActivity(intent);

            }
        });


        menu_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);


                navigationView.getMenu().getItem(2).setEnabled(false);
                navigationView.getMenu().getItem(3).setEnabled(false);
                navigationView.getMenu().getItem(4).setEnabled(false);
                navigationView.getMenu().getItem(5).setEnabled(false);
            }
        });

        return view;


    }

    private void show_download_dishes() {

        final FirebaseDatabase FavDish = FirebaseDatabase.getInstance();
        DatabaseReference Fav = FavDish.getReference("User_info").child(UserID).child("Download_Dishes");

        Fav.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Downlist.clear();
                for(DataSnapshot ds : snapshot.getChildren()) {
                    String favID = ds.getKey().toString();

//                    Toast.makeText(getActivity(),favID,Toast.LENGTH_LONG).show();

                    final FirebaseDatabase GETDishes = FirebaseDatabase.getInstance();
                    DatabaseReference Ref = GETDishes.getReference("Recipe_info");
                    Ref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for(DataSnapshot dp : snapshot.getChildren()) {
                                String User_id = dp.getKey().toString();

                                //                                Toast.makeText(getActivity(),User_id,Toast.LENGTH_LONG).show();


                                final FirebaseDatabase Getdata = FirebaseDatabase.getInstance();
                                DatabaseReference Final = Getdata.getReference("Recipe_info").child(User_id);
                                Final.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        for(DataSnapshot db : snapshot.getChildren()) {



//                                                Toast.makeText(getActivity(),"ID :  " + db.getKey().toString() +"  "+ favID,Toast.LENGTH_LONG).show();

                                            if(db.getKey().toString().equals(favID)) {

                                                String Id = db.child("id").getValue(String.class);
                                                String name = db.child("name").getValue(String.class);
                                                String author = db.child("author").getValue(String.class);
                                                String category = db.child("category").getValue(String.class);
                                                String difficulty = db.child("difficulty").getValue(String.class);
                                                String serving = db.child("serving").getValue(String.class);
                                                String time = db.child("time").getValue(String.class);
                                                String image = db.child("image").getValue(String.class);

//                                                    Toast.makeText(getActivity(),favID,Toast.LENGTH_LONG).show();


                                                Downlist.add(new DishesData(User_id,Id,image,name,author,time,difficulty,category,serving));
                                                DownAdapter.notifyDataSetChanged();

                                                boolean ans = Downlist.isEmpty();
                                                if (ans == true)
                                                {
                                                    emptyanim.setVisibility(View.VISIBLE);
                                                    emptyanim.setVisibility(View.VISIBLE);
                                                }

                                            }

                                        }

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });

                            }




                        }



                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}