package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AllclassAdapter extends RecyclerView.Adapter<AllclassAdapter.ViewHolder>{
    Context context;
    ArrayList<YearClass> yearClasses;
    public AllclassAdapter(Context context, ArrayList<YearClass> monthClasses){
        this.context=context;
        this.yearClasses=monthClasses;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_allclass, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        YearClass yearClass=yearClasses.get(position);

        holder.Co2a.setText("CO2: "+yearClass.Co2a+"ppm ");
        holder.currentanne.setText("Year: "+yearClass.currentanne);
        holder.tempa.setText("Temp: "+yearClass.tempa+"°C ");
        holder.huma.setText("Humid: "+yearClass.huma+"%");
        holder.prea.setText("Press: "+yearClass.prea+"Pa ");



    }

    @Override
    public int getItemCount() {
        return yearClasses.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView prea,huma,currentanne,Co2a,tempa;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            currentanne=itemView.findViewById(R.id.cureenttime);
            tempa=itemView.findViewById(R.id.cmax);
            huma=itemView.findViewById(R.id.cmin);
            prea=itemView.findViewById(R.id.cmi);
            Co2a=itemView.findViewById(R.id.cma);


        }
    }

}