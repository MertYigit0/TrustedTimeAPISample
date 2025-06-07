package com.mertyigit0.trustedtimeapisample

/**
 * Constants used throughout the TrustedTime API Sample app
 */
object Constants {
    // Time thresholds in milliseconds
    const val TAMPERING_THRESHOLD_MS = 5000L // 5 seconds
    const val OUTDATED_THRESHOLD_MS = 60000L // 1 minute
    
    // UI constants
    const val LOADING_INDICATOR_SIZE_DP = 16
    const val LOADING_STROKE_WIDTH_DP = 2
    
    // Date format
    const val DATE_FORMAT_PATTERN = "yyyy-MM-dd HH:mm:ss"
    
    // Error messages
    const val ERROR_CLIENT_INIT_FAILED = "TrustedTime client initialization failed. Please check your internet connection and Google Play Services."
    const val ERROR_CLIENT_INITIALIZING = "TrustedTime client is still initializing. Please wait and try again."
    const val ERROR_TIME_UNAVAILABLE = "Unable to retrieve trusted time. Device may not have synced with time servers yet."
} 