package com.example.myapplication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class CO2main extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<CO2> co2ArrayList;
    CO2Adapter myAdapter;
    FirebaseFirestore db;
    TextView ton,tonm,tony;
    TextView Realtime;
    LineChart lineChart;
    LineDataSet lineDataSet;
    LineData lineData;
    ImageView retourne;
    ArrayList<Entry> lineEntries;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_co2main);

        lineChart = findViewById(R.id.lineChart);
        Realtime=findViewById(R.id.realtime);
        recyclerView=findViewById(R.id.recyclerc);
        recyclerView.setHasFixedSize(true);
        //StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        //layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS); // Optional, to remove gaps between items
        //layoutManager.setOrientation(StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(new LinearLayoutManager(com.example.myapplication.CO2main.this));
        retourne=findViewById(R.id.returne_);
        retourne.setOnClickListener(view -> startActivity(new Intent(com.example.myapplication.CO2main.this, MainActivity.class)));

        db= FirebaseFirestore.getInstance();
        co2ArrayList=  new ArrayList<CO2>();
        myAdapter=new CO2Adapter(com.example.myapplication.CO2main.this,co2ArrayList);
        recyclerView.setAdapter(myAdapter);
        EvenChangelister();
        DocumentReference documentReference = db.collection("theSensor").document("Real_Time");
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (value != null && value.exists()) {
                    Realtime.setText(value.getString("CO2"));
                }
            }
        });
    }

    private void EvenChangelister() {
        db.collection("theSensors").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                if(error!= null){

                    Log.e("Firebase error",error.getMessage());
                    return;
                }
                for(DocumentChange dc: value.getDocumentChanges()){
                    if(dc.getType()==DocumentChange.Type.ADDED){
                        co2ArrayList.add(0, dc.getDocument().toObject(CO2.class)); // Add to the beginning of the list
                    }

                    myAdapter.notifyDataSetChanged();


                    updateGraphWithRecyclerViewData();
                }



            }
        });
        ton=findViewById(R.id.button);
        ton.setOnClickListener(view -> startActivity(new Intent(CO2main.this, Co2Day.class)));
        tonm=findViewById(R.id.buttonm);
        tonm.setOnClickListener(view -> startActivity(new Intent(CO2main.this, Co2Month.class)));
        tony=findViewById(R.id.buttony);
        tony.setOnClickListener(view -> startActivity(new Intent(CO2main.this, Co2Year.class)));


    }/*
    private void updateGraph(float newPressureValue) {
        if (lineEntries == null) {
            lineEntries = new ArrayList<>();
        }
        lineEntries.add(new Entry(lineEntries.size(), newPressureValue));
        lineDataSet = new LineDataSet(lineEntries, "Pressure");
        lineData = new LineData(lineDataSet);
        lineChart.setData(lineData);
        lineChart.invalidate(); // Refresh chart
    }

    private void updateGraphWithRecyclerViewData() {
        lineEntries = new ArrayList<>();
        for (int i = 0; i < pressureArrayList.size(); i++) {
            Pressure pressure = pressureArrayList.get(i);
            lineEntries.add(new Entry(i, Float.parseFloat(pressure.getPressureMax())));
        }

        lineDataSet = new LineDataSet(lineEntries, "Pressure");
        lineData = new LineData(lineDataSet);
        lineChart.setData(lineData);
        lineChart.invalidate(); // Refresh chart
    }*/
    /* private void updateGraphWithRecyclerViewData() {
         // Prepare data for the chart
         ArrayList<Entry> entries = new ArrayList<>();
         ArrayList<String> labels = new ArrayList<>();


         // Iterate through the pressureArrayList to get document names and PressureMax values
         for (int i = 0,j=pressureArrayList.size()-1; i < pressureArrayList.size(); i++,j--) {
             Pressure pressure = pressureArrayList.get(j);
             // Use the index as X-axis value (for simplicity) and PressureMax as Y-axis value
             entries.add(new Entry(i, Float.parseFloat(pressure.getPressureMax())));
             labels.add(pressure.getcurrentHour()); // Use currentHour for X-axis labels
         }

         // Create a LineDataSet from the entries
         LineDataSet lineDataSet = new LineDataSet(entries, "Pressure Max");

         lineDataSet.setColor(Color.rgb(221, 180, 171));
         lineDataSet.setValueTextColor(Color.rgb(180, 180, 180));

         lineDataSet.setCircleHoleColor(Color.WHITE);
         lineDataSet.setCircleColor(Color.WHITE);

         // Create LineData and set it to the LineChart
         LineData lineData = new LineData(lineDataSet);
         lineChart.setData(lineData);
         lineChart.invalidate(); // Refresh the chart

         // Set X-axis labels
         lineChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(labels));
     }*/
    private void updateGraphWithRecyclerViewData() {
        // Prepare data for the chart
        ArrayList<Entry> entries = new ArrayList<>();
        ArrayList<String> labels = new ArrayList<>();

        // Iterate through the pressureArrayList to get document names and PressureMax values
        for (int i = 0, j = co2ArrayList.size() - 1; i < co2ArrayList.size(); i++, j--) {
            CO2 co2 = co2ArrayList.get(j);
            // Use the index as X-axis value (for simplicity) and PressureMax as Y-axis value
            entries.add(new Entry(i, Float.parseFloat(co2.getCO2Max())));
            //labels.add(co2.getcurrentHour()); // Use currentHour for X-axis labels

            String shortenedLabel =co2.getcurrentHour().substring(0, Math.min(5, co2.getcurrentHour().length()));
            labels.add(shortenedLabel);
        }

        // Create a LineDataSet from the entries
        LineDataSet lineDataSet = new LineDataSet(entries, "CO2 Max");

        // Customize LineDataSet
        lineDataSet.setColor(Color.rgb(52, 152, 219)); // Line color
        lineDataSet.setValueTextColor(Color.rgb(255, 255, 255)); // Value text color
        lineDataSet.setCircleColor(Color.rgb(41, 128, 185)); // Circle color
        lineDataSet.setCircleHoleColor(Color.WHITE); // Circle hole color
        lineDataSet.setLineWidth(2f); // Line width
        lineDataSet.setCircleRadius(4f); // Circle radius
        lineDataSet.setValueTextSize(12f); // Value text size
        lineDataSet.setDrawValues(true); // Show value text

        // Create LineData and set it to the LineChart
        LineData lineData = new LineData(lineDataSet);
        lineChart.setData(lineData);

        // Customize X-axis
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
        xAxis.setTextColor(Color.rgb(255, 255, 255)); // X-axis label text color
        xAxis.setTextSize(12f); // X-axis label text size
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM); // X-axis position
        xAxis.setDrawGridLines(false); // Disable grid lines

        // Customize Y-axis (left)
        YAxis leftAxis = lineChart.getAxisLeft();
        leftAxis.setTextColor(Color.rgb(255, 255, 255)); // Y-axis label text color
        leftAxis.setTextSize(12f); // Y-axis label text size
        leftAxis.setDrawGridLines(true); // Enable grid lines

        // Disable Y-axis (right)
        YAxis rightAxis = lineChart.getAxisRight();
        rightAxis.setEnabled(false);

        // Customize chart description
        Description description = new Description();
        description.setText("CO2 Max over Time");
        description.setTextColor(Color.rgb(255, 255, 255)); // Description text color
        description.setTextSize(12f); // Description text size
        lineChart.setDescription(description);
        // Enable zoom and set initial zoom level to show only the last value
        lineChart.moveViewToX(entries.size() - 1);
        lineChart.setVisibleXRangeMaximum(12); // Show only 1 entry initially


        // Refresh the chart
        lineChart.invalidate();
    }







}





