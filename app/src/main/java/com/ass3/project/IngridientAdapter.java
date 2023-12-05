package com.ass3.project;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class IngridientAdapter extends RecyclerView.Adapter<IngridientAdapter.IngridientView> {
    ArrayList<Ingridients> ingridientsList = new ArrayList<>();

    public IngridientAdapter(ArrayList<Ingridients> ingridientsList) {
        this.ingridientsList = ingridientsList;
    }

    @NonNull
    @Override
    public IngridientView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.show_ingrid,parent,false);
        return new IngridientView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IngridientView holder, int position) {
        Ingridients ingridients = ingridientsList.get(position);
        holder.IngridientName.setText(ingridients.getName());
        holder.IngridientAmount.setText(ingridients.getAmount());

    }

    @Override
    public int getItemCount() {
        return ingridientsList.size();
    }

    public class IngridientView extends RecyclerView.ViewHolder{

        TextView IngridientName,IngridientAmount;
        public IngridientView(@NonNull View itemView) {
            super(itemView);

            IngridientName = (TextView)itemView.findViewById(R.id.ingridient_name);
            IngridientAmount = itemView.findViewById(R.id.ingridient_amount);

        }
    }
}
