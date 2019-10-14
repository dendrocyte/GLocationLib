package com.example.googlelocation.customView;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;
import android.util.Log;

import com.example.googlelocation.util.GpsUtil;
import com.example.googlelocation.util.LocationUpdateUtil;
import com.example.googlelocation.util.PermissionUtil;

import java.util.List;

/**
 * Created by luyiling on 2019/4/4
 * Modified by luyiling on 2019/10/4
 * @impNote
 * 為了讓其他的btn可以做修改
 * 將startLocationUpdate()和setOnAddressListener()
 * 的個別程式碼提出來
 * 讓其他btn可以做override
 * <p>
 * TODO: customized btn should modified while lifecycle of activity and fragment changed
 *
 * <IMPORTANT>use customize btn/ textview to show the location information,
 * inclusive of google location module, permission check and gps check, reverse geocoding</IMPORTANT>
 */
public class LocationBtn extends UtilBtn {
    private String TAG = LocationBtn.class.getSimpleName();
    private boolean isActiveUpdate;
    public LocationBtn(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public void init(final Context context){
        Log.e(TAG,"init custom view");
        PermissionUtil.getInstance().askLocationPermission(context, new PermissionUtil.Callback() {
            @SuppressLint("MissingPermission")
            @Override
            public void onSuccess(List<String> granted_permission) {
                //TODO: permission is ok, but gps could be not turn on yet
                Log.e(TAG, "permission here");
                gpsUtil.turnGPSOn(getActivity(context), new GpsUtil.onGpsListener() {
                    @Override
                    public void gpsStatus(boolean isGPSEnable) {
                        Log.e(TAG, "enable gps:"+isGPSEnable);
                        //TODO: update the new location, this 2 method should be nearby as below
                        startLocationUpdate();
                        setOnAddressListener();
                    }
                });
            }
            @Override
            public void onFailed(List<String> deny_permission) {

            }
        });
    }

    /**
     * for override
     */
    protected void startLocationUpdate(){
        LocationUpdateUtil.getInstance().startLocationUpdates(gpsUtil.getLocateRequest());
    }

    /**
     * for override
     */
    protected void setOnAddressListener(){
        LocationUpdateUtil.getInstance().setOnAddressListener(new LocationUpdateUtil.AddressListener() {
            @Override
            public void update(boolean activeUpdate, LocationUpdateUtil.Address address) {
                isActiveUpdate = activeUpdate;
                Log.d(TAG,"current city:"+address.getCity());
                setText(address.getCity());
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isActiveUpdate) init(context);
    }

    @Override
    public void onStop() {
        super.onStop();
        LocationUpdateUtil.getInstance().stopLocationUpdates();
    }

    /*為了拿到activity的context */
    private Activity getActivity(Context cont) {
        if (cont == null)
            return null;
        else if (cont instanceof Activity)
            return (Activity)cont;
        else if (cont instanceof ContextWrapper)
            return getActivity(((ContextWrapper)cont).getBaseContext());

        return null;
    }
}
