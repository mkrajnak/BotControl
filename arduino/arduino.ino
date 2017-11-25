#include <AFMotor.h>

AF_DCMotor motor(3, MOTOR12_8KHZ);
AF_DCMotor motor1(1, MOTOR12_8KHZ);
int data;
char set[3];
int blik = 200;
int echoPin = 33;                               
int initPin = 32;                                
unsigned long pulseTime = 0;                   
unsigned long distance = 0;                    
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
    motor1.setSpeed(200);
    motor.setSpeed(200);
    motor1.run(FORWARD);
    motor.run(FORWARD);
     digitalWrite(initPin, HIGH);                    
     delayMicroseconds(10);                 
    digitalWrite(initPin, LOW);                    
    pulseTime = pulseIn(echoPin, HIGH);             
    distance = pulseTime/58;   
  Serial.println(distance, DEC);         
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
  delay(100);                           
  }
  motor1.setSpeed(255);
  motor.setSpeed(255);
  motor1.run(RELEASE);
  motor.run(RELEASE);
  Serial.println("canceling automode");
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
      digitalWrite(45,1);   }
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
        digitalWrite(49,1);
        digitalWrite(50,1);
        digitalWrite(44,1);
        digitalWrite(45,1);
        delay(blik);
        digitalWrite(49,0);
        digitalWrite(50,0);
        digitalWrite(44,0);
        digitalWrite(45,0);
        delay(blik);
      }   }
    break;
    case ('3'):{
      Serial.println("Changing delay");
      Serial1.readBytes(set,3);
      Serial.println(((set[0]-'0')*100)+((set[1]-'0')*10)+(set[2]-'0'));
      blik=(((set[0]-'0')*100)+((set[1]-'0')*10)+(set[2]-'0'));
    }
    break;
       default:{
      motor.run(RELEASE);
      motor1.run(RELEASE);
    } }}}

