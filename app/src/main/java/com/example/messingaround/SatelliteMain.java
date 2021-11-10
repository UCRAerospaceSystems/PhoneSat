package com.example.messingaround;

import android.app.Service;
import android.content.Intent;
import android.hardware.SensorManager;
import android.os.IBinder;

public class SatelliteMain extends Service {
    public SensorManager sensorManager;
    public MBManager mbManager;
    public GPSManager gpsManager;
    public MotionManager motionManager;

    @Override
    public void onCreate(){
        sensorManager = (SensorManager) getApplicationContext().getSystemService(SENSOR_SERVICE);

        mbManager = new MBManager(sensorManager);
        motionManager = new MotionManager(sensorManager);
        gpsManager = new GPSManager(this);
    }

    @Override
    public void onDestroy(){
        mbManager.pause();
        motionManager.pause();
    }

    @Override
    public int onStartCommand(Intent aIntent, int flags, int startId){
        gpsManager.updateToLastPosition(this);
        mbManager.resume();
        motionManager.resume();

        //TODO: Listen to USB/Serial Port for events

        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
