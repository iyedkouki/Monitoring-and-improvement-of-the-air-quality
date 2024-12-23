package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PressureAdapter extends RecyclerView.Adapter<PressureAdapter.ViewHolder>{
        Context context;
        ArrayList<Pressure> pressureArrayList;
public PressureAdapter(Context context, ArrayList<Pressure> pressureArrayList){
        this.context=context;
        this.pressureArrayList=pressureArrayList;
        }

@NonNull
@Override
public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_pressure, parent, false);
        return new ViewHolder(view);
        }

@Override
public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Pressure pressure=pressureArrayList.get(position);
        holder.PressureMax.setText(pressure.PressureMax+"Pa ");
        holder.PressureMin.setText(pressure.PressureMin+"Pa ");
        holder.currentHour.setText(pressure.currentHour);
        }

@Override
public int getItemCount() {
        return pressureArrayList.size();
        }

public static class ViewHolder extends RecyclerView.ViewHolder {
    TextView PressureMax,PressureMin,currentHour;
    public ViewHolder(@NonNull View itemView) {
        super(itemView);
        PressureMax=itemView.findViewById(R.id.pmax);
        PressureMin=itemView.findViewById(R.id.pmin);
        currentHour=itemView.findViewById(R.id.cureenttime);

    }
}

}