package com.ass3.project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;

public class Show_Description extends AppCompatActivity {

    RecyclerView recyclerDESC;
    ArrayList<Description> descList = new ArrayList<>();
    Desc_Adapter desc_adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_description);
        getSupportActionBar().hide();

        recyclerDESC = findViewById(R.id.recycler_desc);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this,RecyclerView.VERTICAL,false);
        recyclerDESC.setLayoutManager(layoutManager);
        descList = (ArrayList<Description>) getIntent().getExtras().getSerializable("list");
        desc_adapter = new Desc_Adapter(descList,Show_Description.this);
        recyclerDESC.setAdapter(desc_adapter);
        desc_adapter.notifyDataSetChanged();

    }
}