package com.example.myapplication;

public class Pressure {
    String currentHour;
    String PressureMax;
    String PressureMin;

    public Pressure() {
    }

    public Pressure(String PressureMax, String PressureMin, String CurrentHour) {

        this.PressureMax = PressureMax;
        this.PressureMin = PressureMin;
        currentHour= CurrentHour;


    }

    public String getcurrentHour() {
        return currentHour;
    }

    public String getPressureMax() {
        return PressureMax;
    }

    public String getPressureMin() {
        return PressureMin;
    }
}