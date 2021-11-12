package com.example.messingaround;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;

public class AutoBoot extends BroadcastReceiver {

    @Override
    public void onReceive(Context aContext, Intent aIntent) {
        // On boot, lock the CPU to be awoken
        PowerManager powerManager = (PowerManager) aContext.getSystemService(aContext.POWER_SERVICE);
        PowerManager.WakeLock wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "Satellite::StayOn");

        //Now, in a normal program we would make sure to release this wake lock but we never want to let the phone sleep since satellite
        wakeLock.acquire();

        //Start our main service up
        aContext.startService(new Intent(aContext, SatelliteMain.class));

        //Start the activity/app
        aContext.startActivity(new Intent(aContext, MainActivity.class));
    }
}