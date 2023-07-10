package com.digaus.capacitor.wifi;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiNetworkSpecifier;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import com.getcapacitor.Bridge;
import com.getcapacitor.PluginCall;
import android.net.wifi.WifiNetworkSuggestion;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import java.util.ArrayList;
import java.util.List;
import java.io.IOException;
import java.net.InetAddress;

public class WifiService {
    private static final int REQUEST_WIFI_SETTINGS = 1;
    private static String TAG = "WifiService";
    private static final int API_VERSION = Build.VERSION.SDK_INT;
    private PluginCall savedCall;
    private ConnectivityManager.NetworkCallback networkCallback;
    WifiManager wifiManager;
    ConnectivityManager connectivityManager;
    Context context;
    Bridge bridge;
    private ActivityResultLauncher<Intent> wifiAddNetworkLauncher;

    public void load(Bridge bridge) {
      this.bridge = bridge;
      this.wifiManager = (WifiManager) this.bridge.getActivity().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
      this.connectivityManager = (ConnectivityManager) this.bridge.getActivity().getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
      this.context = this.bridge.getContext();

      wifiAddNetworkLauncher = bridge.registerForActivityResult(
        new ActivityResultContracts.StartActivityForResult(),
        result -> {
            if (result.getResultCode() == Activity.RESULT_OK) {
                Toast.makeText(context, "Network suggestion added successfully", Toast.LENGTH_SHORT).show();
                Log.d("WifiService", "Network suggestion added successfully");
            } else {
                Toast.makeText(context, "Failed to add network suggestion", Toast.LENGTH_SHORT).show();
                Log.d("WifiService", "Failed to add network suggestion");
            }
        }
      );
    }
    // public void connect(PluginCall call) {
    //     // ...
    //     this.savedCall = call;
    //     String ssid = call.getString("ssid");
    //     String password = call.getString("password");
    //     boolean isHiddenSsid = false;

    //     if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
    //         List<WifiNetworkSuggestion> existingSuggestions = wifiManager.getNetworkSuggestions();
    //         Log.d("existingSuggestions", "existingSuggestions " + existingSuggestions);

    //         boolean suggestionExists = false;
    //         for (WifiNetworkSuggestion suggestion : existingSuggestions) {
    //             if (suggestion.getSsid().equals(ssid)) {
    //                 suggestionExists = true;
    //                 break;
    //             }
    //         }
    //         Toast.makeText(context, "suggestionExists " + suggestionExists, Toast.LENGTH_SHORT).show();

    //         if (suggestionExists) {
    //             WifiNetworkSpecifier specifier = new WifiNetworkSpecifier.Builder()
    //                     .setSsid(ssid)
    //                     .build();

    //             NetworkRequest networkRequest = new NetworkRequest.Builder()
    //                     .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
    //                     .setNetworkSpecifier(specifier)
    //                     .build();
    //             final WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
    //             final int status = wifiManager.addNetworkSuggestions(suggestionsList);
    //             if (status != WifiManager.STATUS_NETWORK_SUGGESTIONS_SUCCESS) {
    //             // do error handling here…
    //             }
    //             networkCallback = new ConnectivityManager.NetworkCallback() {
    //                 @Override
    //                 public void onAvailable(Network network) {
    //                     Toast.makeText(context, "Connected to Wi-Fi network: " + ssid, Toast.LENGTH_SHORT).show();
    //                     Log.d("WifiService", "Connected to Wi-Fi network: " + ssid);

    //                 }

    //                 @Override
    //                 public void onUnavailable() {
    //                     Toast.makeText(context, "Failed to connect to Wi-Fi network: " + ssid, Toast.LENGTH_SHORT).show();
    //                     Log.d("WifiService", "Failed to connect to Wi-Fi network: " + ssid);

    //                 }
    //             };

    //             connectivityManager.requestNetwork(networkRequest, networkCallback);
    //             Toast.makeText(context, "Connecting to Wi-Fi network: " + ssid, Toast.LENGTH_SHORT).show();
    //             Log.d("WifiService", "Connecting to Wi-Fi network: " + ssid);
    //         } else {
    //             ArrayList<WifiNetworkSuggestion> suggestions = new ArrayList<>();

    //             // WPA2 configuration
    //             suggestions.add(
    //                     new WifiNetworkSuggestion.Builder()
    //                             .setSsid(ssid)
    //                             .setWpa2Passphrase(password)
    //                             .build()
    //             );

    //             Bundle bundle = new Bundle();
    //             bundle.putParcelableArrayList("android.provider.extra.WIFI_NETWORK_LIST", suggestions);
    //             Intent intent = new Intent(Settings.ACTION_WIFI_ADD_NETWORKS);
    //             intent.putExtras(bundle);

    //             ((Activity) context).startActivityForResult(intent, REQUEST_WIFI_SETTINGS);
    //             Toast.makeText(context, "Adding network suggestion...", Toast.LENGTH_SHORT).show();
    //             Log.d("WifiService", "Adding network suggestion...");

    //         }
    //     } else {
    //         WifiConfiguration wifiConfig = new WifiConfiguration();
    //         wifiConfig.SSID = "\"" + ssid + "\"";
    //         wifiConfig.preSharedKey = "\"" + password + "\"";
    //         int networkId = wifiManager.addNetwork(wifiConfig);
    //         if (networkId != -1) {
    //             wifiManager.enableNetwork(networkId, true);
    //             wifiManager.reconnect();

    //             Toast.makeText(context, "Connecting to Wi-Fi network...", Toast.LENGTH_SHORT).show();
    //             Log.d("WifiService", "Connecting to Wi-Fi network...");
    //         } else {
    //             Toast.makeText(context, "Failed to add network configuration", Toast.LENGTH_SHORT).show();
    //             Log.d("WifiService", "Failed to add network configuration");
    //         }

    //     }
    // }

        public void connect(PluginCall call) {
        // Open the Wi-Fi settings
        Intent wifiSettingsIntent = new Intent(Settings.ACTION_WIFI_SETTINGS);
        context.startActivity(wifiSettingsIntent);
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.Q) {
            NetworkRequest networkRequest = new NetworkRequest.Builder()
                    .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
                    .build();
            // Unregister the receiver
            networkCallback = new ConnectivityManager.NetworkCallback() {
                @Override
                public void onAvailable(Network network) {
                    Log.d("WifiService", "Connected to Wi-Fi network: ");
                    Intent appIntent = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
                    appIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(appIntent);
                    Toast.makeText(context, "Connected to Wi-Fi network: ", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onUnavailable() {
                    Toast.makeText(context, "Failed to connect to Wi-Fi network: ", Toast.LENGTH_SHORT).show();
                    Log.d("WifiService", "Failed to connect to Wi-Fi network: ");

                }
            };

            connectivityManager.requestNetwork(networkRequest, networkCallback);
            Toast.makeText(context, "Connecting to Wi-Fi network: ", Toast.LENGTH_SHORT).show();
        }
    }

    public void disconnect(PluginCall call) {
        if (wifiManager.isWifiEnabled()) {
            wifiManager.disconnect();

            Toast.makeText(context, "Disconnecting from Wi-Fi network...", Toast.LENGTH_SHORT).show();
            Log.d("WifiService", "Disconnecting from Wi-Fi network...");
        } else {
            Toast.makeText(context, "Wi-Fi is not enabled", Toast.LENGTH_SHORT).show();
            Log.d("WifiService", "Wi-Fi is not enabled");
        }
    }
}

    // public void connect(PluginCall call) {
    //     // Open the Wi-Fi settings
    //     Intent wifiSettingsIntent = new Intent(Settings.ACTION_WIFI_SETTINGS);
    //     context.startActivity(wifiSettingsIntent);

    //             NetworkRequest networkRequest = new NetworkRequest.Builder()
    //                     .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
    //                     .build();
    //                     // Unregister the receiver
    //                     networkCallback = new ConnectivityManager.NetworkCallback() {
    //                         @Override
    //                         public void onAvailable(Network network) {
    //                             Toast.makeText(context, "Connected to Wi-Fi network: ", Toast.LENGTH_SHORT).show();
    //                             Log.d("WifiService", "Connected to Wi-Fi network: " );

    //                         }

    //                         @Override
    //                         public void onUnavailable() {
    //                             Toast.makeText(context, "Failed to connect to Wi-Fi network: " , Toast.LENGTH_SHORT).show();
    //                             Log.d("WifiService", "Failed to connect to Wi-Fi network: ");

    //                         }
    //                     };

    //                     connectivityManager.requestNetwork(networkRequest, networkCallback);
    //                     Toast.makeText(context, "Connecting to Wi-Fi network: " , Toast.LENGTH_SHORT).show();
    //                     // TODO: Implement your logic to handle the result of the connection
    //                     // For example, you can update UI, make API calls, or perform any other necessary actions

    //                     // Return true to indicate a successful connection

    //     // Register the broadcast receiver to listen for changes in the Wi-Fi connection state
    //     IntentFilter filter = new IntentFilter(WifiManager.NETWORK_STATE_CHANGED_ACTION);

    //     // Return false to indicate that the connection is not yet established

    // }
