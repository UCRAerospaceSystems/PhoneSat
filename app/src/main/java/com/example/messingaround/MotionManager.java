package com.example.messingaround;

import android.app.Activity;
import android.hardware.*;
import android.widget.TextView;

public class MotionManager extends Activity implements SensorEventListener{
    private boolean running = false;

    private final SensorManager SManager;
    private final Sensor Accelerometer;
    private final Sensor Gyroscope;

    private Vector3 LinearAccelerationVector_Instant;
    private Vector3 LinearAccelerationVector_Average;
    private Vector3 LinearAccelerationVector_Sum;
    private float LinearAccelerationSumCount;

    private Vector3 RotationalAccelerationVector_Instant;
    private Vector3 RotationalAccelerationVector_Average;
    private Vector3 RotationalAccelerationVector_Sum;
    private float RotationalAccelerationSumCount;

    //private final TextView LinearAccelerationLabel;
    //private final TextView RotationalAccelerationLabel;
    //private final TextView InaccuracyLabel;

    private float aInaccuracy;
    private float raInaccuracy;
    
    public MotionManager(SensorManager sm){// TextView accelLabel, TextView rotLabel){ //, TextView inaccLabel){
        SManager = sm;
        Accelerometer = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        Gyroscope = sm.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

        LinearAccelerationVector_Instant = new Vector3(0, 0, 0);
        LinearAccelerationVector_Average = new Vector3(0, 0, 0);
        LinearAccelerationVector_Sum = new Vector3(0, 0, 0);
        LinearAccelerationSumCount = 0;

        RotationalAccelerationVector_Instant = new Vector3(0, 0, 0);
        RotationalAccelerationVector_Average = new Vector3(0, 0, 0);
        RotationalAccelerationVector_Sum = new Vector3(0, 0, 0);
        RotationalAccelerationSumCount = 0;

        //InaccuracyLabel = inaccLabel;
        //LinearAccelerationLabel = accelLabel;
        //RotationalAccelerationLabel = rotLabel;

        aInaccuracy = 0;
        raInaccuracy = 0;

        this.resume();
    }

    public void resume(){
        if (!running) {
            SManager.registerListener(this, Accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
            SManager.registerListener(this, Gyroscope, SensorManager.SENSOR_DELAY_NORMAL);
            running = true;
        }
    }

    public void pause(){
        SManager.unregisterListener(this);
        running = false;
    }

    public Vector3 getInstantLinearAcceleration(){
        return LinearAccelerationVector_Instant;
    }
    public Vector3 getAverageLinearAcceleration() { return LinearAccelerationVector_Average; }

    public Vector3 getInstantRotationalAcceleration(){
        return RotationalAccelerationVector_Instant;
    }
    public Vector3 getAverageRotationalAcceleration(){
        return RotationalAccelerationVector_Average;
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        if (sensor.getStringType() == Sensor.STRING_TYPE_ACCELEROMETER) {
            aInaccuracy = accuracy;
        }else{ //Note that we may add more sensors into the MotionManager, so this'll need to be an elsif
            raInaccuracy = accuracy;
        }

        //String placeholder = "Acceleration Accuracy: " + aInaccuracy + ", Rotation Accuracy: " + raInaccuracy;
        //InaccuracyLabel.setText(placeholder);
    }
    
    public void onSensorChanged(SensorEvent event){
        AverageSensor(event.sensor.getStringType(), event.values);

        if (event.sensor.getStringType() == Sensor.STRING_TYPE_ACCELEROMETER) {
            LinearAccelerationVector_Instant.set(event.values);
            float[] normalizedValues = LinearAccelerationVector_Average.getRoundedVector(1);

            //String placeHolder = "Acceleration: (" + normalizedValues[0] + ", "+ normalizedValues[1] + ", " + normalizedValues[2] + ")";
            //LinearAccelerationLabel.setText(placeHolder);
        }else if (event.sensor.getStringType() == Sensor.STRING_TYPE_GYROSCOPE){
            RotationalAccelerationVector_Instant.set(event.values);
            float[] normalizedValues = RotationalAccelerationVector_Instant.getRoundedVector(1);

            //String placeHolder = "Rotational Acceleration: (" + normalizedValues[0] + ", "+ normalizedValues[1] + ", " + normalizedValues[2] + ")";
            //RotationalAccelerationLabel.setText(placeHolder);
        }
    }

    private void AverageSensor(String sensorType, float[] values){
        if (sensorType == Sensor.STRING_TYPE_ACCELEROMETER){
            if (LinearAccelerationSumCount > 10){
                LinearAccelerationVector_Average = LinearAccelerationVector_Sum.divideUnchanged(LinearAccelerationSumCount);

                LinearAccelerationVector_Sum.set(values);
                LinearAccelerationSumCount = 1;
            }else{
                LinearAccelerationVector_Sum.add(values);
                LinearAccelerationSumCount += 1;
            }
        }else if (sensorType == Sensor.STRING_TYPE_GYROSCOPE){
            if (RotationalAccelerationSumCount > 10){
                RotationalAccelerationVector_Average = RotationalAccelerationVector_Sum.divideUnchanged(RotationalAccelerationSumCount);

                RotationalAccelerationVector_Sum.set(values);
                RotationalAccelerationSumCount = 1;
            }else{
                RotationalAccelerationVector_Sum.add(values);
                RotationalAccelerationSumCount += 1;
            }
        }
    }
}
