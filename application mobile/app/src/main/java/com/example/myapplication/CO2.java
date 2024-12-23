package com.example.myapplication;

public class CO2 {


        String currentHour;
        String CO2Max;
        String CO2Min;

        public CO2() {
        }

        public CO2(String CO2Max, String CO2Min, String CurrentHour) {

            this.CO2Max = CO2Max;
            this.CO2Min = CO2Min;
            currentHour= CurrentHour;


        }

        public String getcurrentHour() {
            return currentHour;
        }

        public String getCO2Max() {
            return CO2Max;
        }

        public String getCO2Min() {
            return CO2Min;
        }
    }

