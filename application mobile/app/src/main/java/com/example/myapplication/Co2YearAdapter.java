package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Co2YearAdapter extends RecyclerView.Adapter<Co2YearAdapter.ViewHolder>{
    Context context;
    ArrayList<YearClass> yearClasses;
    public Co2YearAdapter(Context context, ArrayList<YearClass> monthClasses){
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

        holder.Co2a.setText(yearClass.Co2a+"ppm ");
        holder.currentanne.setText("Year "+yearClass.currentanne);



    }

    @Override
    public int getItemCount() {
        return yearClasses.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView prej,huma,currentanne,Co2a,tempj;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            Co2a=itemView.findViewById(R.id.cmax);
            currentanne=itemView.findViewById(R.id.cureenttime);
        }
    }

}