package com.example.myapplication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.data.Entry;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class AllclassYear extends AppCompatActivity {
    RecyclerView recyclerView;
    ArrayList<YearClass> yearClasses;
    AllclassAdapter multiBarAdapter;
    ImageView retourne;
    FirebaseFirestore db;
    BarChart barChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_allclass_year);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        barChart = findViewById(R.id.barChart);

        db = FirebaseFirestore.getInstance();
        yearClasses = new ArrayList<>();
        multiBarAdapter = new AllclassAdapter(this, yearClasses);
        recyclerView.setAdapter(multiBarAdapter);

        fetchDataAndUpdateUI();
        retourne=findViewById(R.id.returne_);
        retourne.setOnClickListener(view -> startActivity(new Intent(AllclassYear.this, MainActivity.class)));

    }

    private void fetchDataAndUpdateUI() {
        db.collection("theSensorsyear").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.e("Firebase error", error.getMessage());
                    return;
                }

                for (DocumentChange dc : value.getDocumentChanges()) {
                    if (dc.getType() == DocumentChange.Type.ADDED) {
                        yearClasses.add(0, dc.getDocument().toObject(YearClass.class));
                    }
                }

                multiBarAdapter.notifyDataSetChanged();
                updateBarChart();
            }
        });

    }
    private void updateBarChart() {
        ArrayList<BarEntry> co2Entries = new ArrayList<>();
        ArrayList<BarEntry> tempEntries = new ArrayList<>();
        ArrayList<BarEntry> humidityEntries = new ArrayList<>();
        ArrayList<BarEntry> pressureEntries = new ArrayList<>();
        ArrayList<String> labels = new ArrayList<>();

        // Add data and labels
        for (int i = 0, j = yearClasses.size() - 1; i < yearClasses.size(); i++, j--) {
            YearClass yearClass = yearClasses.get(j);

            co2Entries.add(new BarEntry(i, Float.parseFloat(yearClass.getCo2a())));
            tempEntries.add(new BarEntry(i, Float.parseFloat(yearClass.gettempa())));
            humidityEntries.add(new BarEntry(i, Float.parseFloat(yearClass.gethuma())));
            pressureEntries.add(new BarEntry(i, Float.parseFloat(yearClass.getprea())));

            labels.add(yearClass.getcurrentanne());
        }

        // Create BarDataSets
        BarDataSet co2DataSet = new BarDataSet(co2Entries, "CO2");
        BarDataSet tempDataSet = new BarDataSet(tempEntries, "Temperature");
        BarDataSet humidityDataSet = new BarDataSet(humidityEntries, "Humidity");
        BarDataSet pressureDataSet = new BarDataSet(pressureEntries, "Pressure");

        // Set colors for bars
        co2DataSet.setColor(Color.rgb(216, 67, 21));
        tempDataSet.setColor(Color.rgb(255, 249, 196));
        humidityDataSet.setColor(Color.rgb(47, 189, 133));
        pressureDataSet.setColor(Color.rgb(52, 152, 219));

        // Set value text color to white
        co2DataSet.setValueTextColor(Color.WHITE);
        tempDataSet.setValueTextColor(Color.WHITE);
        humidityDataSet.setValueTextColor(Color.WHITE);
        pressureDataSet.setValueTextColor(Color.WHITE);

        // Create BarData and set bar width
        BarData barData = new BarData(co2DataSet, tempDataSet, humidityDataSet, pressureDataSet);
        barData.setBarWidth(0.15f);  // Set a narrower bar width for better spacing

        // Set up proper group spacing
        float groupSpace = 0.4f;    // Space between groups
        float barSpace = 0.05f;     // Space between bars in a group
        float barWidth = 0.15f;     // Bar width (should be set to the same value used in setBarWidth)

        // Set the data to the chart
        barChart.setData(barData);

        // Adjust X-axis range to ensure all bars are visible
        float startYear = 0f;
        float endYear = labels.size();  // Ensure we account for all years
        barChart.getXAxis().setAxisMinimum(startYear);
        barChart.getXAxis().setAxisMaximum(startYear + barData.getGroupWidth(groupSpace, barSpace) * labels.size());  // Adjust axis maximum
        barChart.groupBars(startYear, groupSpace, barSpace); // Group bars with adjusted spacing

        // Customize X-axis
        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
        xAxis.setGranularity(1f);
        xAxis.setCenterAxisLabels(true);  // Center the labels between groups
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextColor(Color.rgb(255, 255, 255)); // X-axis label text color
        xAxis.setTextSize(12f); // X-axis label text size

        // Customize Y-axis (left)
        YAxis leftAxis = barChart.getAxisLeft();
        leftAxis.setGranularity(1f);
        leftAxis.setTextColor(Color.rgb(255, 255, 255)); // Y-axis label text color
        leftAxis.setTextSize(12f); // Y-axis label text size
        leftAxis.setDrawGridLines(true); // Enable grid lines

        // Disable Y-axis (right)
        YAxis rightAxis = barChart.getAxisRight();
        rightAxis.setEnabled(false);

        // Customize legend
        Legend legend = barChart.getLegend();
        legend.setTextColor(Color.WHITE); // Set the legend text color to white

        // Show all the data initially
        barChart.setVisibleXRangeMaximum(labels.size()); // Ensure all years are visible
        barChart.moveViewToX(0); // Move view to the first year

        // Refresh the chart
        barChart.invalidate();
    }

    /*
    private void updateBarChart() {
        ArrayList<BarEntry> co2Entries = new ArrayList<>();
        ArrayList<BarEntry> tempEntries = new ArrayList<>();
        ArrayList<BarEntry> humidityEntries = new ArrayList<>();
        ArrayList<BarEntry> pressureEntries = new ArrayList<>();
        ArrayList<String> labels = new ArrayList<>();

        // Add data and labels
        for (int i = 0, j = yearClasses.size() - 1; i < yearClasses.size(); i++, j--) {
            YearClass yearClass = yearClasses.get(j);

            co2Entries.add(new BarEntry(i, Float.parseFloat(yearClass.getCo2a())));
            tempEntries.add(new BarEntry(i, Float.parseFloat(yearClass.gettempa())));
            humidityEntries.add(new BarEntry(i, Float.parseFloat(yearClass.gethuma())));
            pressureEntries.add(new BarEntry(i, Float.parseFloat(yearClass.getprea())));

            labels.add(yearClass.getcurrentanne());
        }

        // Create BarDataSets
        BarDataSet co2DataSet = new BarDataSet(co2Entries, "CO2");
        BarDataSet tempDataSet = new BarDataSet(tempEntries, "Temperature");
        BarDataSet humidityDataSet = new BarDataSet(humidityEntries, "Humidity");
        BarDataSet pressureDataSet = new BarDataSet(pressureEntries, "Pressure");

        // Set colors
        co2DataSet.setColor(Color.RED);
        tempDataSet.setColor(Color.BLUE);
        humidityDataSet.setColor(Color.GREEN);
        pressureDataSet.setColor(Color.rgb(52, 152, 219));

        // Set value text colors to white
        co2DataSet.setValueTextColor(Color.WHITE);
        tempDataSet.setValueTextColor(Color.WHITE);
        humidityDataSet.setValueTextColor(Color.WHITE);
        pressureDataSet.setValueTextColor(Color.WHITE);



        // Create BarData and set bar width
        BarData barData = new BarData(co2DataSet, tempDataSet, humidityDataSet, pressureDataSet);
        barData.setBarWidth(0.15f);  // Set a narrower bar width for better spacing

        // Set group space and bar space
        float groupSpace = 0.4f;    // Space between groups
        float barSpace = 0.05f;     // Space between bars in a group
        float barWidth = 0.15f;     // Bar width (should be set to the same value used in setBarWidth)

        // Set the data to the chart
        barChart.setData(barData);

        // Adjust X-axis range to ensure all bars are visible
        float startYear = 0f;
        float endYear = labels.size();  // Ensure we account for all years
        barChart.getXAxis().setAxisMinimum(startYear);
        barChart.getXAxis().setAxisMaximum(startYear + barData.getGroupWidth(groupSpace, barSpace) * labels.size());  // Adjust axis maximum
        barChart.groupBars(startYear, groupSpace, barSpace); // Group bars with adjusted spacing

        // Customize X-axis
        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
        xAxis.setGranularity(1f);
        xAxis.setCenterAxisLabels(true);  // Center the labels between groups
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextColor(Color.rgb(255, 255, 255)); // X-axis label text color
        xAxis.setTextSize(12f); // X-axis label text size

        // Customize Y-axis (left)
        YAxis leftAxis = barChart.getAxisLeft();
        leftAxis.setGranularity(1f);
        leftAxis.setTextColor(Color.rgb(255, 255, 255)); // Y-axis label text color
        leftAxis.setTextSize(12f); // Y-axis label text size
        leftAxis.setDrawGridLines(true); // Enable grid lines

        // Disable Y-axis (right)
        YAxis rightAxis = barChart.getAxisRight();
        rightAxis.setEnabled(false);

        // Show all the data initially
        barChart.setVisibleXRangeMaximum(labels.size()); // Ensure all years are visible
        barChart.moveViewToX(0); // Move view to the first year

        // Refresh the chart
        barChart.invalidate();
    }



/*
    private void updateBarChart() {
        ArrayList<BarEntry> co2Entries = new ArrayList<>();
        ArrayList<BarEntry> tempEntries = new ArrayList<>();
        ArrayList<BarEntry> humidityEntries = new ArrayList<>();
        ArrayList<BarEntry> pressureEntries = new ArrayList<>();
        ArrayList<String> labels = new ArrayList<>();

        for (int i = 0, j = yearClasses.size() - 1; i < yearClasses.size(); i++, j--) {
            YearClass yearClass = yearClasses.get(j);

            co2Entries.add(new BarEntry(i, Float.parseFloat(yearClass.getCo2a())));
            tempEntries.add(new BarEntry(i, Float.parseFloat(yearClass.gettempa())));
            humidityEntries.add(new BarEntry(i, Float.parseFloat(yearClass.gethuma())));
            pressureEntries.add(new BarEntry(i, Float.parseFloat(yearClass.getprea())));

            labels.add(yearClass.getcurrentanne());
        }

        BarDataSet co2DataSet = new BarDataSet(co2Entries, "CO2");
        BarDataSet tempDataSet = new BarDataSet(tempEntries, "Temperature");
        BarDataSet humidityDataSet = new BarDataSet(humidityEntries, "Humidity");
        BarDataSet pressureDataSet = new BarDataSet(pressureEntries, "Pressure");

        co2DataSet.setColor(Color.RED);
        tempDataSet.setColor(Color.BLUE);
        humidityDataSet.setColor(Color.GREEN);
        pressureDataSet.setColor(Color.YELLOW);

        BarData barData = new BarData(co2DataSet, tempDataSet, humidityDataSet, pressureDataSet);
        barData.setBarWidth(0.2f);  // Set the bar width

        // Set up proper group spacing
        float groupSpace = 0.4f;    // Space between groups
        float barSpace = 0.02f;     // Space between bars in a group

        // Ensure that we have enough space to group bars correctly
        barChart.setData(barData);
        barChart.getXAxis().setAxisMinimum(0f);
        barChart.getXAxis().setAxisMaximum(labels.size());
        barChart.groupBars(0f, groupSpace, barSpace); // Group bars properly

        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
        xAxis.setGranularity(1f);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextColor(Color.rgb(255, 255, 255)); // X-axis label text color
        xAxis.setTextSize(12f); // X-axis label text size

        YAxis leftAxis = barChart.getAxisLeft();
        leftAxis.setGranularity(1f);
        leftAxis.setTextColor(Color.rgb(255, 255, 255)); // Y-axis label text color
        leftAxis.setTextSize(12f); // Y-axis label text size
        leftAxis.setDrawGridLines(true); // Enable grid lines

        YAxis rightAxis = barChart.getAxisRight();
        rightAxis.setEnabled(false); // Disable the right Y-axis

        barChart.invalidate(); // Refresh the chart
    }

    /*
    private void updateBarChart() {
        ArrayList<BarEntry> co2Entries = new ArrayList<>();
        ArrayList<BarEntry> tempEntries = new ArrayList<>();
        ArrayList<BarEntry> humidityEntries = new ArrayList<>();
        ArrayList<BarEntry> pressureEntries = new ArrayList<>();
        ArrayList<String> labels = new ArrayList<>();

        for (int i = 0, j = yearClasses.size() - 1; i < yearClasses.size(); i++, j--) {
            YearClass yearClass = yearClasses.get(j);

            co2Entries.add(new BarEntry(i, Float.parseFloat(yearClass.getCo2a())));
            tempEntries.add(new BarEntry(i, Float.parseFloat(yearClass.gettempa())));
            humidityEntries.add(new BarEntry(i, Float.parseFloat(yearClass.gethuma())));
            pressureEntries.add(new BarEntry(i, Float.parseFloat(yearClass.getprea())));

            labels.add(yearClass.getcurrentanne());
        }

        BarDataSet co2DataSet = new BarDataSet(co2Entries, "CO2");
        BarDataSet tempDataSet = new BarDataSet(tempEntries, "Temperature");
        BarDataSet humidityDataSet = new BarDataSet(humidityEntries, "Humidity");
        BarDataSet pressureDataSet = new BarDataSet(pressureEntries, "Pressure");

        co2DataSet.setColor(Color.RED);
        tempDataSet.setColor(Color.BLUE);
        humidityDataSet.setColor(Color.GREEN);
        pressureDataSet.setColor(Color.YELLOW);

        BarData barData = new BarData(co2DataSet, tempDataSet, humidityDataSet, pressureDataSet);
        barData.setBarWidth(0.2f);

        barChart.setData(barData);
        barChart.groupBars(0f, 0.4f, 0.02f);

        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
        xAxis.setGranularity(1f);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextColor(Color.rgb(255, 255, 255)); // X-axis label text color
        xAxis.setTextSize(12f); // X-axis label text size

        YAxis leftAxis = barChart.getAxisLeft();
        leftAxis.setGranularity(1f);
        leftAxis.setTextColor(Color.rgb(255, 255, 255)); // Y-axis label text color
        leftAxis.setTextSize(12f); // Y-axis label text size
        leftAxis.setDrawGridLines(true); // Enable grid lines

        YAxis rightAxis = barChart.getAxisRight();
        rightAxis.setEnabled(false); // Disable the right Y-axis

        barChart.invalidate(); // Refresh the chart
    }


    private void updateBarChart() {
        ArrayList<BarEntry> co2Entries = new ArrayList<>();
        ArrayList<BarEntry> tempEntries = new ArrayList<>();
        ArrayList<BarEntry> humidityEntries = new ArrayList<>();
        ArrayList<BarEntry> pressureEntries = new ArrayList<>();
        ArrayList<String> labels = new ArrayList<>();

        for (int i = 0; i < yearClasses.size(); i++) {
            YearClass yearClass = yearClasses.get(i);

            co2Entries.add(new BarEntry(i, Float.parseFloat(yearClass.getCo2a())));
            tempEntries.add(new BarEntry(i, Float.parseFloat(yearClass.gettempa())));
            humidityEntries.add(new BarEntry(i, Float.parseFloat(yearClass.gethuma())));
            pressureEntries.add(new BarEntry(i, Float.parseFloat(yearClass.getprea())));

            labels.add(yearClass.getcurrentanne());
        }

        BarDataSet co2DataSet = new BarDataSet(co2Entries, "CO2");
        BarDataSet tempDataSet = new BarDataSet(tempEntries, "Temperature");
        BarDataSet humidityDataSet = new BarDataSet(humidityEntries, "Humidity");
        BarDataSet pressureDataSet = new BarDataSet(pressureEntries, "Pressure");

        co2DataSet.setColor(Color.RED);
        tempDataSet.setColor(Color.BLUE);
        humidityDataSet.setColor(Color.GREEN);
        pressureDataSet.setColor(Color.YELLOW);

        BarData barData = new BarData(co2DataSet, tempDataSet, humidityDataSet, pressureDataSet);
        barData.setBarWidth(0.2f);

        barChart.setData(barData);
        barChart.groupBars(0f, 0.4f, 0.02f);

        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
        xAxis.setGranularity(1f);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        YAxis leftAxis = barChart.getAxisLeft();
        leftAxis.setGranularity(1f);

        YAxis rightAxis = barChart.getAxisRight();
        rightAxis.setEnabled(false);

        barChart.invalidate();
    }*/
}
