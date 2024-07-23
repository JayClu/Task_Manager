package com.edge.light.ledscreen.edgescreen.utils.ex

import android.app.ActivityManager
import android.app.Service
import android.content.Context
import android.content.Context.ACTIVITY_SERVICE
import android.content.Intent
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import java.io.IOException
import java.io.InputStream


fun Context.connectService(pClass: Class<out Service>) {
    startService(Intent(this, pClass))
}

fun Context.endService(pClass: Class<out Service>) {
    stopService(Intent(this, pClass))
}


fun Context.showToast(msg: String, isShowDurationLong: Boolean = false) {
    val duration = if (isShowDurationLong) Toast.LENGTH_LONG else Toast.LENGTH_SHORT
    Toast.makeText(this, msg, duration).show()
}

fun Fragment.showToast(msg: String, isShowDurationLong: Boolean = false) {
    requireContext().showToast(msg, isShowDurationLong)
}

fun Context.hasPermission(permission: String): Boolean {
    return ContextCompat.checkSelfPermission(
        this,
        permission
    ) == PackageManager.PERMISSION_GRANTED
}

fun Context.loadJsonFromAsset(path: String): String {
    var json: String = ""
    try {
        val ios: InputStream = assets.open(path)
        val size = ios.available()
        val buffer = ByteArray(size)
        ios.read(buffer)
        ios.close()
        json = String(buffer, Charsets.UTF_8)
    } catch (e: IOException) {
        e.printStackTrace()
    }
    return json
}

fun Context.isNetworkAdsConnected(): Boolean {
    val connectivityManager =
        getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val capabilities =
        connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            ?: return false
    return when {
        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) && capabilities.hasCapability(
            NetworkCapabilities.NET_CAPABILITY_VALIDATED
        ) -> true

        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) && capabilities.hasCapability(
            NetworkCapabilities.NET_CAPABILITY_VALIDATED
        ) -> true

        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) && capabilities.hasCapability(
            NetworkCapabilities.NET_CAPABILITY_VALIDATED
        ) -> true

        else -> false
    }
}

fun Context.isServiceRunning(serviceClass: Class<*>): Boolean {
    val manager = getSystemService(ACTIVITY_SERVICE) as ActivityManager
    val services = manager.getRunningServices(Integer.MAX_VALUE)
    for (service in services) {
        if (serviceClass.name == service.service.className) {
            return true
        }
    }
    return false
}

fun Fragment.isServiceRunning(serviceClass: Class<*>): Boolean {
    val manager = requireContext().getSystemService(ACTIVITY_SERVICE) as ActivityManager
    val services = manager.getRunningServices(Integer.MAX_VALUE)
    for (service in services) {
        if (serviceClass.name == service.service.className) {
            return true
        }
    }
    return false
}


