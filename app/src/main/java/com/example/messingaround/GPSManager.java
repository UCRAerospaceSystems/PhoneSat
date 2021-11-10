package com.example.messingaround;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.concurrent.Executor;

public class GPSManager implements LocationListener {

    private FusedLocationProviderClient fusedLocationClient;
    private Location location;

    public GPSManager(Service context){
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
    }

    public GPSManager(AppCompatActivity context){
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
    }

    public void updateToLastPosition(Context context){
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //this.setLocation(new Location());

            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        fusedLocationClient.getLastLocation().addOnSuccessListener((Executor) this, location -> {
            if (location != null) {
                this.setLocation(location);
            }
        });
    }

    protected void setLocation(Location location){
        this.location = location;
    }

    public Location getCachedLocation(){
        return location;
    }

    /*public void updatePositionalText(){
        String placeholder = "Latitude: " + location.getLatitude() + " Longitude: " + location.getLongitude()
                + "\n Altitude: " + location.getAltitude() + " Provider: " + location.getProvider()
                + "\n Bearing: " + location.getBearing() + " Speed: " + location.getSpeed();

        gpsLocationLabel.setText(placeholder);
    }*/

    @Override
    public void onLocationChanged(@NonNull Location location) {
        this.location = location;
    }

    @Override
    public void onProviderDisabled(String provider) {}

    @Override
    public void onProviderEnabled(String provider) {}

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {}
}
