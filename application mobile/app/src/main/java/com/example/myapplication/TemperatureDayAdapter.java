package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TemperatureDayAdapter extends RecyclerView.Adapter<TemperatureDayAdapter.ViewHolder>{
    Context context;
    ArrayList<dayclass> dayclasses;
    public TemperatureDayAdapter(Context context, ArrayList<dayclass> dayclasses){
        this.context=context;
        this.dayclasses=dayclasses;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_day, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        dayclass dayclass=dayclasses.get(position);

        holder.tempj.setText(dayclass.prej+"°C ");
        holder.currentjour.setText(dayclass.currentjour);



    }

    @Override
    public int getItemCount() {
        return dayclasses.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView prej,humj,currentjour,Co2j,tempj;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tempj=itemView.findViewById(R.id.cmax);

            currentjour=itemView.findViewById(R.id.cureenttime);

        }
    }

}