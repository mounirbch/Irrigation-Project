#include "DHT.h"
#include <ArduinoJson.h>
#include <WiFi.h>
#include <HTTPClient.h>

#define DHTPIN 15
#define DHTTYPE DHT22
#define AOUT_PIN 32
#define Send_interval 1000
DHT dht(DHTPIN, DHTTYPE);

unsigned long lastDHTReadingTime = 0;
unsigned long lastReadingTime = 0;
const char *ssid = "globalnet";
const char *password = "changeme";
const char *serverUrl = "http://192.168.1.4:8082/api/iot";

const int pinRelay = 21;
char data = -1;
String Server = "192.168.1.4";
const int port = 8082;
const char* endpoint = "/calendar/tempsDirrigation"; 
unsigned long lastRequestTime = 0;
const unsigned long requestInterval = 6000;

float sumTemp = 0; 
float sumHumidity = 0;  
int dhtcount = 0;   
float sumsol= 0;
int solcount= 0;

void setupWiFi() {
  WiFi.begin(ssid, password);
  while (WiFi.status() != WL_CONNECTED) {
    delay(1000);
    Serial.println("Connexion au réseau Wi-Fi...");
  }
  Serial.println("Connecté au réseau Wi-Fi !");
}

void readSensors() {
  float h = dht.readHumidity();
  float t = dht.readTemperature();
  int soilMoisture = analogRead(AOUT_PIN);
  float soilHumidity = 100 - soilMoisture * 100 / 4096;

  if (!isnan(h) && !isnan(t)) {
    sumTemp += t;
    sumHumidity += h;
    sumsol += soilHumidity;
    dhtcount++;
    solcount++;
  }
}


void calculateAverages(JsonDocument &doc) {
  if (dhtcount > 0) {
    float averageTemp = sumTemp / dhtcount;
    float averageHumidity = sumHumidity / dhtcount;
    doc["temperature"] = averageTemp;
    doc["humidity"] = averageHumidity;
  } else {
    Serial.println("Aucune donnée valide pendant la minute.");
  }

  if (solcount > 0) {
    float averagesol = sumsol / solcount;
    doc["soilHumidity"] = averagesol;
  } else {
    Serial.println("Aucune donnée valide pendant la minute.");
  }

  doc["ph"] = 0;
}


void sendDataToServer(JsonDocument &doc) {
  String jsonString;
  serializeJson(doc, jsonString);

  Serial.println("Données moyennes :");
  Serial.println(jsonString);

  HTTPClient http;
  http.begin(serverUrl);
  http.addHeader("Content-Type", "application/json");
  int httpResponseCode = http.POST(jsonString);

  if (httpResponseCode > 0) {
    String response = http.getString();
    Serial.print("Réponse du serveur : ");
    Serial.println(response);
  } else {
    Serial.print("Échec de la requête HTTP : ");
    Serial.println(httpResponseCode);
  }

  http.end();
}


void setup() {
  Serial.begin(115200);
  dht.begin();
  setupWiFi();
  pinMode(pinRelay, OUTPUT);
}

void loop() {
  unsigned long currentTime = millis();
  


  if (currentTime - lastDHTReadingTime >= 60000) {
    lastDHTReadingTime = currentTime;
    StaticJsonDocument<200> doc;
    calculateAverages(doc);
    sendDataToServer(doc);
    sumTemp = 0;
    sumHumidity = 0;
    dhtcount = 0;
    solcount = 0;
    sumsol = 0;
  }

  readSensors();

  int currentTime1 = millis();
  if (currentTime1 - lastRequestTime >= requestInterval) {
    lastRequestTime = currentTime1;
    fetchDataFromServer();
  }
  delay(50); 



}
void fetchDataFromServer() {
  HTTPClient http;
  
  String url = "http://" + Server + ":" + String(port) + endpoint;
  
  
  http.begin(url);
  int httpCode = http.GET();
  
  if (httpCode > 0) {
    if (httpCode == HTTP_CODE_OK) {
      String payload = http.getString();
      if (payload.length() > 0) {
        data = payload[0];
        Serial.println("Données reçues : " + payload);
        controlRelay();
      } else {
        Serial.println("Aucune donnée reçue.");
      }
    } else {
      Serial.printf("Code d'erreur HTTP : %d\n", httpCode);
    }
  } else {
    Serial.println("Échec de la connexion au serveur.");
  }
  http.end();
}

void controlRelay() {
  Serial.print("Donnée de contrôle du relais : ");
  if (data == '1') {
    Serial.println("HIGH");
    digitalWrite(pinRelay, HIGH);
  } else {
    Serial.println("LOW");
    digitalWrite(pinRelay, LOW);
  }
}