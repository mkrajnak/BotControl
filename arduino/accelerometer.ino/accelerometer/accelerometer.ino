#include <Wire.h>
#include <MPU6050.h>

MPU6050 mpu;

void checkSettings()
{
  Serial.println();
  
  Serial.print(" * Sleep Mode:            ");
  Serial.println(mpu.getSleepEnabled() ? "Enabled" : "Disabled");
  
  Serial.print(" * Clock Source:          ");
  switch(mpu.getClockSource())
  {
    case MPU6050_CLOCK_KEEP_RESET:     Serial.println("Stops the clock and keeps the timing generator in reset"); break;
    case MPU6050_CLOCK_EXTERNAL_19MHZ: Serial.println("PLL with external 19.2MHz reference"); break;
    case MPU6050_CLOCK_EXTERNAL_32KHZ: Serial.println("PLL with external 32.768kHz reference"); break;
    case MPU6050_CLOCK_PLL_ZGYRO:      Serial.println("PLL with Z axis gyroscope reference"); break;
    case MPU6050_CLOCK_PLL_YGYRO:      Serial.println("PLL with Y axis gyroscope reference"); break;
    case MPU6050_CLOCK_PLL_XGYRO:      Serial.println("PLL with X axis gyroscope reference"); break;
    case MPU6050_CLOCK_INTERNAL_8MHZ:  Serial.println("Internal 8MHz oscillator"); break;
  }
  
  Serial.print(" * Accelerometer:         ");
  switch(mpu.getRange())
  {
    case MPU6050_RANGE_16G:            Serial.println("+/- 16 g"); break;
    case MPU6050_RANGE_8G:             Serial.println("+/- 8 g"); break;
    case MPU6050_RANGE_4G:             Serial.println("+/- 4 g"); break;
    case MPU6050_RANGE_2G:             Serial.println("+/- 2 g"); break;
  }

  mpu.setDLPFMode(MPU6050_DLPF_2);
  mpu.setDHPFMode(MPU6050_DHPF_5HZ);

  Serial.println();
}
  
  int gDivider = 16384;
  long xOffsetAverageSum, yOffsetAverageSum, zOffsetAverageSum;
  int xyzOffsetAverageDivider;

  double xVelocity, yVelocity, zVelocity; // in m/s
  double xTravel, yTravel, zTravel; // in m
  double deltaTime = millis();;
  double currTime;
  
 
  // just the raw values from the accelerometer
  void addMeasurementsToOffset( short xAcceleration, short yAcceleration, short zAcceleration) 
  {
    
    xOffsetAverageSum += xAcceleration;
    yOffsetAverageSum += yAcceleration;
    zOffsetAverageSum += zAcceleration;
    xyzOffsetAverageDivider++;
  }
  

  void addMeasurementsToTravel( short xAcceleration, short yAcceleration, short zAcceleration) 
  {
    double ax = xAcceleration;
    double ay = yAcceleration;
    double az = zAcceleration;
    if( xyzOffsetAverageDivider > 0) {

      ax -= (double)xOffsetAverageSum / xyzOffsetAverageDivider;
      ay -= (double)yOffsetAverageSum / xyzOffsetAverageDivider;
      az -= (double)zOffsetAverageSum / xyzOffsetAverageDivider;
    }
    
    // convert to g force
    ax /= gDivider;
    ay /= gDivider;
    az /= gDivider;
    
    // convert to force [N]
    ax *= 9.80665;
    ay *= 9.80665;
    az *= 9.80665;
    
    // distance moved in deltaTime, s = 1/2 a t^2 + vt
    currTime = millis();
    deltaTime = (currTime - deltaTime)/1000;
    double sx = 0.5 * ax * deltaTime * deltaTime + xVelocity * deltaTime;
    double sy = 0.5 * ay * deltaTime * deltaTime + yVelocity * deltaTime;
    double sz = 0.5 * az * deltaTime * deltaTime + zVelocity * deltaTime;
    xTravel += sx;
    yTravel += sy;
    zTravel += sz;
    
    // change in velocity, v = v0 + at
    xVelocity += ax * deltaTime;
    yVelocity += ay * deltaTime;
    zVelocity += az * deltaTime;
    deltaTime = currTime;
  }


void setup() 
{
  // Power for accelerometer
  pinMode(24,OUTPUT);
  pinMode(25,OUTPUT); 
  digitalWrite(24,1);
  digitalWrite(25,0); 
  delay(1000);
  Serial.begin(115200);

  Serial.println("Initialize MPU6050");

  while(!mpu.begin(MPU6050_SCALE_2000DPS, MPU6050_RANGE_2G))
  {
    Serial.println("Could not find MPU6050 sensor!");
    delay(500);
  }
  
  checkSettings();
  Vector rawAccel = mpu.readRawAccel();
  for (int i=0; i<10; i++){
    addMeasurementsToOffset(rawAccel.XAxis, rawAccel.YAxis, rawAccel.ZAxis);  
  }
}
  


void loop()
{
  Vector rawAccel = mpu.readRawAccel();
  addMeasurementsToTravel(rawAccel.XAxis, rawAccel.YAxis,rawAccel.ZAxis );

    
  Serial.print(" X = ");
  Serial.print(xTravel);
  Serial.print("   time(ms):");
  Serial.println(millis());
//  Serial.print(" Y = ");
//  Serial.print(yVelocity);
//  Serial.print(" Z = ");
//  Serial.println(zVelocity);
  
//  Serial.print(" Xnorm = ");
//  Serial.print(normAccel.XAxis);
//  Serial.print(" Ynorm = ");
//  Serial.print(normAccel.YAxis);
//  Serial.print(" Znorm = ");
//  Serial.println(normAccel.ZAxis);
  delay(100);


}


