
#include <AFMotor.h>


AF_DCMotor motor(3, MOTOR12_8KHZ);
AF_DCMotor motor1(1, MOTOR12_8KHZ);
int data;
boolean a=true ;
char set [3];
int temp,vlavo,vpravo,i;
int merania[3]; 
const int numOfReadings = 10;                   // number of readings to take/ items in the array
int readings[numOfReadings];                    // stores the distance readings in an array
int arrayIndex = 0;                             // arrayIndex of the current item in the array
int total = 0;                                  // stores the cumlative total
int averageDistance = 0;   
int blik =200;// stores the average value

// setup pins and variables for SRF05 sonar device

int echoPin = 33;                                // SRF05 echo pin (digital 2)
int initPin = 32;                                // SRF05 trigger pin (digital 3)
unsigned long pulseTime = 0;                    // stores the pulse in Micro Seconds
unsigned long distance = 0;                     // variable for storing the distance (cm)

void setup() {
  Serial.begin(9600);
  Serial1.begin(9600);
  pinMode(echoPin, INPUT);
  pinMode(initPin, OUTPUT);
  pinMode(50,OUTPUT);
  pinMode(49,OUTPUT);
  pinMode(44,OUTPUT);
  pinMode(45,OUTPUT);
  pinMode(31,OUTPUT);
  motor.setSpeed(255);
  motor1.setSpeed(255);
  
}
void automode(){
  Serial.println("Automode");
  while(data != 'E'){
    data=(char) Serial1.read();
    motor1.setSpeed(180);
    motor.setSpeed(180);
    motor1.run(FORWARD);
     motor.run(FORWARD);
     digitalWrite(initPin, HIGH);                    // send 10 microsecond pulse
     delayMicroseconds(10);                  // wait 10 microseconds before turning off
    digitalWrite(initPin, LOW);                     // stop sending the pulse
    pulseTime = pulseIn(echoPin, HIGH);             // Look for a return pulse, it should be high as the pulse goes low-high-low
    distance = pulseTime/58;   
  Serial.println(distance, DEC);         // print out the average distance to the debugger
  if(distance <=20 && distance !=0){
    motor1.run(RELEASE);
    motor.run(RELEASE);
    motor1.setSpeed(255);
    motor.setSpeed(255);
    motor1.run(FORWARD);
    motor.run(BACKWARD);
    delay(400);
    motor1.setSpeed(200);
    motor.setSpeed(200 );
    motor1.run(RELEASE);
    motor.run(RELEASE);
  }
  delay(100);                                   // wait 100 milli seconds before looping again
  }
  motor1.setSpeed(255);
  motor.setSpeed(255);
  motor1.run(RELEASE);
  motor.run(RELEASE);
  Serial.println("canceling automode");
}

int meraj(){
    digitalWrite(initPin, HIGH);                    // send 10 microsecond pulse
    delayMicroseconds(10);                  // wait 10 microseconds before turning off
    digitalWrite(initPin, LOW);                     // stop sending the pulse
    pulseTime = pulseIn(echoPin, HIGH);             // Look for a return pulse, it should be high as the pulse goes low-high-low
    distance = pulseTime/58;
    return distance;
}

void loop() {
  digitalWrite(31,0);
if(Serial1.available() >0){
  data = Serial1.read();
  switch(data){
    case('F'):{
      while (data != 'E'){  
        data=Serial1.read(); 
    //    Serial1.println(30);
        motor.run(FORWARD);
        motor1.run(FORWARD);
      }
      motor.run(RELEASE);
      motor1.run(RELEASE);
    }
    break;
    case('B'):{
      while (data != 'E'){  
        data=Serial1.read();
        motor.run(BACKWARD);
        motor1.run(BACKWARD);
      }
      motor.run(RELEASE);
      motor1.run(RELEASE);
    }
    break;
    case('L'):{
      while (data != 'E'){  
        data=Serial1.read();
        motor.run(FORWARD);
        motor1.run(BACKWARD);
      }
      motor.run(RELEASE);
      motor1.run(RELEASE);
    }
    break;
    case('R'):{
      while (data != 'E'){  
        data=Serial1.read();
        motor.run(BACKWARD);
        motor1.run(FORWARD);
        
      }
      motor.run(RELEASE);
      motor1.run(RELEASE);
    }
    break;
    case('A'):
      automode();
    break;
    case ('S'):{
      Serial.println("Changing speed");
      Serial1.readBytes(set,3);
      Serial.println(((set[0]-'0')*100)+((set[1]-'0')*10)+((set[2]-'0')));
      motor.setSpeed(((set[0]-'0')*100)+((set[1]-'0')*10)+((set[2]-'0')));
      motor1.setSpeed(((set[0]-'0')*100)+((set[1]-'0')*10)+((set[2]-'0')));
    }
    break;
    case('1'):{      //lights ON
      digitalWrite(49,1);
      digitalWrite(50,1);
      digitalWrite(44,1);
      digitalWrite(45,1);
    }
    break;
    case('0'):{    //Light OFF
       digitalWrite(49,0);
      digitalWrite(50,0);
      digitalWrite(44,0);
      digitalWrite(45,0);
      
    }
    break;
    case('2'):{ //Vystrazne svetla
      while (data != 'E'){  
        data=Serial1.read();
        if(a)
          a=false;
         else
           a=true;
        digitalWrite(49,a);
        digitalWrite(50,a);
        digitalWrite(44,a);
        digitalWrite(45,a);
        delay(blik);
        
      }
    }
    break;
    case ('3'):{
      Serial.println("Changing delay");
      Serial1.readBytes(set,3);
      Serial.println(((set[0]-'0')*100)+((set[1]-'0')*10)+(set[2]-'0'));
      blik=(((set[0]-'0')*100)+((set[1]-'0')*10)+(set[2]-'0'));
    }
    break;
     break;
    case('M'):{      //lights ON
        Serial1.println(meraj(),DEC);
    }
    break;
    default:{
      motor.run(RELEASE);
      motor1.run(RELEASE);
    }
    
 }

}
}
