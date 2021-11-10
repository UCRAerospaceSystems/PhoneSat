package com.example.messingaround;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.widget.TextView;

public class MBManager extends Activity implements SensorEventListener {
    private boolean running = false;
    private SensorManager sManager;

    private Sensor magnetometer;
    private Vector3 magneticVector_Instant;
    private Vector3 magneticVector_Average;
    private Vector3 magneticVector_Sum;
    private float magneticVector_SumCount;

    private Sensor barometer; //millibar
    private float pressure_Instant;
    private float pressure_Average;
    private float pressure_Sum;
    private float pressure_SumCount;

    //private final TextView label;

    public MBManager(SensorManager sensorManager){ //, TextView uiObject){
        sManager = sensorManager;

        magnetometer = sManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        magneticVector_Instant = new Vector3();
        magneticVector_Average = new Vector3();
        magneticVector_Sum = new Vector3();
        magneticVector_SumCount = 0;

        barometer = sManager.getDefaultSensor(Sensor.TYPE_PRESSURE);
        pressure_Instant = 0;
        pressure_Average = 0;
        pressure_Sum = 0;
        pressure_SumCount = 0;

        //label = uiObject;

        this.resume();
    }

    public void resume(){
        if (!running) {
            sManager.registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_NORMAL);
            sManager.registerListener(this, barometer, SensorManager.SENSOR_DELAY_NORMAL);
            running = true;
        }
    }

    public void pause(){
        sManager.unregisterListener(this);
        running = false;
    }

    public Vector3 getInstantMagneticVector(){ return this.magneticVector_Instant; }
    public Vector3 getMagneticVector_Average() { return this.magneticVector_Average; }

    public float getInstantPressure(){ return this.pressure_Instant; }
    public float getAveragePressure(){ return this.pressure_Average; }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.toString() == Sensor.STRING_TYPE_MAGNETIC_FIELD){
            magneticVector_Instant.set(event.values);

            if (magneticVector_SumCount > 10){
                magneticVector_Average = magneticVector_Sum.divideUnchanged(magneticVector_SumCount);

                magneticVector_Sum.set(0, 0, 0);
                magneticVector_SumCount = 0;
            }

            magneticVector_Sum.add(event.values);
            magneticVector_SumCount += 1;
        }else if (event.toString() == Sensor.STRING_TYPE_PRESSURE){
            pressure_Instant = event.values[0];

            if (pressure_SumCount > 10){
                pressure_Average = pressure_Sum / pressure_SumCount;

                pressure_Sum = 0;
                pressure_SumCount = 0;
            }

            pressure_Sum += pressure_Instant;
            pressure_SumCount += 1;
        }


        /*String placeholder = "Magnetic Field: (" + magneticVector_Average.getX() + ", " + magneticVector_Average.getY() + ", " + magneticVector_Average.getZ() + ")\n"
                + "Pressure: " + pressure_Instant + "mPa";
        label.setText(placeholder); */
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
