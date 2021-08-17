package com.ks.food_runner.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo

class ConnectionManager {

    fun checkConnection(context: Context):Boolean{
        val connectionManager=context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeState:NetworkInfo?=connectionManager.activeNetworkInfo

        if(activeState?.isConnected != null){
            return activeState?.isConnected
        }
        else{
            return false
        }
    }
}