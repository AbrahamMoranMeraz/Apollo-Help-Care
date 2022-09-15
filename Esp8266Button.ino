#include <ESP8266WiFi.h>
#include <WiFiClientSecure.h> 
#include <ESP8266HTTPClient.h>

// Conect to wifi using the credentiales
const char* ssid = "Mitelefono";
const char* password = "pikachu98";
// Enpoints to open and aware statuts of patient
const char* host = "https://csm-2022.ny-2.paas.massivegrid.net/hackaton/webresources/com.mim.alerta/switch/12/1";
const char* host2 = "https://csm-2022.ny-2.paas.massivegrid.net/hackaton/webresources/com.mim.alerta/switch/12/0";
// Constant to usen in https request
const int httpsPort = 443;
// Variable to know status of button
boolean isActive = false;
// Variable to Pin in Esp8266
const int buttonPin = 2;
// Variable to save current state input by the fisical button
int currentState;

void setup() {
  // Put your setup code here, to run once:
  Serial.begin(115200);
  // Input button
  pinMode(buttonPin, INPUT);
  // Inicialit wifi parametros
  WiFi.begin(ssid, password);
  // Aware until connections
  Serial.println("Connecting");
  while(WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.print(".");
  }
  Serial.println("");
  Serial.print("Connected to WiFi network with IP Address: ");
  Serial.println(WiFi.localIP());
  Serial.println("Init of events of button");

}

void loop() {
  // Save values of button HIGH or LOW
  currentState = digitalRead(buttonPin);
  // Condition to specific a new open incident for a patient
  if(currentState == HIGH && isActive == false){
  Serial.println("The patient's button was pushed");
  // Instance to use https protocol
  WiFiClientSecure httpsClient;
  // Print serial por of https used
  Serial.println(host);
  // Disable to insecure entreys
  httpsClient.setInsecure();
  httpsClient.setTimeout(15000); // 15 Seconds
  delay(1000);
  // Connect to https
  Serial.print("HTTPS Connecting");
  int r=0; //retry counter
  // Time to conect correctly, depending wifi used what take a bit a time.
  while((!httpsClient.connect(host, httpsPort)) && (r < 50)){
      delay(100);
      Serial.print(".");
      r++;
  }
  if(r==30) {
    Serial.println("Connection failed");
  }
  else {
    // When is correct the conection
    Serial.println("Connected to web");
    if (!isActive){
      HTTPClient http;
      // Init post
      http.begin(httpsClient,host);
      int httpResponseCode = http.POST("");

      // Si es mayor a 0 el response
      if (httpResponseCode == 200) {
        Serial.print("HTTP Response code: ");
        Serial.println(httpResponseCode);
        String payload = http.getString();
        Serial.println(payload);
        isActive = true;
        http.end();
      }
      // Error al realizar request post
      else {
        Serial.print("Error code: ");
        Serial.println(httpResponseCode);
      }
      }
  }
  }
  // when the button is down is needed up it
  if(currentState == LOW && isActive == true){
  Serial.println("Suelta");
  WiFiClientSecure httpsClient;
  Serial.println(host2);
  httpsClient.setInsecure();
  httpsClient.setTimeout(15000); // 15 Seconds
  delay(1000);

  Serial.print("HTTPS Connecting");
  int r=0; //retry counter
  while((!httpsClient.connect(host2, httpsPort)) && (r < 50)){
      delay(100);
      Serial.print(".");
      r++;
  }
  if(r==30) {
    Serial.println("Connection failed");
  }
  else {
    Serial.println("Connected to web");
    if (isActive){
      HTTPClient http;
      http.begin(httpsClient,host2);
      int httpResponseCode = http.POST("");

      // Si es mayor a 0 el response
      if (httpResponseCode == 200) {
        Serial.print("HTTP Response code: ");
        Serial.println(httpResponseCode);
        String payload = http.getString();
        Serial.println(payload);
        isActive = false;
        http.end();
      }
      // Error al realizar request post
      else {
        Serial.print("Error code: ");
        Serial.println(httpResponseCode);
      }
      }
  }
  }
  
}
