package com.example.googlelocation;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.util.Log;

import com.example.googlelocation.rxListener.IViewActionHandler;

/**
 * Created by luyiling on 2019/4/5
 * <p>
 * TODO:
 *
 * <IMPORTANT></IMPORTANT>
 */
public class LocationBtnLifecycleObserver implements LifecycleObserver {
    private String TAG = LocationBtnLifecycleObserver.class.getSimpleName();
    private IViewActionHandler actionHandler;
    static LocationBtnLifecycleObserver locationBtnLifecycleObserver;
    private Lifecycle lifecycle;
    public static LocationBtnLifecycleObserver getInstance(){
        if (locationBtnLifecycleObserver == null)
            locationBtnLifecycleObserver = new LocationBtnLifecycleObserver();
        return locationBtnLifecycleObserver;
    }

    public void registerViewActionHandler(IViewActionHandler observable){
        this.actionHandler = observable;
    }

    public void registerLifecycle(Lifecycle lifecycle){
        this.lifecycle = lifecycle;
        lifecycle.addObserver(this);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    void start(){
        Log.e(TAG, "lifeowner start");
        if (actionHandler != null) actionHandler.onStart();
    }
    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    void resume(){
        Log.e(TAG, "lifeowner resume");
        if (actionHandler != null) actionHandler.onResume();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    void stop(){
        Log.e(TAG, "lifeowner stop");
        if (actionHandler != null) actionHandler.onStop();
    }

}
