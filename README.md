# GLocationLib
**Google gps location view**

wrap the google gps module and location permission module.<br>
Just put LocationBtn in xml,
it will handle everyting, and update the location text

--------

*Step1.add this aar to your project*

*Step2.add the following to your gradle*

````
    implementation project(':glocationlib')
    implementation 'com.google.android.gms:play-services-location:16.0.0'
    implementation 'com.yanzhenjie.permission:support:2.0.1'
````
    
*Step3.add the following to your manifest*

````
    <meta-data
                android:name="com.google.android.gms.version"
                android:value="@integer/google_play_services_version"/>
    <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION"/>
    <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION"/>
````

*Step4.add btn to your xml*
LocationBtn: get address & set address its own
AbsLocationBtn: get lat & lng, but have to setLocationCallback(listenr)

*Step5. if your xml is for*
fragment -> extend LocationObserveFragment
activity -> extend LocationObserveActivity
