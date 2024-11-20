#include <WiFi.h>
#include <Firebase_ESP_Client.h>
#include <addons/TokenHelper.h>
#include <addons/RTDBHelper.h>
#include <WiFiUdp.h>
#include <NTPClient.h>
#include <stdio.h>
#include <string.h>

// Replace with your network credentials
const char* ssid = "Podium";
const char* password = "12345678";
WiFiUDP ntpUDP;
NTPClient timeClient(ntpUDP, "pool.ntp.org");

// Firebase project credentials
#define FIREBASE_HOST "https://stm32senddata-default-rtdb.firebaseio.com/"
#define API_KEY "AIzaSyAQoJkkPfzVMO41xRrP_3KPzBDhFrOWiNM"
#define FIRESTORE_PROJECT_ID "stm32senddata"
#define pass "12345678"
#define email1 "happy1@gmail.com"

#define RX 16
#define TX 17


String documentName = "first";
FirebaseData fbdo;
FirebaseConfig config;
FirebaseAuth auth;
bool signupOK = false;

// Variables to track min and max values
float TemperatureMin = 1000;
float TemperatureMax = 0;
float HumidityMin = 1000;
float HumidityMax = 0;
float PressureMin = 1100;
float PressureMax = 0;
float CO2Max=0;
float CO2Min=10000;
float Co2j=0,Co2m=0,Co2a=0;
float tempj=0,tempm=0,tempa=0;
float humj=0,humm=0,huma=0;
float prej=0,prem=0,prea=0;
int cofficientjour=1,cofficientmois=1,cofficientanne=1;

String lastHour = " ";
String lastjour= " ";
String lastmois= " ";
String lastanne= " ";

const char* monthNames[] = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};

void setup() {
  Serial.begin(115200); // Set to 115200 for a faster serial communication
  Serial2.begin(115200,SERIAL_8N1,RX,TX);//

  // Connect to WiFi
  WiFi.begin(ssid, password);
  Serial.print("Connecting to Wi-Fi");
  while (WiFi.status() != WL_CONNECTED) {
    Serial.print(".");
    delay(300);
  }
  Serial.println();
  Serial.print("Connected with IP: ");
  Serial.println(WiFi.localIP());
  Serial.println();

  // Initialize Firebase
  config.api_key = API_KEY;
  config.database_url = FIREBASE_HOST;
  auth.user.email = email1;
  auth.user.password = pass;

  // Increase the timeout to 30 seconds
  fbdo.setBSSLBufferSize(1024, 4096);
  fbdo.setResponseSize(4096);
  Firebase.RTDB.setReadTimeout(&fbdo, 1000 * 30);
  Firebase.RTDB.setwriteSizeLimit(&fbdo, "small");

  // Sign up
  if (Firebase.signUp(&config, &auth, "", "")) {
    Serial.println("Sign up successful.");
    signupOK = true;
  } else {
    Serial.printf("Sign up error: %s\n", config.signer.signupError.message.c_str());
  }

  // Start Firebase
  Firebase.begin(&config, &auth);
  Firebase.reconnectWiFi(true);
  
  // Start NTPClient
  timeClient.begin();
  timeClient.setTimeOffset(3600);
  //from
   timeClient.update();
  time_t rawT = timeClient.getEpochTime();
  struct tm* timeI;
  char buff[25];
  timeI = localtime(&rawT);

  // Format date and time as "YYYY-MM-DD_HH-MM-SS"
  strftime(buff, sizeof(buff), "%Y-%m-%d_%H-%M-%S", timeI);
  String currentDate = String(buff);
  lastHour = String(currentDate[14]) + String(currentDate[15]);
  lastjour= String(currentDate[8]) + String(currentDate[9]);
  lastmois= String(currentDate[5]) + String(currentDate[6]);
  lastanne= String(currentDate[2]) + String(currentDate[3]);
}

void loop() {
  // Update time
    if (Serial2.available()) {
    String message = Serial2.readString();
    Serial.println(message);
  int spaceIndex1 = message.indexOf(' ');
  
 String part1 = message.substring(0, spaceIndex1);
  
  String rest = message.substring(spaceIndex1 + 1);
  
  int spaceIndex2 = rest.indexOf(' ');
  
    String part2 = rest.substring(0, spaceIndex2);
  
  String rest2 = rest.substring(spaceIndex2 + 1);
  
  int spaceIndex3 = rest2.indexOf(' ');
  
  String part3 = rest2.substring(0, spaceIndex3);
  
  String part4 = rest2.substring(spaceIndex3 + 1);
  
  // Convertir les parties en float
  float Temperature = part1.toFloat();
  float Humidity = part2.toFloat();
  float Pressure = part3.toFloat();
  float CO2 = part4.toFloat();
  timeClient.update();
  time_t rawTime = timeClient.getEpochTime();
  struct tm* timeInfo;
  char buffer[25];
  timeInfo = localtime(&rawTime);

  // Format date and time as "YYYY-MM-DD_HH-MM-SS"
  strftime(buffer, sizeof(buffer), "%Y-%m-%d_%H-%M-%S", timeInfo);
  String currentDateTime = String(buffer);
  Serial.println(currentDateTime);

  // Extract minutes from currentDateTime
  String currentHour = String(currentDateTime[14]) + String(currentDateTime[15]);
  String currentHour1 = String(currentDateTime[11]) + String(currentDateTime[12]);
  String currentjour= String(currentDateTime[8]) + String(currentDateTime[9]);
  String currentmois= String(currentDateTime[5]) + String(currentDateTime[6]);
  String currentanne= String(currentDateTime[2]) + String(currentDateTime[3]);

  // Extract hours from currentDateTime
 // String currentHour = String(currentDateTime[11]) + String(currentDateTime[12]);
  
  FirebaseJson content;
  


  if (Temperature < TemperatureMin) TemperatureMin = Temperature;
  if (Temperature > TemperatureMax) TemperatureMax = Temperature;
  if (Humidity < HumidityMin) HumidityMin = Humidity;
  if (Humidity > HumidityMax) HumidityMax = Humidity;
  if (Pressure < PressureMin) PressureMin = Pressure;
  if (Pressure > PressureMax) PressureMax = Pressure;
  if (CO2 < CO2Min) CO2Min = CO2;
  if (CO2 > CO2Max) CO2Max = CO2;

  Co2j+=CO2;
  tempj+=Temperature;
  humj+=Humidity;
  prej+=Pressure;
  
  Co2m+=CO2;
  tempm+=Temperature;
  humm+=Humidity;
  prem+=Pressure;

  Co2a+=CO2;
  tempa+=Temperature;
  huma+=huma+Humidity;
  prea+=Pressure;


  Co2j = (float)Co2j / cofficientjour;
  tempj = (float)tempj / cofficientjour;
  humj =(float) humj / cofficientjour;
  prej =(float) prej / cofficientjour;

  Co2m =(float) Co2m / cofficientmois;
  tempm = (float)tempm / cofficientmois;
  humm =(float) humm / cofficientmois;
  prem =(float) prem / cofficientmois;

  Co2a =(float) Co2a / cofficientanne;
  tempa = (float)tempa / cofficientanne;
  huma =(float) huma / cofficientanne;
  prea =(float) prea / cofficientanne;


  
  cofficientmois++;
  cofficientanne++;
  cofficientjour++;
  

  if (currentHour != lastHour) {
    Serial.println("Sending data to Firebase hour");
    lastHour = currentHour; // Mettre à jour lastHour à l'heure actuelle
    documentName = currentDateTime;
    currentHour=currentHour1+":"+currentHour+" "+currentjour+"/"+currentmois+"/"+currentanne;
    String documentPath = "theSensors/" + documentName;

    // Set the 'Temperature' and 'Humidity' fields in the FirebaseJson object
    content.set("fields/TemperatureMin/stringValue", String(TemperatureMin, 2));
    content.set("fields/TemperatureMax/stringValue", String(TemperatureMax, 2));
    content.set("fields/HumidityMin/stringValue", String(HumidityMin, 2));
    content.set("fields/HumidityMax/stringValue", String(HumidityMax, 2));
    content.set("fields/PressureMin/stringValue", String(PressureMin, 2));
    content.set("fields/PressureMax/stringValue", String(PressureMax, 2)); 
    content.set("fields/CO2Min/stringValue", String(CO2Min, 2));
    content.set("fields/CO2Max/stringValue", String(CO2Max, 2)); 
    content.set("fields/currentHour/stringValue", currentHour);
    Serial.print("Update/Add Data... ");

    // Use the patch Document method to update the Temperature and Humidity Firestore document
    if (Firebase.Firestore.patchDocument(&fbdo, FIRESTORE_PROJECT_ID, "", documentPath.c_str(), content.raw(), "TemperatureMin,TemperatureMax,HumidityMin,HumidityMax,PressureMin,PressureMax,CO2Min,CO2Max,currentHour")) {
      Serial.printf("ok\n%s\n\n", fbdo.payload().c_str());
    } else {
      Serial.println("Failed to update Firestore document.");
      Serial.printf("Reason: %s\n", fbdo.errorReason().c_str());
    }
    TemperatureMin = 1000;
    TemperatureMax = 0;
    HumidityMin = 1000;
    HumidityMax = 0;
    PressureMin = 1100;
    PressureMax = 0;
    CO2Max=0;
    CO2Min=10000;
     if (currentjour != lastjour) {
    Serial.println("Sending data to Firebase day");
    lastjour = currentjour; // Mettre à jour lastHour à l'heure actuelle
     currentjour=currentjour+"/"+currentmois+"/"+currentanne;
     // Calculate daily averages

    documentName = currentDateTime;
    String documentPath = "theSensorsday/" + documentName;

    // Set the 'Temperature' and 'Humidity' fields in the FirebaseJson object
    content.set("fields/tempj/stringValue", String(tempj, 2));
    content.set("fields/humj/stringValue", String(humj, 2));
    content.set("fields/prej/stringValue", String(prej, 2));
    content.set("fields/Co2j/stringValue", String(Co2j, 2));
    content.set("fields/currentjour/stringValue", currentjour);
    Serial.print("Update/Add Data day... ");
    
    // Use the patch Document method to update the Temperature and Humidity Firestore document
    if (Firebase.Firestore.patchDocument(&fbdo, FIRESTORE_PROJECT_ID, "", documentPath.c_str(), content.raw(), "tempj,humj,prej,Co2j,currentjour,")) {
      Serial.printf("ok\n%s\n\n", fbdo.payload().c_str());
    } else {
      Serial.println("Failed to update Firestore document.");
      Serial.printf("Reason: %s\n", fbdo.errorReason().c_str());
    }

    currentjour= String(currentDateTime[8]) + String(currentDateTime[9]);
    Co2j=0;
    tempj=0;
    humj=0;
    prej=0;
    cofficientjour=1;
    delay(1000);


    if (currentmois != lastmois) {
    Serial.println("Sending data to Firebase month");
    lastmois = currentmois; // Mettre à jour lastHour à l'heure actuelle
    int monthIndex = (String(currentDateTime[5]) + String(currentDateTime[6])).toInt() - 1;
    currentmois = monthNames[monthIndex];


    documentName = currentDateTime;
    String documentPath = "theSensorsmonth/" + documentName;

    // Set the 'Temperature' and 'Humidity' fields in the FirebaseJson object
    content.set("fields/Co2m/stringValue", String(Co2m, 2));
    content.set("fields/tempm/stringValue", String(tempm, 2));
    content.set("fields/humm/stringValue", String(humm, 2));
    content.set("fields/prem/stringValue", String(prem, 2));
    content.set("fields/currentmois/stringValue", currentmois);
    Serial.print("Update/Add Data day... ");
    

    // Use the patch Document method to update the Temperature and Humidity Firestore document
    if (Firebase.Firestore.patchDocument(&fbdo, FIRESTORE_PROJECT_ID, "", documentPath.c_str(), content.raw(), "Co2m,tempm,humm,prem,currentmois,")) {
      Serial.printf("ok\n%s\n\n", fbdo.payload().c_str());
    } else {
      Serial.println("Failed to update Firestore document.");
      Serial.printf("Reason: %s\n", fbdo.errorReason().c_str());
    }

    Co2m=0;
    tempm=0;
    humm=0;
    prem=0;
    cofficientmois=1;
    
    delay(1000);
    if (currentanne != lastanne) {
    Serial.println("Sending data to Firebase year");
    lastanne = currentanne; // Mettre à jour lastHour à l'heure actuelle
    documentName = currentDateTime;
    currentanne="20"+currentanne;


    String documentPath = "theSensorsyear/" + documentName;

    // Set the 'Temperature' and 'Humidity' fields in the FirebaseJson object
    content.set("fields/Co2a/stringValue", String(Co2a, 2));
    content.set("fields/tempa/stringValue", String(tempa, 2));
    content.set("fields/huma/stringValue", String(huma, 2));
    content.set("fields/prea/stringValue", String(prea, 2));
    content.set("fields/currentanne/stringValue", currentanne);
    Serial.print("Update/Add Data day... ");
    

    // Use the patch Document method to update the Temperature and Humidity Firestore document
    if (Firebase.Firestore.patchDocument(&fbdo, FIRESTORE_PROJECT_ID, "", documentPath.c_str(), content.raw(), "Co2a,tempa,huma,prea,currentanne,")) {
      Serial.printf("ok\n%s\n\n", fbdo.payload().c_str());
    } else {
      Serial.println("Failed to update Firestore document.");
      Serial.printf("Reason: %s\n", fbdo.errorReason().c_str());
    }
    Co2a=0;
    tempa=0;
    huma=0;
    prea=0;
    cofficientanne=1;
    delay(1000);
    }
    }
    }


    delay(1000); // Send data every 1 seconds
  }
  else{
    Serial.println("Sending data to Firebase by second");
    content.set("fields/Temperature/stringValue", String(Temperature, 2));
    content.set("fields/Humidity/stringValue", String(Humidity, 2));
    content.set("fields/Pressure/stringValue", String(Pressure, 2));
    content.set("fields/CO2/stringValue", String(CO2, 2));

    Serial.print("Update/Add Data... ");
    String documentPath = "theSensor/Real_Time";

    if (Firebase.Firestore.patchDocument(&fbdo, FIRESTORE_PROJECT_ID, "", documentPath.c_str(), content.raw(), "Temperature,Humidity,Pressure,CO2,")) {
      Serial.printf("ok\n%s\n\n", fbdo.payload().c_str());
    } else {
      Serial.println("Failed to update Firestore document.");
      Serial.printf("Reason: %s\n", fbdo.errorReason().c_str());
    }
  }

  delay(1000); // Wait 1 seconds before checking time again
}else {
    Serial.println("No data available");
  }
   delay(5000);
}
