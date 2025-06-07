package com.mertyigit0.trustedtimeapisample

import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.abs

/**
 * Utility functions for time calculations and formatting
 */
object TimeUtils {
    
    private val dateFormat = SimpleDateFormat(Constants.DATE_FORMAT_PATTERN, Locale.getDefault())
    
    /**
     * Format milliseconds to a readable date string
     */
    fun formatTime(timeMillis: Long): String {
        return dateFormat.format(Date(timeMillis))
    }
    
    /**
     * Calculate human-readable time difference with millisecond precision
     */
    fun formatTimeDifference(timeDifferenceMs: Long): String {
        val timeDifferenceAbs = abs(timeDifferenceMs)
        
        val diffMilliseconds = timeDifferenceAbs % 1000
        val diffSeconds = (timeDifferenceAbs / 1000) % 60
        val diffMinutes = (timeDifferenceAbs / (1000 * 60)) % 60
        val diffHours = (timeDifferenceAbs / (1000 * 60 * 60)) % 24
        val diffDays = timeDifferenceAbs / (1000 * 60 * 60 * 24)
        
        val sign = if (timeDifferenceMs >= 0) "+" else "-"
        
        return when {
            diffDays > 0 -> "${sign}${diffDays}d ${diffHours}h ${diffMinutes}m ${diffSeconds}s ${diffMilliseconds}ms"
            diffHours > 0 -> "${sign}${diffHours}h ${diffMinutes}m ${diffSeconds}s ${diffMilliseconds}ms"
            diffMinutes > 0 -> "${sign}${diffMinutes}m ${diffSeconds}s ${diffMilliseconds}ms"
            diffSeconds > 0 -> "${sign}${diffSeconds}s ${diffMilliseconds}ms"
            else -> "${sign}${diffMilliseconds}ms"
        }
    }
    
    /**
     * Format elapsed time since last update
     */
    fun formatElapsedTime(elapsedTimeMs: Long): String {
        val elapsedSeconds = (elapsedTimeMs / 1000) % 60
        val elapsedMinutes = elapsedTimeMs / (1000 * 60)
        
        return if (elapsedMinutes > 0) {
            "${elapsedMinutes}m ${elapsedSeconds}s"
        } else {
            "${elapsedSeconds}s"
        }
    }
    
    /**
     * Check if time difference indicates tampering
     */
    fun isTamperingDetected(timeDifferenceMs: Long): Boolean {
        return abs(timeDifferenceMs) > Constants.TAMPERING_THRESHOLD_MS
    }
    
    /**
     * Check if trusted time is outdated
     */
    fun isTrustedTimeOutdated(lastUpdateTime: Long): Boolean {
        val timeSinceUpdate = System.currentTimeMillis() - lastUpdateTime
        return timeSinceUpdate > Constants.OUTDATED_THRESHOLD_MS
    }
    
    /**
     * Get time difference description
     */
    fun getTimeDifferenceDescription(timeDifferenceMs: Long): String {
        return "Trusted time ${if (timeDifferenceMs >= 0) "ahead" else "behind"} system time"
    }
} 