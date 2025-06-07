package com.mertyigit0.trustedtimeapisample

import android.app.Application
import com.google.android.gms.time.TrustedTime
import com.google.android.gms.time.TrustedTimeClient

/**
 * Custom Application class to initialize TrustedTimeClient early in the app lifecycle.
 * This follows the recommended approach from the TrustedTime API documentation.
 */
class TrustedTimeApplication : Application() {
    
    // TrustedTimeClient instance accessible throughout the app
    var trustedTimeClient: TrustedTimeClient? = null
        private set
    
    // Flag to track if client initialization failed
    var clientInitializationFailed: Boolean = false
        private set

    override fun onCreate() {
        super.onCreate()
        
        // Initialize TrustedTimeClient as early as possible in app lifecycle
        val initializeTrustedTimeClientTask = TrustedTime.createClient(this)
        
        initializeTrustedTimeClientTask.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // Store the client for use throughout the app
                trustedTimeClient = task.result
                clientInitializationFailed = false
            } else {
                // Handle initialization failure
                val exception = task.exception
                clientInitializationFailed = true
                // In a production app, you might want to log this error
                exception?.printStackTrace()
            }
        }
    }
} 