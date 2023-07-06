package com.digaus.capacitor.wifi;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;
import com.getcapacitor.Bridge;
import com.getcapacitor.PluginCall;
import android.net.wifi.WifiNetworkSuggestion;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

public class WifiService {
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
                // Network suggestion added successfully
                Toast.makeText(context, "Network suggestion added successfully", Toast.LENGTH_SHORT).show();
                Log.d("WifiService", "Network suggestion added successfully");
                // Connect to the network or perform any necessary actions
            } else {
                // Failed to add network suggestion
                Toast.makeText(context, "Failed to add network suggestion", Toast.LENGTH_SHORT).show();
                Log.d("WifiService", "Failed to add network suggestion");

                // Handle the failure
            }
        }
      );
    }

    public void connect(PluginCall call) {
        this.savedCall = call;
        String ssid = call.getString("ssid");
        String password = call.getString("password");
        boolean isHiddenSsid = false;
        if (call.hasOption("isHiddenSsid")) {
            isHiddenSsid = call.getBoolean("isHiddenSsid");
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            // Create an Intent for the ACTION_WIFI_ADD_NETWORKS
            Intent intent = new Intent(Settings.ACTION_WIFI_ADD_NETWORKS);

            // Set the extras for the network suggestion
            WifiNetworkSuggestion suggestion = new WifiNetworkSuggestion.Builder()
                    .setSsid(ssid)
                    .setWpa2Passphrase(password)
                    .setIsHiddenSsid(isHiddenSsid)
                    .build();

            // Start the activity to add the network suggestion
            wifiAddNetworkLauncher.launch(intent);

            // Show a toast message to indicate that the network suggestion is being added
            Toast.makeText(context, "Adding network suggestion...", Toast.LENGTH_SHORT).show();
            Log.d("WifiService", "Adding network suggestion...");
        } else {
            // Connect to the network using the previous implementation
            // ...
        }
    }
    public void disconnect(PluginCall call) {
        // Check if Wi-Fi is enabled
        if (wifiManager.isWifiEnabled()) {
            // Disconnect from the currently connected Wi-Fi network
            wifiManager.disconnect();

            // Show a toast message to indicate that the disconnection is in progress
            Toast.makeText(context, "Disconnecting from Wi-Fi network...", Toast.LENGTH_SHORT).show();
            Log.d("WifiService", "Disconnecting from Wi-Fi network...");
        } else {
            // Wi-Fi is not enabled
            // Show a toast message to indicate that Wi-Fi is not enabled
            Toast.makeText(context, "Wi-Fi is not enabled", Toast.LENGTH_SHORT).show();
            Log.d("WifiService", "Wi-Fi is not enabled");
        }
    }
}