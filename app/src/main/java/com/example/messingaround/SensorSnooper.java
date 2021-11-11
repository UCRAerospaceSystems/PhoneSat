package com.example.messingaround;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import java.util.Vector;

public class SensorSnooper implements SensorEventListener {
    //Sensor Manager
    SensorManager sensorManager;
    private boolean running;

    //Motion Section
    private Sensor accelerometer;
    private Vector3 linearAccelerationInstant;
    private Vector3 linearAccelerationAverage;
    private Vector3 linearAccelerationSum;
    private float linearAccelerationSumCount = 0;

    private Sensor gyroscope;
    private Vector3 rotationalAccelerationInstant;
    private Vector3 rotationalAccelerationAverage;
    private Vector3 rotationalAccelerationSum;
    private float rotationalAccelerationSumCount = 0;

    //Magnetic Field Section
    private Sensor magnetometer;
    private Vector3 magneticFieldInstant;
    private Vector3 magneticFieldAverage;
    private Vector3 magneticFieldSum;
    private float magneticFieldSumCount = 0;

    //Pressure Section
    private Sensor barometer;
    private float pressure_Instant = 0;
    private float pressure_Average= 0;
    private float pressure_Sum = 0;
    private float pressure_SumCount = 0;

    SensorSnooper(SensorManager sensorManager){
        this.sensorManager = sensorManager;

        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        gyroscope = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        magnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        barometer = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);
    }

    public Vector3 getLinearAccelerationInstant(){
        return linearAccelerationInstant;
    }

    public Vector3 getLinearAccelerationAverage() {
        return linearAccelerationAverage;
    }

    public Vector3 getRotationalAccelerationInstant() {
        return rotationalAccelerationInstant;
    }

    public Vector3 getRotationalAccelerationAverage(){
        return rotationalAccelerationAverage;
    }

    public Vector3 getMagneticFieldInstant() {
        return magneticFieldInstant;
    }

    public Vector3 getMagneticFieldAverage() {
        return magneticFieldAverage;
    }

    public float getPressureInstant() {
        return pressure_Instant;
    }

    public float getPressureAverage(){
        return pressure_Average;
    }

    public void resume(){
        if (!running) {
            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
            sensorManager.registerListener(this, gyroscope, SensorManager.SENSOR_DELAY_NORMAL);
            sensorManager.registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_NORMAL);
            sensorManager.registerListener(this, barometer, SensorManager.SENSOR_DELAY_NORMAL);
            running = true;
        }
    }

    public void pause(){
        sensorManager.unregisterListener(this);
        running = false;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        String sensorType = event.sensor.getStringType();
        if (sensorType == accelerometer.getStringType()){
            linearAccelerationInstant.set(event.values);

            if (linearAccelerationSumCount > 10){
                linearAccelerationAverage = linearAccelerationSum.divideUnchanged(linearAccelerationSumCount);

                linearAccelerationSum.set(0, 0, 0);
                linearAccelerationSumCount = 0;
            }

            linearAccelerationSum.add(event.values);
            linearAccelerationSumCount += 1;
        }else if (sensorType == gyroscope.getStringType()){
            rotationalAccelerationInstant.set(event.values);
        }else if (sensorType == magnetometer.getStringType()){
            magneticFieldInstant.set(event.values);
        }else if (sensorType == barometer.getStringType()){
            pressure_Instant = event.values[0];
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
