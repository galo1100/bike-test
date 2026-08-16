package galo.db.biketest.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import dagger.hilt.android.AndroidEntryPoint
import galo.db.biketest.presentation.telemetry.BikeTelemetryScreen
import galo.db.biketest.presentation.theme.BikeTestTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BikeTestTheme {
                BikeTelemetryScreen()
            }
        }
    }
}
