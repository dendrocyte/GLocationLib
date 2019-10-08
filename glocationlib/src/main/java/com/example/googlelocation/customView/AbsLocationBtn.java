package com.example.googlelocation.customView;

import android.content.Context;
import android.location.Location;
import android.util.AttributeSet;
import android.util.Log;

import com.example.googlelocation.util.LocationUpdateUtil;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;

/**
 * Created by luyiling on 2019-10-04
 * Modified by
 *
 * <title> </title>
 * TODO:
 * Description: wanna get the lon & lat of location
 *
 * <IMPORTANT>
 * this is the tutorial for get abs location btn
 * do these necessary
 * @params setLocationCallback(AbsLocationListener callback)
 * @params AbsLocationBtn setStopEnv(int spec)
 * </IMPORTANT>
 */
public class AbsLocationBtn extends LocationBtn  {
    private String TAG = AbsLocationBtn.class.getSimpleName();
    private AbsLocationListener listener;
    public AbsLocationBtn(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * do it necessary at your UI layer
     * @param callback
     */
    public void setLocationCallback(AbsLocationListener callback){
        this.listener = callback;
    }

    public final int STOP_AFTER_GOT = 2;
    public final int NOT_STOP = 3;
    private int stop_spec = NOT_STOP; //default not_stop

    /**
     * do it necessary at your UI layer
     * 2 choice: wanna non_stop or stop after got
     * @param spec STOP_AFTER_GOT | NOT_STOP
     * @return AbsLocationBtn
     */
    public AbsLocationBtn setStopEnv(int spec){
        stop_spec = spec;
        return this;
    }


    @Override
    protected void startLocationUpdate() {
        LocationUpdateUtil
                .getInstance()
                .startLocationUpdates(gpsUtil.getLocateRequest(), locationCallback);

    }

    private LocationCallback locationCallback = new LocationCallback(){
        @Override
        public void onLocationResult(LocationResult locationResult) {
            if (stop_spec == STOP_AFTER_GOT && locationResult.getLastLocation() != null){
                LocationUpdateUtil.getInstance().stopLocationUpdates();
            }
            listener.onAbsResult(locationResult);
        }
    };

    @Override
    protected void setOnAddressListener() {
        /**
         * not to do this
         * clear the super.setOnAddressListener
         */
    }

    interface AbsLocationListener{
        void onAbsResult(LocationResult result);
    }
}
