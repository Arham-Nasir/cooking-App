package com.ass3.project;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class FavAdapter extends RecyclerView.Adapter<FavAdapter.MyViewHolder> {

    List<DishesData> ls;
    Context c;

    public FavAdapter(List<DishesData> ls, Context c) {
        this.ls = ls;
        this.c = c;
    }


    @NonNull
    @Override
    public FavAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View row= LayoutInflater.from(c).inflate(R.layout.favouritedishes,parent,false);
        return new MyViewHolder(row);

    }

    @Override
    public void onBindViewHolder(@NonNull FavAdapter.MyViewHolder holder, int position) {
        holder.name.setText(ls.get(position).getName());
        holder.author.setText(ls.get(position).getAuthor());
        holder.time.setText(ls.get(position).getTime());
        Picasso.get().load(ls.get(position).getImage()).into(holder.image);
        holder.difficulty.setText(ls.get(position).getDifficulty());
        String ID = ls.get(position).getId();
        String Father = ls.get(position).getFather();



        holder.favouriteRV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(c, Dish_Details.class);
                i.putExtra("father",Father);
                i.putExtra("ID",ID);
                c.startActivity(i);

            }
        });

    }


    @Override
    public int getItemCount() {
        return ls.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout favouriteRV;
        ImageView image;
        TextView name,author,time,difficulty;
        public MyViewHolder(@NonNull View itemView){
            super(itemView);
            favouriteRV = itemView.findViewById(R.id.categoryID);
            image = itemView.findViewById(R.id.dishimage);
            name = itemView.findViewById(R.id.dishname);
            author = itemView.findViewById(R.id.authorname);
            time = itemView.findViewById(R.id.time);
            difficulty = itemView.findViewById(R.id.level);

        }

    }

}
