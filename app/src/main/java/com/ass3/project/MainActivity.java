package com.ass3.project;

import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;

import java.net.InetAddress;

public class MainActivity extends AppCompatActivity {



    BottomNavigationView bottomNavigationView;
    home home = new home();
    Favourite favourite  = new Favourite();
    Uploads uploads = new Uploads();
    Download download  = new Download();
    Upload upload = new Upload();
    FloatingActionButton Upload;
    String Email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);




        Email = getIntent().getStringExtra("email");







        bottomNavigationView = findViewById(R.id.bottom);
        Upload = (FloatingActionButton) findViewById(R.id.upload);


        bottomNavigationView.setBackground(null);
        bottomNavigationView.getMenu().getItem(2).setEnabled(false);




        if (isNetworkAvailable(this))
        {
//            Toast.makeText(this, "Internet Available" , Toast.LENGTH_SHORT).show();
            getSupportFragmentManager().beginTransaction().replace(R.id.container,home).commit();

            Upload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//              Toast.makeText(MainActivity.this, "Data Added Successfully", Toast.LENGTH_SHORT).show();
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.container,upload);
                    transaction.commit();
                }
            });
            bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {


                    switch (item.getItemId())
                    {
                        case R.id.home:
//                        Toast.makeText(MainActivity.this, "Data Added Successfully", Toast.LENGTH_SHORT).show();
                            getSupportFragmentManager().beginTransaction().replace(R.id.container,home).commit();
                            return true;
                        case R.id.uploads:
                            getSupportFragmentManager().beginTransaction().replace(R.id.container,uploads).commit();
                            return true;
                        case R.id.save:
                            getSupportFragmentManager().beginTransaction().replace(R.id.container,download).commit();
                            return true;
                        case R.id.fav:
                            getSupportFragmentManager().beginTransaction().replace(R.id.container,favourite).commit();
                            return true;
                    }




                    return false;
                }
            });

        }
        else{
//            Toast.makeText(this, "Internet not Available " , Toast.LENGTH_SHORT).show();
            getSupportFragmentManager().beginTransaction().replace(R.id.container,download).commit();
        }

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

    public boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }



    public String getEmail() {
        return Email;
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