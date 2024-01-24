package com.example.contentprovider;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.IBinder;
import android.widget.Toast;

import java.lang.reflect.Method;

public class WifiHotspotService extends Service {

    private WifiManager wifiManager;
    private boolean isHotspotEnabled = false;

    @Override
    public void onCreate() {
        super.onCreate();
        wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startStopHotspot();
        return START_STICKY;
    }

    private void startStopHotspot() {
        if (!isHotspotEnabled) {
            // Start Wi-Fi hotspot
            if (turnOnHotspot()) {
                Toast.makeText(this, "Wi-Fi Hotspot Enabled", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Failed to Enable Wi-Fi Hotspot", Toast.LENGTH_SHORT).show();
            }
        } else {
            // Stop Wi-Fi hotspot
            if (turnOffHotspot()) {
                Toast.makeText(this, "Wi-Fi Hotspot Disabled", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Failed to Disable Wi-Fi Hotspot", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean turnOnHotspot() {
        try {
            Method method = wifiManager.getClass().getMethod("setWifiApEnabled", WifiConfiguration.class, boolean.class);
            WifiConfiguration wifiConfig = new WifiConfiguration();
            wifiConfig.SSID = "MyHotspot";
            wifiConfig.preSharedKey = "password123";
            wifiConfig.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
            return (boolean) method.invoke(wifiManager, wifiConfig, true);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean turnOffHotspot() {
        try {
            Method method = wifiManager.getClass().getMethod("setWifiApEnabled", WifiConfiguration.class, boolean.class);
            return (boolean) method.invoke(wifiManager, null, false);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
