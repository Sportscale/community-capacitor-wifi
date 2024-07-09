package com.digaus.capacitor.wifi;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import com.getcapacitor.Bridge;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Timer;
import java.util.TimerTask;

public class InternetConnectivityObserver {
    private Context context;
    private ConnectivityManager.NetworkCallback networkCallback;
    private boolean isConnected;
    private boolean isInternet;
    private Bridge bridge;

    public void load(Bridge bridge) {
        this.bridge = bridge;
        this.context = this.bridge.getContext();
    }

    public void startObserving() {
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkRequest networkRequest = new NetworkRequest.Builder()
                    .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                    .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
                    .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
                    .build();

            networkCallback = new ConnectivityManager.NetworkCallback() {
                @Override
                public void onAvailable(Network network) {
                    Log.d("startObserving", "startObserving start " + network);
                    Toast.makeText(context, "Network is available!", Toast.LENGTH_SHORT).show();
                    super.onAvailable(network);
                    isConnected = true;
                    checkInternetConnection();
                }

                @Override
                public void onLost(Network network) {
                    super.onLost(network);
                    Toast.makeText(context, "Network is lost!", Toast.LENGTH_SHORT).show();
                    isConnected = false;
                }

                @Override
                public void onCapabilitiesChanged(Network network, NetworkCapabilities networkCapabilities) {
                    super.onCapabilitiesChanged(network, networkCapabilities);
                    if (networkCapabilities != null) {
                        if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                            Toast.makeText(context, "WiFi is connected!", Toast.LENGTH_SHORT).show();
                        } else if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                            Toast.makeText(context, "Cellular network is connected!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            };
            connectivityManager.registerNetworkCallback(networkRequest, networkCallback);

        } catch(Exception e) {
            Log.d("startObserving", "startObserving error: ", e);
        }
    }

    public void stopObserving() {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        connectivityManager.unregisterNetworkCallback(networkCallback);
    }

    public boolean isConnected() {
        return isConnected;
    }

    private void checkInternetConnection() {
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                try {
                    boolean isReachable = InetAddress.getByName("google.com").isReachable(5000);
                    if (!isReachable) {
                        isInternet = false;
                        timer.cancel();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        timer.schedule(task, 0, 5000); // Check every 5 seconds
    }
}
