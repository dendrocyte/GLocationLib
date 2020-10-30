package com.example.googlelocation.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.googlelocation.rxListener.IActivityResultObserver;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.lang.ref.WeakReference;

/**
 * Created by luyiling on 2019/3/31
 * <p>
 * TODO: method for turn on GPS
 *
 * <IMPORTANT></IMPORTANT>
 */
public class GpsUtil {
    LocationManager locationManager;
    LocationSettingsRequest request;
    SettingsClient client;
    LocationRequest locationRequest;
    WeakReference<Activity> weakAct;
    onGpsListener listener;
    private String TAG = GpsUtil.class.getSimpleName();
    private final int REQUEST_CHECK_SETTINGS = 33;
    //not to be singleton, due to observer should be an new instance
    public GpsUtil(Context application) {
        with(application);
    }

    private void with(Context application) {
        locationManager = (LocationManager) application.getSystemService(Context.LOCATION_SERVICE);
        LocationSettingsRequest.Builder builder
                = new LocationSettingsRequest.Builder().addLocationRequest(createLocationRequest());
        request = builder.build();
        client = LocationServices.getSettingsClient(application);

        //**************************
        builder.setAlwaysShow(true); //this is the key ingredient
        //**************************
    }


    protected LocationRequest createLocationRequest() {
        locationRequest = LocationRequest.create();
        locationRequest.setInterval(10000);//ms
        locationRequest.setFastestInterval(5000);//ms
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);//city level:PRIORITY_LOW_POWER
        return locationRequest;
    }

    /**
     * <IMPORTANT>
     *     Android 4.4-7: gps check
     *     locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
     *     startResolutionForResult(需要activity在這裏)
     * </IMPORTANT>
     * @param onGpsListener
     */
    public void turnGPSOn( final Activity activity, @NonNull final onGpsListener onGpsListener) {
        Task<LocationSettingsResponse> task = client.checkLocationSettings(request);
        task.addOnSuccessListener(new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                //GPS is already enable, callback GPS status through listener
                LocationSettingsStates states = locationSettingsResponse.getLocationSettingsStates();
                /*
                * gps is on, network is on:
                *   isGPSUsable:false
                    isGPSPresent:true
                    isNetworkPresent:true
                    isNetworkUsable:true
                    isLocationPresent:true
                    isLocationUsable:true
                * */
                Log.e(TAG, "\n\nisGPSUsable:"+states.isGpsUsable()
                                +"\nisGPSPresent:"+states.isGpsPresent()
                                +"\nisNetworkPresent:"+states.isNetworkLocationPresent()
                                +"\nisNetworkUsable:"+states.isNetworkLocationUsable()
                                +"\nisLocationPresent:"+states.isLocationPresent()
                                +"\nisLocationUsable:"+states.isLocationUsable()
                );
                onGpsListener.gpsStatus(states.isGpsPresent());

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG, e.toString());
                if (e instanceof ResolvableApiException) {
                    // Location settings are not satisfied, but this can be fixed
                    // by showing the user a dialog.
                    try {
                        // Show the dialog by calling startResolutionForResult(),
                        // and check the result in onActivityResult().
                        ResolvableApiException resolvable = (ResolvableApiException) e;
                        weakAct = new WeakReference<>(activity);
                        listener = onGpsListener;
                        resolvable.startResolutionForResult(weakAct.get(), REQUEST_CHECK_SETTINGS);
                    } catch (IntentSender.SendIntentException sendEx) {
                        // Ignore the error.
                        Log.i(TAG, "PendingIntent unable to execute request.");
                    } catch (NullPointerException e1){
                        // Ignore the error.
                        Log.e(TAG, "Activity shall not be null.");
                    }
                }
            }
        });
    }

    public void turnGPSOn(@NonNull final onGpsListener onGpsListener, @Nullable final onResovableListener onResovableListener) {
        Task<LocationSettingsResponse> task = client.checkLocationSettings(request);
        task.addOnSuccessListener(new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                //GPS is already enable, callback GPS status through listener
                LocationSettingsStates states = locationSettingsResponse.getLocationSettingsStates();
                /*
                * gps is on, network is on:
                *   isGPSUsable:false
                    isGPSPresent:true
                    isNetworkPresent:true
                    isNetworkUsable:true
                    isLocationPresent:true
                    isLocationUsable:true
                * */
                Log.e(TAG, "\n\nisGPSUsable:"+states.isGpsUsable()
                        +"\nisGPSPresent:"+states.isGpsPresent()
                        +"\nisNetworkPresent:"+states.isNetworkLocationPresent()
                        +"\nisNetworkUsable:"+states.isNetworkLocationUsable()
                        +"\nisLocationPresent:"+states.isLocationPresent()
                        +"\nisLocationUsable:"+states.isLocationUsable()
                );
                onGpsListener.gpsStatus(states.isGpsPresent());

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG, e.toString());

                if (e instanceof ResolvableApiException) {
                    if (onResovableListener != null) {
                        onResovableListener.resolve((ResolvableApiException)e);
                    }
                    listener = onGpsListener;
                }
            }
        });
    }


    public void clear(){
        if (weakAct != null) weakAct.clear();
        removeObserver();
    }

    public IActivityResultObserver getObserver(){ return observer; }
    public void removeObserver(){ observer = null; }
    public LocationRequest getLocateRequest(){return locationRequest;}

    IActivityResultObserver observer = new IActivityResultObserver(){
        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            if (requestCode == REQUEST_CHECK_SETTINGS) {
                Log.e(TAG, "result code:"+resultCode);
                listener.gpsStatus(resultCode == Activity.RESULT_OK);
            }
        }
    };

    public interface onGpsListener {
        void gpsStatus(boolean isGPSEnable);
    }

    public interface onResovableListener{
        void resolve(ResolvableApiException e);
    }
}
