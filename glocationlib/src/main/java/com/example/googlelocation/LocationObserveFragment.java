package com.example.googlelocation;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import com.example.googlelocation.rxListener.IActivityResultObservable;
import com.example.googlelocation.rxListener.IActivityResultObserver;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by luyiling on 2019/4/4
 * <p>
 * TODO:
 *
 * <IMPORTANT>contain lifecycle observer and onActivityResultObservable</IMPORTANT>
 */
public class LocationObserveFragment extends Fragment
        implements IActivityResultObservable {
    List<IActivityResultObserver> observableList = new ArrayList<>();


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        for (IActivityResultObserver observer: observableList){
            observer.onActivityResult(requestCode, resultCode, data);
        }
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LocationBtnLifecycleObserver.getInstance().registerLifecycle(getLifecycle());
    }


    @Override
    public void addObserver(IActivityResultObserver activityResultObserver) {
        observableList.add(activityResultObserver);
    }

    @Override
    public void removeObserver(IActivityResultObserver activityResultObserver) {
        observableList.remove(activityResultObserver);
    }
}
