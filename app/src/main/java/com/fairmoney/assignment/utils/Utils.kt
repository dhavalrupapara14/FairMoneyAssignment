package com.fairmoney.assignment.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.text.format.DateFormat
import java.text.SimpleDateFormat
import java.util.*

const val SERVER_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
const val GMT = "GMT"
const val USER_DETAIL_DOB_FORMAT = "MMMM dd, yyyy"

fun getFormattedDobForUserDetails(
    date: String
): String? {
    return try {
        val sdf = SimpleDateFormat(SERVER_DATE_FORMAT, Locale.ENGLISH)
        sdf.timeZone = TimeZone.getTimeZone(GMT)

        DateFormat.format(USER_DETAIL_DOB_FORMAT, sdf.parse(date).time).toString()
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

fun isNetworkConnected(
    context: Context
): Boolean {
    return try {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val nw = connectivityManager.activeNetwork ?: return false
            val actNw = connectivityManager.getNetworkCapabilities(nw) ?: return false
            return when {
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                //for other device how are able to connect with Ethernet
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                //for check internet over Bluetooth
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH) -> true
                else -> false
            }
        } else {
            val nwInfo = connectivityManager.activeNetworkInfo ?: return false
            return nwInfo.isConnected
        }
    } catch (e: Exception) {
        e.printStackTrace()
        false
    }
}

