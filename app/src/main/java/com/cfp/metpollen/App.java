package com.cfp.metpollen;

import com.activeandroid.ActiveAndroid;
import com.crashlytics.android.Crashlytics;
import com.cfp.metpollen.view.broadcastRecievers.ConnectivityReceiver;
import com.cfp.metpollen.view.broadcastRecievers.LocationProviderChangedReceiver;
import com.cfp.metpollen.view.customViews.blurkit.BlurKit;
import com.cfp.metpollen.view.utilities.utils;

import io.fabric.sdk.android.Fabric;

/**
 * Created by AhmedAbbas on 12/7/2017.
 */

public class App extends com.activeandroid.app.Application implements LocationProviderChangedReceiver.LocationReceiverListener{

    private static App INSTANCE;
    public static /*synchronized*/ App getInstance() {
        return INSTANCE;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        ActiveAndroid.initialize(this);
        Fabric.with(this, new Crashlytics());
        INSTANCE = this;
        LocationProviderChangedReceiver.locationReceiverListenerWidget = this;
        BlurKit.init(this);
    }

    public void setConnectivityListener(ConnectivityReceiver.ConnectivityReceiverListener listener) {
        ConnectivityReceiver.connectivityReceiverListener = listener;
    }

    public void setLocationConnectivityListener(LocationProviderChangedReceiver.LocationReceiverListener listener) {
        LocationProviderChangedReceiver.locationReceiverListener = listener;
    }

    @Override
    public void onLocationEnabledStateChanged(boolean isConnected) {
        utils.updateWidget();
    }
}
