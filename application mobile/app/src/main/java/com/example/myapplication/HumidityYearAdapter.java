package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class HumidityYearAdapter extends RecyclerView.Adapter<HumidityYearAdapter.ViewHolder>{
    Context context;
    ArrayList<YearClass> yearClasses;
    public HumidityYearAdapter(Context context, ArrayList<YearClass> monthClasses){
        this.context=context;
        this.yearClasses=monthClasses;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_day, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        YearClass yearClass=yearClasses.get(position);

        holder.huma.setText(yearClass.huma+"% ");
        holder.currentanne.setText("Year "+yearClass.currentanne);



    }

    @Override
    public int getItemCount() {
        return yearClasses.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView prej,huma,currentanne,Co2j,tempj;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            huma=itemView.findViewById(R.id.cmax);
            currentanne=itemView.findViewById(R.id.cureenttime);
        }
    }

}