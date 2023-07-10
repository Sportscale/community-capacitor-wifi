package com.digaus.capacitor.wifi;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;

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
    Bridge bridge;

    public void load(Bridge bridge) {
        this.bridge = bridge;
        this.context = this.bridge.getContext();
    }

    public void startObserving() {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkRequest networkRequest = new NetworkRequest.Builder()
                .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                .build();

        networkCallback = new ConnectivityManager.NetworkCallback() {
            @Override
            public void onAvailable(Network network) {
                super.onAvailable(network);
                isConnected = true;
                checkInternetConnection();
            }

            @Override
            public void onLost(Network network) {
                super.onLost(network);
                isConnected = false;
            }
        };

        connectivityManager.registerNetworkCallback(networkRequest, networkCallback);
    }

    public void stopObserving() {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        connectivityManager.unregisterNetworkCallback(networkCallback);
    }

    public boolean isConnected() {
        return isConnected;
    }

    // private void checkInternetConnection() {
    //     new Thread(new Runnable() {
    //         @Override
    //         public void run() {
    //             try {
    //                 while (isConnected) {
    //                     boolean isReachable = InetAddress.getByName("google.com").isReachable(5000);
    //                     if (!isReachable) {
    //                         isConnected = false;
    //                     }
    //                     Thread.sleep(5000); // Wait for 5 seconds before checking again
    //                 }
    //             } catch (IOException | InterruptedException e) {
    //                 e.printStackTrace();
    //             }
    //         }
    //     }).start();
    // }
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
