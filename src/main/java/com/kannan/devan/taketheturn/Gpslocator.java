package com.kannan.devan.taketheturn;

import android.*;
import android.Manifest;
import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import static android.content.Context.LOCATION_SERVICE;

/**
 * Created by devan on 8/1/17.
 */

public class Gpslocator implements LocationListener {

    public Context mContext;
    public boolean iSGPSEnabled = false;
    public boolean isNetworkEnabled=false;
    public boolean GetLocationStatus = false;

    Location mGpsLocation;
    Location mNetLocation;
    Location mLocation;
    double latitude;
    double longitude;

    private static final long MIN_UPDATE_DISTANCE = 1000;
    private static final long MIN_UPDATE_TIME = 1000 * 60 * 1;

    protected LocationManager mLocationManager;

    public Gpslocator(Context context) {
        super();
        mContext = context;
        getLocation();


    }

    public void getLocation() {
        try {
            mLocationManager = (LocationManager) mContext.getSystemService(LOCATION_SERVICE);
            iSGPSEnabled = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            isNetworkEnabled=mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            if (iSGPSEnabled) {
                if (mGpsLocation == null) {
                    if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_UPDATE_TIME, MIN_UPDATE_DISTANCE, this);
                    mGpsLocation = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                }
            }
             if(isNetworkEnabled){
                if (mNetLocation==null){
                    mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,MIN_UPDATE_TIME,MIN_UPDATE_DISTANCE,this);
                    mNetLocation=mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                }
            }

            if (mGpsLocation!=null&&mNetLocation!=null){
                if (mGpsLocation.getAccuracy()>mNetLocation.getAccuracy()){
                    latitude=mGpsLocation.getLatitude();
                    longitude=mGpsLocation.getLongitude();
                    mLocation=mGpsLocation;
                }
                else {
                    latitude=mNetLocation.getLatitude();
                    longitude=mNetLocation.getLongitude();
                    mLocation=mNetLocation;
                }
            }
            else {
                if (mGpsLocation!=null)
                    mLocation=mGpsLocation;
                else
                    if (mNetLocation!=null)
                        mLocation=mNetLocation;
            }

        } catch (Exception ex) {
            Log.e("Request Permission", ex.getMessage());
        }
    }

    public void stopUsingGps() {
        if (mLocationManager != null) {
            if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            mLocationManager.removeUpdates(Gpslocator.this);
        }
    }

    public double getLatitude(){
        if (mLocation!=null){
            latitude=mLocation.getLatitude();
        }
        return latitude;
    }

    public double getLongitude(){
        if (mLocation!=null){
            longitude=mLocation.getLongitude();
        }
        return longitude;
    }

    public boolean setGetLocationStatus(){
        return this.GetLocationStatus;
    }

    public void showSettingsAlet(){
        final AlertDialog.Builder settingsAlert=new AlertDialog.Builder(mContext)
                .setTitle("Enable GPS")
                .setMessage("GPS is not enabled in device. Please goto settings and enable GPS")
                .setPositiveButton("Goto Settings", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent=new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        mContext.startActivity(intent);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
        settingsAlert.show();
    }


    @Override
    public void onLocationChanged(Location location) {
        latitude=location.getLatitude();
        longitude=location.getLongitude();
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }


}
