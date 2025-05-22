
#include <ESP8266WiFi.h>

const char* ssid = "your_SSID";
const char* password = "your_PASSWORD";

WiFiServer server(80);
int ledPin = 2;

void setup() {
  pinMode(ledPin, OUTPUT);
  WiFi.begin(ssid, password);
  while (WiFi.status() != WL_CONNECTED) delay(1000);
  server.begin();
}

void loop() {
  WiFiClient client = server.available();
  if (client) {
    String req = client.readStringUntil('\r');
    if (req.indexOf("/ON") != -1) digitalWrite(ledPin, LOW);
    if (req.indexOf("/OFF") != -1) digitalWrite(ledPin, HIGH);

    client.print("HTTP/1.1 200 OK\r\nContent-Type: text/html\r\n\r\n");
    client.print("<a href=\"/ON\">ON</a><br><a href=\"/OFF\">OFF</a>");
    delay(1);
  }
}
