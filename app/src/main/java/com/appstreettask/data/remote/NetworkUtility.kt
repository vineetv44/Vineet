package com.appstreettask.data.remote

import android.content.Context
import android.net.ConnectivityManager
import android.net.wifi.WifiManager
import com.appstreettask.ui.AppStreetApplication

object NetworkUtility {

    val isNetworkAvailable: Boolean
        get() {
            val manager = sManager
            val wifiManager = wifiManager

            val netInfo = manager.activeNetworkInfo
            if (netInfo != null && netInfo.isConnected || wifiManager.isWifiEnabled
            ) {
                return true
            }
            return false
        }

    val wifiManager by lazy {
        AppStreetApplication.getAppContext()
            .applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
    }

    val sManager by lazy {
        AppStreetApplication.getAppContext()
            .applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }
}
