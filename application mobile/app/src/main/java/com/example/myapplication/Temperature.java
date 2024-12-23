package com.example.myapplication;

public class Temperature {
    String currentHour;
    String TemperatureMax;
    String TemperatureMin;

    public Temperature() {
    }

    public Temperature(String temperatureMax,String temperatureMin, String CurrentHour) {

        TemperatureMax = temperatureMax;
        TemperatureMin = temperatureMin;
        currentHour= CurrentHour;


    }

    public String getcurrentHour() {
        return currentHour;
    }

    public String getTemperatureMax() {
        return TemperatureMax;
    }

    public String getTemperatureMin() {
        return TemperatureMin;
    }
}
