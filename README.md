# GLocationLib
google gps location view


1.add this aar to your project
2.add the following to your gradle
    implementation project(':glocationlib')
    implementation 'com.google.android.gms:play-services-location:16.0.0'
    implementation 'com.yanzhenjie.permission:support:2.0.1'
3.add the following to your manifest
    <meta-data
                android:name="com.google.android.gms.version"
                android:value="@integer/google_play_services_version"/>
    <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION"/>
    <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION"/>

4.add btn to your xml
LocationBtn -> get address & set address its own
AbsLocationBtn -> get lat & lon, but have to setLocationCallback(listenr)

5.your xml is
fragment -> extend LocationObserveFragment
activity -> extend LocationObserveActivity
