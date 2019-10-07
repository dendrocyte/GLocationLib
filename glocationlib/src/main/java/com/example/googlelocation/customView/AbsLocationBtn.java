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
 * @params
 * @params </IMPORTANT>
 */
public class AbsLocationBtn extends LocationBtn  {
    private String TAG = AbsLocationBtn.class.getSimpleName();
    private LocationCallback locationCallback;
    public AbsLocationBtn(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * do it necessary
     * @param callback
     */
    public void setLocationCallback(LocationCallback callback){
        this.locationCallback = callback;
    }

    @Override
    protected void startLocationUpdate() {
        LocationUpdateUtil
                .getInstance()
                .startLocationUpdates(gpsUtil.getLocateRequest(), locationCallback);

    }

    @Override
    protected void setOnAddressListener() {
        /**
         * not to do this
         * clear the super.setOnAddressListener
         */
    }
}
