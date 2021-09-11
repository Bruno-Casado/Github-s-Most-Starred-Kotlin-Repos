package br.com.brunocasado.githubsmoststarredrepositoriesinkotlin.core.network

import android.content.Context
import android.net.ConnectivityManager
import javax.inject.Inject

@Suppress("DEPRECATION")
class NetworkInfoImpl @Inject constructor(private val context: Context) : NetworkInfo {
    override fun isNetworkAvailable(): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val activeNetworkInfo: android.net.NetworkInfo? = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }

}