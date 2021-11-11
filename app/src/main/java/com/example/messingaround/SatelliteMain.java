package com.example.messingaround;

import android.app.Service;
import android.content.Intent;
import android.hardware.SensorManager;
import android.os.IBinder;

public class SatelliteMain extends Service {
    public SensorManager sensorManager;
    public SensorSnooper sensorHub;
    public GPSManager gpsManager;

    @Override
    public void onCreate(){
        sensorManager = (SensorManager) getApplicationContext().getSystemService(SENSOR_SERVICE);

        sensorHub = new SensorSnooper(sensorManager);
        gpsManager = new GPSManager(this);
    }

    @Override
    public void onDestroy(){
        sensorHub.pause();
    }

    @Override
    public int onStartCommand(Intent aIntent, int flags, int startId){
        gpsManager.updateToLastPosition(this);
        sensorHub.resume();

        //TODO: Listen to USB/Serial Port for events

        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
