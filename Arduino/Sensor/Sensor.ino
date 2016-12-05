#include <SoftwareSerial.h>
    
//Define os pinos para a serial  (RX, TX)
SoftwareSerial MinhaSerial(6,7);
String command = "";

const int AOUTpin=0;//the AOUT pin of the CO sensor goes into analog pin A0 of the arduino
const int DOUTpin=8;//the DOUT pin of the CO sensor goes into digital pin D8 of the arduino

const int ledRed=10; //the anode of the red LED connects to digital pin D10 of the arduino
const int ledYellow=11; //the anode of the yellow LED connects to digital pin D11 of the arduino
const int ledGreen=12; //the anode of the green LED connects to digital pin D11 of the arduino

int coLimit = 100; //The limit of CO that is allowed

//int limit;
float value;

void setThreshold(int value){
  coLimit = value;
}

float readDataFromSensor(){
  float sum = 0.0;
  float value;
  int i;

  // We get the value of CO six times in a minute
  for(i = 0; i < 6; i++){
    value = analogRead(AOUTpin); // Reads the CO value and accumulates it 
    sum += value;
    
    //Serial.print("CO value: "); 
    //Serial.println(value); //prints the CO value

    delay(1000 * 10); // Wait 10s for the next measurement
  }

  return sum / 6.0;
}

void turnOnLed(float average){
  /*
   * The Arduino will select the LED to turn on based on the average of CO.
   * First, we must turn off all of them (for when it is necessary to change the LED).
   * Then, we select the LED
   */

  digitalWrite(ledRed, LOW);
  digitalWrite(ledRed, LOW);
  digitalWrite(ledRed, LOW);

  Serial.print("CO average: ");
  Serial.println(average);
//  MinhaSerial.print("CO average: ");
  MinhaSerial.print((int)average);
  
  if(average >= coLimit){
    digitalWrite(ledRed, HIGH);
    digitalWrite(ledGreen, LOW);
    digitalWrite(ledYellow, LOW);
  }
  else if(average >= 0.8 * coLimit){
    digitalWrite(ledYellow, HIGH);
    digitalWrite(ledRed, LOW);
    digitalWrite(ledGreen, LOW);
  }
  else{
    digitalWrite(ledGreen, HIGH);
    digitalWrite(ledRed, LOW);
    digitalWrite(ledYellow, LOW);
  }
}

void setup(){
  Serial.begin(115200); // sets the baud rate 
  MinhaSerial.begin(9600);
  
  pinMode(DOUTpin, INPUT); //sets the pin as an input to the arduino
  //pinMode(ledPin, OUTPUT); //sets the pin as an output of the arduino

  pinMode(ledRed, OUTPUT); pinMode(ledYellow, OUTPUT); pinMode(ledGreen, OUTPUT);
}

void loop(){
  int i = 0;
  char c;
  byte b;
  if(MinhaSerial.available()){
    while(MinhaSerial.available())
     {
       b = MinhaSerial.read();
       c = (char) b;
       command += c;
  
     } 
     Serial.print("received: ");
     int readbytes = Serial.println(command); 
     Serial.print("bytes read: ");
     Serial.println(readbytes);
     int newThreshold = command.toInt();
     setThreshold(newThreshold);
     command = "";
  }
  float average = readDataFromSensor();
  turnOnLed(average);
}

