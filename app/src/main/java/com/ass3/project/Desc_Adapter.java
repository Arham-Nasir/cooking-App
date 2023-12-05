package com.ass3.project;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.stringtemplate.v4.ST;

import java.util.ArrayList;

public class Desc_Adapter extends RecyclerView.Adapter<Desc_Adapter.DescView>  {
    ArrayList<Description> descList = new ArrayList<>();
    String No="0";
    Context c;

    public Desc_Adapter(ArrayList<Description> descList, Context c) {
        this.No="0";
        this.descList = descList;
        this.c = c;
    }


    @NonNull
    @Override
    public DescView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_show_desc,parent,false);

        return new Desc_Adapter.DescView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DescView holder, int position) {
        Description description = descList.get(position);
        No = (""+(position+1));
        holder.Step_no.setText(No);
        holder.Desc_Details.setText(description.getDetails());

//        Toast.makeText(c, "Step : " + holder.Desc_Details.getText().toString(), Toast.LENGTH_SHORT).show();


    }

    @Override
    public int getItemCount() {
        return descList.size();
    }

    public class DescView extends RecyclerView.ViewHolder {
        TextView Desc_Details,Step_no;

        public DescView(@NonNull View itemView) {
            super(itemView);

            Desc_Details = (TextView)itemView.findViewById(R.id.desc_details);
            Step_no = (TextView) itemView.findViewById(R.id.Step_NO);


        }
    }
}
