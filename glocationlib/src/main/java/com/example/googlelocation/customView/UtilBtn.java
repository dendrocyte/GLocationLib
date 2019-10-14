package com.example.googlelocation.customView;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.util.Log;

import com.example.googlelocation.LocationBtnLifecycleObserver;
import com.example.googlelocation.rxListener.IViewActionHandler;
import com.example.googlelocation.util.GpsUtil;
import com.example.googlelocation.rxListener.IActivityResultObservable;
import com.example.googlelocation.rxListener.IActivityResultObserver;
import com.example.googlelocation.util.LocationUpdateUtil;

/**
 * Created by luyiling on 2019/4/4
 * <p>
 * TODO: register / add / remove observer, actionHandler
 *
 * <IMPORTANT></IMPORTANT>
 */
public class UtilBtn extends AppCompatTextView implements IViewActionHandler {
    Context context;
    private String TAG = UtilBtn.class.getSimpleName();
    protected GpsUtil gpsUtil;//have to get an instance
    private IActivityResultObserver activityResultObserver;
    public UtilBtn(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        activityResultObserver = init(context.getApplicationContext());
        LocationBtnLifecycleObserver.getInstance().registerViewActionHandler(this);
        /**
         * @ImpNote 當設定成external lib, context(fragment) 可能會遺失 fragment 裡的 activity
         * @ImpNote 這時要做Intent 就會NullPointerException
         * @ImpNote 即使將context置入為application context也一樣
         * @ImpNote 若改由隱性intent呼叫，就需要將：app module 的 package name塞入才行
         * @ImpNote 故在btn產生時就將package name 塞入 Util，每當要做intent 就自己補足該package name
         */
        LocationUpdateUtil.getInstance()
                .init(context.getApplicationContext(), context.getPackageName());//need to be initialize first
    }

    //***************** GpsUtil observe onActivityResult() **************************************//

    //get Util activityResultObserver
    private IActivityResultObserver init(Context application){
        gpsUtil = new GpsUtil(application);
        return gpsUtil.getObserver();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (context instanceof IActivityResultObservable) {
            ((IActivityResultObservable) context).addObserver(activityResultObserver);
        }
    }
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (context instanceof IActivityResultObservable) {
            ((IActivityResultObservable) context).removeObserver(activityResultObserver);
        }
        gpsUtil.clear();
        LocationUpdateUtil.getInstance().detach();
    }

    //****************** UtilBtn observe Activity/Fragment lifecycle *****************************//
    @Override
    public void onStart() {
        Log.e(TAG, "detect the lifecycleowner--- started");
    }

    @Override
    public void onResume() { Log.e(TAG, "detect the lifecycleowner--- resumed"); }

    @Override
    public void onStop() {
        Log.e(TAG, "detect the lifecycleowner--- stoped");
    }


}
