package com.example.myapplication;

public class MonthClass {
    String currentmois;
    String Co2m;
    String humm;
    String prem;
    String tempm;

    public MonthClass() {
    }

    public MonthClass(String Co2m, String humm, String currentmois, String prem, String tempm) {

        this.Co2m = Co2m;
        this.humm = humm;
        this.currentmois= currentmois;
        this.prem = prem;
        this.tempm= tempm;


    }

    public String getcurrentmois() {
        return currentmois;
    }

    public String getCo2m() {
        return Co2m;
    }

    public String gethumm() {
        return humm;
    }
    public String getprem() {
        return prem;
    }

    public String gettempm() {
        return tempm;
    }
}
