package sashjakk.weather.app.connectivity

import android.content.Context
import android.net.ConnectivityManager
import androidx.core.content.getSystemService

class DefaultConnectivityProvider(
    private val context: Context
) : ConnectivityProvider {

    private val connectivityManager by lazy {
        checkNotNull(context.getSystemService<ConnectivityManager>())
    }

    override val isConnected: Boolean
        get() = connectivityManager.activeNetworkInfo?.isConnectedOrConnecting ?: false
}