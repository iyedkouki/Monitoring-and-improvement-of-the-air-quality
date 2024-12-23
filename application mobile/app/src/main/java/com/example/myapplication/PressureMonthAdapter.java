package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PressureMonthAdapter extends RecyclerView.Adapter<PressureMonthAdapter.ViewHolder>{
    Context context;
    ArrayList<MonthClass> monthClasses;
    public PressureMonthAdapter(Context context, ArrayList<MonthClass> monthClasses){
        this.context=context;
        this.monthClasses=monthClasses;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_day, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MonthClass monthClass=monthClasses.get(position);

        holder.prem.setText(monthClass.prem+"Pa ");
        holder.currentmois.setText(monthClass.currentmois);



    }

    @Override
    public int getItemCount() {
        return monthClasses.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView prem,humm,currentmois,Co2j,tempj;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            prem=itemView.findViewById(R.id.cmax);
            currentmois=itemView.findViewById(R.id.cureenttime);
        }
    }

}