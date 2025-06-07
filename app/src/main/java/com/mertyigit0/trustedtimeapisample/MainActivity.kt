package com.mertyigit0.trustedtimeapisample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.mertyigit0.trustedtimeapisample.ui.components.*
import com.mertyigit0.trustedtimeapisample.ui.theme.TrustedTimeAPISampleTheme
import kotlin.math.abs

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TrustedTimeAPISampleTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    TrustedTimeScreen()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrustedTimeScreen() {
    // Get the application instance to access TrustedTimeClient
    val app = LocalContext.current.applicationContext as TrustedTimeApplication
    
    // State variables for UI
    var trustedTimeMillis by remember { mutableLongStateOf(0L) }
    var systemTimeMillis by remember { mutableLongStateOf(0L) }
    var errorMessage by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var lastUpdateTime by remember { mutableLongStateOf(0L) }
    
    // Function to fetch trusted time
    fun fetchTrustedTime() {
        isLoading = true
        errorMessage = ""
        
        // Get current system time for comparison
        systemTimeMillis = System.currentTimeMillis()
        
        // Check if TrustedTimeClient is available
        val client = app.trustedTimeClient
        if (client == null) {
            errorMessage = if (app.clientInitializationFailed) {
                Constants.ERROR_CLIENT_INIT_FAILED
            } else {
                Constants.ERROR_CLIENT_INITIALIZING
            }
            isLoading = false
            return
        }
        
        try {
            // Get trusted time from the API
            val trustedTime = client.computeCurrentUnixEpochMillis()
            if (trustedTime != null) {
                trustedTimeMillis = trustedTime
                lastUpdateTime = System.currentTimeMillis()
                errorMessage = ""
            } else {
                errorMessage = Constants.ERROR_TIME_UNAVAILABLE
            }
        } catch (e: Exception) {
            errorMessage = "Error retrieving trusted time: ${e.message}"
        }
        
        isLoading = false
    }
    
    // Fetch trusted time on first composition
    LaunchedEffect(Unit) {
        fetchTrustedTime()
    }
    
    Scaffold(
        topBar = {
            ModernAppBar(title = "TrustedTime API")
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))
            
            // Refresh button
            LoadingButton(
                text = "Refresh Time",
                isLoading = isLoading,
                onClick = { fetchTrustedTime() },
                modifier = Modifier.fillMaxWidth()
            )
            
            // Error message
            if (errorMessage.isNotEmpty()) {
                ErrorCard(message = errorMessage)
            }
            
            // Time information cards
            if (trustedTimeMillis > 0 && systemTimeMillis > 0) {
                // Trusted Time Card
                TimeDisplayCard(
                    title = "Trusted Time",
                    time = TimeUtils.formatTime(trustedTimeMillis),
                    subtitle = "From Google's secure time servers",
                    icon = TimeIcons.TrustedTime
                )
                
                // System Time Card
                TimeDisplayCard(
                    title = "System Time",
                    time = TimeUtils.formatTime(systemTimeMillis),
                    subtitle = "From device's local clock",
                    icon = TimeIcons.SystemTime
                )
                
                // Time difference analysis
                val timeDifferenceMs = trustedTimeMillis - systemTimeMillis
                val timeDifferenceAbs = abs(timeDifferenceMs)
                val diffString = TimeUtils.formatTimeDifference(timeDifferenceMs)
                val isWarning = TimeUtils.isTamperingDetected(timeDifferenceMs)
                
                // Time difference card
                TimeDifferenceCard(
                    difference = diffString,
                    description = TimeUtils.getTimeDifferenceDescription(timeDifferenceMs),
                    isWarning = isWarning
                )
                
                // Warning messages
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Tampering warning
                    if (isWarning) {
                        WarningCard(
                            title = "Time Tampering Detected",
                            message = "Device time may have been tampered with. Time difference exceeds ${Constants.TAMPERING_THRESHOLD_MS / 1000} seconds ($diffString)."
                        )
                    }
                    
                    // Outdated trusted time warning
                    if (TimeUtils.isTrustedTimeOutdated(lastUpdateTime)) {
                        val timeSinceUpdate = System.currentTimeMillis() - lastUpdateTime
                        val updateDiffString = TimeUtils.formatElapsedTime(timeSinceUpdate)
                        
                        WarningCard(
                            title = "Trusted Time Outdated",
                            message = "Trusted time is $updateDiffString old. Consider refreshing for more accurate time."
                        )
                    }
                }
            }
            
            // How it works info card
            InfoCard(
                title = "How TrustedTime API Works",
                content = """
                    • Provides accurate time from Google's secure infrastructure
                    • Compares with device's system time to detect tampering
                    • Shows time difference with millisecond precision
                    • Warns if time difference exceeds ${Constants.TAMPERING_THRESHOLD_MS / 1000} seconds
                    • Indicates when trusted time data is outdated (>${Constants.OUTDATED_THRESHOLD_MS / 1000} seconds)
                    • Periodic sync reduces network usage compared to NTP calls
                """.trimIndent()
            )
            

            
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}