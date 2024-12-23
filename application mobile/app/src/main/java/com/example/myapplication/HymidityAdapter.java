package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class HymidityAdapter extends RecyclerView.Adapter<HymidityAdapter.ViewHolder>{
    Context context;
    ArrayList<Hymidity> hymidityArrayList;
    public HymidityAdapter(Context context, ArrayList<Hymidity> hymidityArrayList){
        this.context=context;
        this.hymidityArrayList=hymidityArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_humidity, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Hymidity hymidity=hymidityArrayList.get(position);
        holder.HumidityMax.setText(hymidity.HumidityMax+"H ");
        holder.HumidityMin.setText(hymidity.HumidityMin+"H ");
        holder.currentHour.setText(hymidity.currentHour);
    }

    @Override
    public int getItemCount() {
        return hymidityArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView HumidityMax,HumidityMin,currentHour;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            HumidityMax=itemView.findViewById(R.id.humpmax);
            HumidityMin=itemView.findViewById(R.id.humpmin);
            currentHour=itemView.findViewById(R.id.cureenttime);

        }
    }

}
