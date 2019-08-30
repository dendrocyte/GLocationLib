package com.example.googlelocation.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.util.Log;

import com.example.googlelocation.BuildConfig;
import com.example.googlelocation.Constants;
import com.example.googlelocation.FetchAddressIntentService;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;


/**
 * Created by luyiling on 2019/4/5
 * <p> in need:
 * TODO: PermissionUtil -> GpsUtil -> LocationUpdateUtil
 * via ResultReceiver (kind of broadcast receiver or listener) to pass result
 * <IMPORTANT></IMPORTANT>
 */
public class LocationUpdateUtil {
    static LocationUpdateUtil locationUpdateUtil;
    private FusedLocationProviderClient fusedLocateClient; //TODO: store it in base activity
    private Location mCurrentLocation; //store it in preference
    private AddressListener listener;
    private ResultReceiver resultReceiver;
    private Handler handler;
    private Context context;
    private String TAG = LocationUpdateUtil.class.getSimpleName();
    private String package_name;
    public static LocationUpdateUtil getInstance(){
        if (locationUpdateUtil == null) locationUpdateUtil = new LocationUpdateUtil();
        return locationUpdateUtil;
    }
    public void init(Context application){
        context = application;
        fusedLocateClient = LocationServices.getFusedLocationProviderClient(application);
    }

    public void init(Context application, String package_name){
        context = application;
        this.package_name = package_name;
        fusedLocateClient = LocationServices.getFusedLocationProviderClient(application);
    }
    /**
     * this method is for get last location only, not consistly to update location
     * special case: location be null
     *                      * 1.Location is turned off in the device settings
     *                      * 2.The device never recorded its location,
     *                      * 3.the device that has been restored to factory settings.
     *                      * 4.Google Play services on the device has restarted
     *                      * 5. GPS is close
     *                      *
     * @param locationRequest
     */
    @SuppressLint("MissingPermission")
    public void getLastLocation(final LocationRequest locationRequest){
        fusedLocateClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    //TODO: get last location
                    Log.e(TAG, "lat:" + location.getLatitude()
                            + "long:" + location.getLongitude());
                    mCurrentLocation = location;
                    //TODO: transform to address

                } else {
                    //if the permission or gps is off, then get granted permission and gps is on now
                    //last location will be null
                    startLocationUpdates(locationRequest);
                    //TODO: manage to update the new location, then transform to address

                }
            }
        });
    }

    /**
     * already permission check
     * @param locationRequest
     */
    @SuppressLint("MissingPermission")
    public void startLocationUpdates(LocationRequest locationRequest) {
        fusedLocateClient.requestLocationUpdates(locationRequest, locationCallback, null /* Looper */);
    }
    public void stopLocationUpdates() {
        fusedLocateClient.removeLocationUpdates(locationCallback);
    }
    public void setOnAddressListener(AddressListener listener){
        this.listener = listener;
    }

    private LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            if (locationResult == null) return;
            Log.e(TAG, "get location result:"+locationResult);
            mCurrentLocation = locationResult.getLastLocation();
            Log.e(TAG, mCurrentLocation.getLatitude()+";"+mCurrentLocation.getLongitude());
            startIntentService(true);

            //TODO: if wanna trace location, should fill in database, like Room
//            for (Location location : locationResult.getLocations()) {
//                // Update UI with location data
//                // ...
//            }
        }
    };

    //remove it when view is detached
    public void detach(){
        if (context != null){
            Intent intent = new Intent(BuildConfig.ACTION_LOCATION);
            intent.addCategory(BuildConfig.APPLICATION_ID);
            intent.setPackage(package_name);
//            Intent intent = new Intent(context, FetchAddressIntentService.class);
            context.stopService(intent);
        }
        context = null;
        listener = null;
    }
    //remove it when application is terminated
    public void clear(){
        resultReceiver = null;
        handler = null;
    }

    private void startIntentService(final boolean activeUpdate) {
        handler = new Handler();
        resultReceiver = new ResultReceiver(handler){
            @Override
            protected void onReceiveResult(int resultCode, Bundle resultData) {
                //TODO: pass to activity
                if (resultCode == Constants.SUCCESS_RESULT){
                    Address address = new Address(resultData.getString(Constants.RESULT_DATA_KEY));
                    if(listener != null) listener.update(activeUpdate, address);
                }
            }
        };
        if (context != null){
            Intent intent = new Intent(BuildConfig.ACTION_LOCATION);
            intent.addCategory(BuildConfig.APPLICATION_ID);
            intent.setPackage(package_name);
            intent.putExtra(Constants.RECEIVER, resultReceiver);
            intent.putExtra(Constants.LOCATION_DATA_EXTRA, mCurrentLocation);
            context.startService(intent);
        }
    }

    public interface AddressListener {
        void update(boolean activeUpdate, Address address);
    }

    public class Address{
        String city;
        public Address(String city) {
            this.city = city;
        }
        public String getCity() {
            return city;
        }
    }
}
