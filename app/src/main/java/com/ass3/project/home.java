package com.ass3.project;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class home extends Fragment {
    ImageView menu_button;
    RecyclerView categoryRV;
    CategoryAdapter categoryAdapter;
    LinearLayout Popular;
    RadioButton popular;
    List<DishesData> dishlist;
//    MutableLiveData<Boolean> boolToObserve = new MutableLiveData<Boolean>();
    ArrayList <String>  ID_List = new ArrayList<String>();
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    TextView drawer_name;
    CircleImageView drawer_pic;
    CircleImageView profilepic;
    RadioButton Popular_button;
    RadioButton Western_button;
    RadioButton Local_button;
    RadioButton Drinks_button;
    RadioButton Deserts_button;
    SearchView searchView;
    TextView category_selector,UserName;
    String Name;
    String Email;
    String UserID;
    String Recipe_info_ID;
    RadioGroup CATEGORY;
    FirebaseAuth mAuth;
    androidx.appcompat.widget.AppCompatButton Notify;

    InterstitialAd mInterstitialAd;

    Button Load_Ad;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_home, container, false);




//        FirebaseDatabase.getInstance().setPersistenceEnabled(true);






        MainActivity activity = (MainActivity) getActivity();

        menu_button = view.findViewById(R.id.menu_icon);
        profilepic = view.findViewById(R.id.profilepic);
        UserName = view.findViewById(R.id.username);
        drawerLayout = view.findViewById(R.id.drawer);
        navigationView = view.findViewById(R.id.nav);
//        Notify = view.findViewById(R.id.notifiy);
        drawer_name = navigationView.getHeaderView(0).findViewById(R.id.profilename);
        drawer_pic = navigationView.getHeaderView(0).findViewById(R.id.profilepic);

//        Load_Ad = view.findViewById(R.id.load_ad);
//
//        //AAAAADDDDDDDSSSSSSSSSSS
//
//
//        MobileAds.initialize(getActivity(), new OnInitializationCompleteListener() {
//            @Override
//            public void onInitializationComplete(InitializationStatus initializationStatus) {
//            }
//        });
//        AdRequest adRequest = new AdRequest.Builder().build();
//
//        InterstitialAd.load(getActivity(),"ca-app-pub-2381417715402387/4028636460", adRequest,
//                new InterstitialAdLoadCallback() {
//                    @Override
//                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
//                        // The mInterstitialAd reference will be null until
//                        // an ad is loaded.
//                        mInterstitialAd = interstitialAd;
////                        Log.i(TAG, "onAdLoaded");
//                    }
//
//                    @Override
//                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
//                        // Handle the error
////                        Log.d(TAG, loadAdError.toString());
//                        mInterstitialAd = null;
//                    }
//                });
//
//
//        Load_Ad.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (mInterstitialAd != null) {
//                    mInterstitialAd.show(getActivity());
//                } else {
//                    Log.d("TAG", "The interstitial ad wasn't ready yet.");
//                }
//            }
//        });

//        Toast.makeText(getActivity().getApplicationContext(),
//                        "Registration Successful!"+userId,
//                        Toast.LENGTH_LONG)
//                .show();

//        Notify.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                try {
//                    JSONObject notificationContent = new JSONObject(
//                            "{'contents':{'en':'" + "Today Recipe" + "'},"+
//                                    "'include_player_ids':['" + userId + "']," +
//                                    "'headings':{'en': '" + "Cooking App" + "'}}");
//                    OneSignal.postNotification(notificationContent, null);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        });

                navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        int id = item.getItemId();
                        if (id == R.id.nav_logout) {
                            mAuth = FirebaseAuth.getInstance();
                            mAuth.signOut();
                            Intent intent = new Intent(getActivity(), Sign_In.class);
                            startActivity(intent);
                        } else if (id == R.id.nav_profile) {
                            Intent intent = new Intent(getActivity(), Profile_Activity.class);
                            startActivity(intent);
                        } else if (id == R.id.nav_pass) {
                            Intent intent = new Intent(getActivity(), Reset_Password.class);
                            startActivity(intent);


                        }
                        return true;
                    }
                });




        profilepic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),Profile_Activity.class);
                startActivity(intent);

            }
        });

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
                        Picasso.get().load(link).into(profilepic);
                        drawer_name.setText(Name);
                        UserName.setText(Name);
                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        throw error.toException();

                    }
                });

        Email = activity.getEmail();


        CATEGORY =  view.findViewById(R.id.Category_radio);
        Popular_button = (RadioButton) view.findViewById(R.id.PopularDishes);
        Western_button = view.findViewById(R.id.WesternDishes);
        Local_button = view.findViewById(R.id.LocalDishes);
        Drinks_button = view.findViewById(R.id.Drinks_Recipes);
        Deserts_button = view.findViewById(R.id.Deserts_Recipes);
        category_selector = view.findViewById(R.id.Category_Text);
        searchView = view.findViewById(R.id.searchView);
        EditText editText = (EditText) view.findViewById(R.id.dummy);
        searchView.clearFocus();



        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterlist(newText);
                return true;
            }
        });


        Popular_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                searchView.setQuery("", false);
                searchView.clearFocus();
                category_selector.setText("BBQ Dishes");
                show_data("BBQ");
                categoryAdapter.reset(dishlist);


            }
        });
        Western_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                searchView.setQuery("", false);
                searchView.clearFocus();
                category_selector.setText("Western Dishes");
                show_data("Western");
                categoryAdapter.reset(dishlist);
            }
        });
        Local_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                searchView.setQuery("", false);
                searchView.clearFocus();
                category_selector.setText("Local Dishes");
                show_data("Local");
                categoryAdapter.reset(dishlist);
            }
        });
        Drinks_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                searchView.setQuery("", false);
                searchView.clearFocus();
                category_selector.setText("Drinks Recipees");
                show_data("Drink");
                categoryAdapter.reset(dishlist);
            }
        });
        Deserts_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                searchView.setQuery("", false);
                searchView.clearFocus();
                category_selector.setText("Deserts Dishes");
                show_data("Desert");
                categoryAdapter.reset(dishlist);
            }
        });


        dishlist = new ArrayList<>();
        categoryRV = view.findViewById(R.id.Dishes);
        categoryAdapter = new CategoryAdapter(dishlist, getActivity());
        RecyclerView.LayoutManager lm = new LinearLayoutManager(getActivity());
        categoryRV.setLayoutManager(lm);
        categoryRV.setAdapter(categoryAdapter);













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

    private void show_data(String cat) {
        final FirebaseDatabase GETDishes = FirebaseDatabase.getInstance();
        DatabaseReference Ref = GETDishes.getReference("Recipe_info");

        Ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dishlist.clear();
                for(DataSnapshot ds : snapshot.getChildren()) {

                    String ID_get = ds.getKey().toString();
//                        ID_List.add(ID_get);

//                    Toast.makeText(getActivity(), "Id : " + ID_get, Toast.LENGTH_SHORT).show();


                    DatabaseReference GetData = GETDishes.getReference("Recipe_info").child(ID_get);
                    GetData.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            for(DataSnapshot ds : snapshot.getChildren())
                            {
                                String category = ds.child("category").getValue(String.class);

                                if(ds.child("category").getValue(String.class).equals(cat)) {
                                    String Id = ds.child("id").getValue(String.class);
                                    String name = ds.child("name").getValue(String.class);
                                    String author = ds.child("author").getValue(String.class);
                                    String difficulty = ds.child("difficulty").getValue(String.class);
                                    String serving = ds.child("serving").getValue(String.class);
                                    String time = ds.child("time").getValue(String.class);
                                    String image = ds.child("image").getValue(String.class);
//                                    Toast.makeText(getActivity(), "Id : ", Toast.LENGTH_SHORT).show();


                                    dishlist.add(new DishesData(ID_get, Id, image, name, author, time, difficulty, category, serving));

                                }
                            }
                            categoryAdapter.notifyDataSetChanged();

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

    private void filterlist(String newText) {

        List<DishesData> filteredlist = new ArrayList<DishesData>();
        for (DishesData item : dishlist) {
            if (item.getName().toLowerCase().contains(newText.toLowerCase())) {
                filteredlist.add(item);
            }
            if (filteredlist.isEmpty()) {
                Toast.makeText(getActivity().getApplicationContext(),
                                "No Data Found",
                                Toast.LENGTH_LONG)
                        .show();
                categoryAdapter.setfilterList(filteredlist);
            } else {
                // at last we are passing that filtered
                // list to our adapter class.
                categoryAdapter.setfilterList(filteredlist);
            }
        }
    }


    private void enableSearchView(View view, boolean enabled) {
        view.setEnabled(enabled);
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                View child = viewGroup.getChildAt(i);
                enableSearchView(child, enabled);
            }
        }
    }


    public boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }


    public boolean isInternetAvailable() {
        try {
            InetAddress ipAddr = InetAddress.getByName("google.com");
            //You can replace it with your name
            return !ipAddr.equals("");

        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        dishlist.clear();
        Popular_button.setChecked(true);
        category_selector.setText("BBQ Dishes");
        show_data("BBQ");
    }

}