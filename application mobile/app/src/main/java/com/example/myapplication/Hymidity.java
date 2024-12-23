package com.example.myapplication;

public class Hymidity {
    String currentHour;
    String HumidityMax;
    String HumidityMin;

public Hymidity() {
        }

public Hymidity(String humidityMax, String humidityMin, String CurrentHour) {

    this.HumidityMax = humidityMax;
    this.HumidityMin = humidityMin;
    currentHour= CurrentHour;


        }

public String getcurrentHour() {
        return currentHour;
        }

public String getHumidityMax() {
        return HumidityMax;
        }

public String getHumidityMin() {
        return HumidityMin;
        }
        }