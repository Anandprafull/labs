
int leds[] = {5, 4, 0, 2}; // GPIO pins
int n = 4;

void setup() {
  for (int i = 0; i < n; i++) pinMode(leds[i], OUTPUT);
}

void loop() {
  for (int i = 0; i < n; i++) {
    digitalWrite(leds[i], LOW);
    delay(300);
    digitalWrite(leds[i], HIGH);
  }
}
