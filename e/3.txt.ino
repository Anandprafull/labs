
int led = 9;
int pot = A0;

void setup() {
  pinMode(led, OUTPUT);
}

void loop() {
  int val = analogRead(pot);
  analogWrite(led, val / 4);
}
