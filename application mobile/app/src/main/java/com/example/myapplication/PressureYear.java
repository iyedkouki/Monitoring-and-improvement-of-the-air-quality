package com.example.myapplication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
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

public class PressureYear extends AppCompatActivity {
    RecyclerView recyclerView;
    ArrayList<YearClass> yearClasses;
    PressureYearAdapter humidityYearAdapter;
    TextView buttond,buttonm;

    FirebaseFirestore db;
    ImageView retourne;
    LineChart lineChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pressure_year);
        recyclerView=findViewById(R.id.recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(PressureYear.this));
        lineChart=findViewById(R.id.lineChart);
        retourne=findViewById(R.id.returne_);
        retourne.setOnClickListener(view -> startActivity(new Intent(PressureYear.this, Pressure_main.class)));
        db= FirebaseFirestore.getInstance();
        yearClasses =  new ArrayList<YearClass>();
        humidityYearAdapter =new PressureYearAdapter(PressureYear.this, yearClasses);
        recyclerView.setAdapter(humidityYearAdapter);
        EvenChangelister();


        buttond=findViewById(R.id.button);
        buttond.setOnClickListener(view -> startActivity(new Intent(PressureYear.this, PressureDay.class)));
        buttonm=findViewById(R.id.buttonm);
        buttonm.setOnClickListener(view -> startActivity(new Intent(PressureYear.this, PressureMonth.class)));



    }

    private void EvenChangelister() {
        db.collection("theSensorsyear").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                if(error!= null){

                    Log.e("Firebase error",error.getMessage());
                    return;
                }
                for(DocumentChange dc: value.getDocumentChanges()){
                    if(dc.getType()==DocumentChange.Type.ADDED){
                        yearClasses.add(0, dc.getDocument().toObject(YearClass.class)); // Add to the beginning of the list
                    }

                    humidityYearAdapter.notifyDataSetChanged();

                }updateGraphWithRecyclerViewData();
            }
        });
    }
    /*   private void updateGraphWithRecyclerViewData() {
           // Prepare data for the chart
           ArrayList<Entry> entries = new ArrayList<>();
           ArrayList<String> labels = new ArrayList<>();


           // Iterate through the pressureArrayList to get document names and PressureMax values
           for (int i = 0,j=hymidityArrayList.size()-1; i < hymidityArrayList.size(); i++,j--) {
               Hymidity hymidity = hymidityArrayList.get(j);
               // Use the index as X-axis value (for simplicity) and PressureMax as Y-axis value
               entries.add(new Entry(i, Float.parseFloat(hymidity.getHumidityMax())));
               labels.add(hymidity.getcurrentHour()); // Use currentHour for X-axis labels
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
        for (int i = 0, j = yearClasses.size() - 1; i < yearClasses.size(); i++, j--) {
            YearClass yearClass = yearClasses.get(j);
            // Use the index as X-axis value (for simplicity) and PressureMax as Y-axis value
            entries.add(new Entry(i, Float.parseFloat(yearClass.getprea())));
            labels.add(yearClass.getcurrentanne()); // Use currentHour for X-axis labels
        }

        // Create a LineDataSet from the entries
        LineDataSet lineDataSet = new LineDataSet(entries, "Pressure Avg");

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
        description.setText("Pressure Avg over Time");
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