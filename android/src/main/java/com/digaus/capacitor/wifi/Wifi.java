package com.digaus.capacitor.wifi;

import android.Manifest;
import android.os.Build;

import com.getcapacitor.PermissionState;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;
import com.getcapacitor.annotation.Permission;
import com.getcapacitor.annotation.PermissionCallback;


@CapacitorPlugin(
    name = "Wifi",
    permissions = {
        @Permission(
            alias = "fineLocation",
            strings = { Manifest.permission.ACCESS_FINE_LOCATION }
        ),
    }
)
public class Wifi extends Plugin {

    private static final int API_VERSION = Build.VERSION.SDK_INT;

    WifiService wifiService;
    InternetConnectivityObserver internetConnectivityObserver;

    @Override
    public void load() {
      super.load();
      this.wifiService = new WifiService();
      this.wifiService.load(this.bridge);
      this.internetConnectivityObserver = new InternetConnectivityObserver();
      this.internetConnectivityObserver.load(this.bridge);
    }
    
    @PluginMethod()
    public void connect(PluginCall call) {
        if (!call.getData().has("ssid")) {
            call.reject("Must provide an ssiddd");
            return;
        }
        if (API_VERSION >= 23 && getPermissionState("fineLocation") != PermissionState.GRANTED) {
            requestPermissionForAlias("fineLocation", call, "accessFineLocation");
        } else {
            this.wifiService.connect(call);
        }

    }

    @PluginMethod()
    public void disconnect(PluginCall call) {
      this.wifiService.disconnect(call);
    }

    @PermissionCallback
    private void accessFineLocation(PluginCall call) {
      if (getPermissionState("fineLocation") == PermissionState.GRANTED) {
        if (call.getMethodName().equals("connect")) {
          this.wifiService.connect(call);
        }
      } else {
        call.reject("User denied permission");
      }
    }
    @PluginMethod()
    public void startObserver(PluginCall call) {
        this.internetConnectivityObserver.startObserving();
    }
    @PluginMethod()
    public void stopObserver(PluginCall call) {
        this.internetConnectivityObserver.stopObserving();
    }
    @PluginMethod()
    public void isConnected(PluginCall call) {
        this.internetConnectivityObserver.isConnected();
    }
}
