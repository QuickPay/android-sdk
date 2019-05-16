package net.quickpay.quickpaysdk.networking

import android.content.Context

import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley

// Singleton class, to access an instance of Volley to send http requests.
internal class NetworkUtility

// Private constructor for Singleton pattern.
private constructor(context: Context) {
    private val requestQueue: RequestQueue = Volley.newRequestQueue(context.applicationContext)

    fun <T> addNetworkRequest(req: Request<T>) {
        requestQueue.add(req)
    }

    companion object {

        private lateinit var instance: NetworkUtility

        @Synchronized
        fun getInstance(context: Context): NetworkUtility {
            if (instance == null) {
                instance = NetworkUtility(context)
                instance.requestQueue.start()
            }
            return instance
        }

        /**
         * Use this method to retrieve an instance of NetworkUtility, which allows you to add http requests to the volley.RequestQueue.
         * You are required the call NetworkUtility.getInstance(context) first, otherwise an IllegalStateExpection is thrown.
         * @return An instance of NetworkUtility.
         */
        fun getInstance(): NetworkUtility {
            return if (instance == null) {
                throw IllegalStateException("No NetworkUtility has been created. You need to call NetworkUtility.getInstance( context ) first.")
            } else {
                instance
            }
        }
    }

}
