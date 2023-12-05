package com.ass3.project;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;

public class Show_Ingridients extends AppCompatActivity {

    RecyclerView recyclerIngridients;
    ArrayList<Ingridients> ingridientsList = new ArrayList<>();
    IngridientAdapter ingridientAdapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_ingridients);
        getSupportActionBar().hide();

        recyclerIngridients = findViewById(R.id.recycler_ingridients);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this,RecyclerView.VERTICAL,false);
        recyclerIngridients.setLayoutManager(layoutManager);

        ingridientsList = (ArrayList<Ingridients>) getIntent().getExtras().getSerializable("list");
        ingridientAdapter = new IngridientAdapter(ingridientsList);
        recyclerIngridients.setAdapter(ingridientAdapter);
        ingridientAdapter.notifyDataSetChanged();


    }
}