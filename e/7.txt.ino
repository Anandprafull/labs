
int pir = 2;
int led = 13;

void setup() {
  pinMode(pir, INPUT);
  pinMode(led, OUTPUT);
  Serial.begin(9600);
}

void loop() {
  int motion = digitalRead(pir);
  if (motion == HIGH) {
    digitalWrite(led, HIGH);
    Serial.println("Motion Detected");
  } else {
    digitalWrite(led, LOW);
  }
  delay(500);
}
