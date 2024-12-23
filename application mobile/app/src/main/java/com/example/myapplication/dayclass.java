package com.example.myapplication;

public class dayclass {
    String currentjour;
    String Co2j;
    String humj;
    String prej;
    String tempj;

    public dayclass() {
    }

    public dayclass(String Co2j, String humj, String currentjour, String prej, String tempj) {

        this.Co2j = Co2j;
        this.humj = humj;
        this.currentjour= currentjour;
        this.prej = prej;
        this.tempj= tempj;


    }

    public String getcurrentjour() {
        return currentjour;
    }

    public String getCo2j() {
        return Co2j;
    }

    public String gethumj() {
        return humj;
    }
    public String getprej() {
        return prej;
    }

    public String gettempj() {
        return tempj;
    }
}
