
int lm35 = A0;

void setup() {
  Serial.begin(9600);
}

void loop() {
  int value = analogRead(lm35);
  float voltage = value * 5.0 / 1024;
  float temperature = voltage * 100;
  Serial.print("Temperature: ");
  Serial.println(temperature);
  delay(1000);
}
