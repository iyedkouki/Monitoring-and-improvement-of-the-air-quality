package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CO2Adapter extends RecyclerView.Adapter<CO2Adapter.ViewHolder>{
    Context context;
    ArrayList<CO2> co2ArrayList;
    public CO2Adapter(Context context, ArrayList<CO2> co2ArrayList){
        this.context=context;
        this.co2ArrayList=co2ArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_co2, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CO2 co2=co2ArrayList.get(position);
        holder.CO2Max.setText(co2.CO2Max+"ppm ");
        holder.CO2Min.setText(co2.CO2Min+"ppm ");
        holder.currentHour.setText(co2.currentHour);
    }

    @Override
    public int getItemCount() {
        return co2ArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView CO2Max,CO2Min,currentHour;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            CO2Max=itemView.findViewById(R.id.cmax);
            CO2Min=itemView.findViewById(R.id.cmin);
            currentHour=itemView.findViewById(R.id.cureenttime);

        }
    }

}