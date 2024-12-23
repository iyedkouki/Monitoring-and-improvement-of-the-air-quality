package com.example.myapplication;



import android.content.Context;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TemperatureAdapter extends RecyclerView.Adapter<TemperatureAdapter.ViewHolder>{
    Context context;
    ArrayList<Temperature> temperatureArrayList;
    public  TemperatureAdapter(Context context,ArrayList<Temperature> temperatureArrayList){
        this.context=context;
        this.temperatureArrayList=temperatureArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_temp1, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Temperature temperature=temperatureArrayList.get(position);
        holder.currentHour.setText(temperature.currentHour);
        holder.TemperatureMax.setText(temperature.TemperatureMax+"°C");
        holder.TemperatureMin.setText(temperature.TemperatureMin+"°C");
    }

    @Override
    public int getItemCount() {
        return temperatureArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView TemperatureMax,TemperatureMin,currentHour;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            TemperatureMax=itemView.findViewById(R.id.Tempmax);
            TemperatureMin=itemView.findViewById(R.id.Tempmin);
            currentHour=itemView.findViewById(R.id.cureenttime);

        }
    }




}
