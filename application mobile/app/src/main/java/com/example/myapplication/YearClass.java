package com.example.myapplication;

public class YearClass {
    String currentanne;
    String Co2a;
    String huma;
    String prea;
    String tempa;

    public YearClass() {
    }

    public YearClass(String Co2j, String humj, String currentjour, String prej, String tempj) {

        this.Co2a = Co2j;
        this.huma = humj;
        this.currentanne= currentjour;
        this.prea = prej;
        this.tempa= tempj;


    }

    public String getcurrentanne() {
        return currentanne;
    }

    public String getCo2a() {
        return Co2a;
    }

    public String gethuma() {
        return huma;
    }
    public String getprea() {
        return prea;
    }

    public String gettempa() {
        return tempa;
    }
}
