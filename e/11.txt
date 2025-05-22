
#include <DHT.h>
#define DHTPIN 2
#define DHTTYPE DHT11

DHT dht(DHTPIN, DHTTYPE);

void setup() {
  Serial.begin(115200);
  dht.begin();
}

void loop() {
  float t = dht.readTemperature();
  float h = dht.readHumidity();
  Serial.print("Temp: ");
  Serial.print(t);
  Serial.print(" C, Humidity: ");
  Serial.println(h);
  delay(2000);
}
