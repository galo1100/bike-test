package galo.db.biketest.presentation.telemetry.entities

import androidx.annotation.StringRes
import kotlinx.collections.immutable.ImmutableList

data class BikeTelemetryUiModel(
    val bike: BikeUi,
    val battery: BatteryUi,
    val session: SessionUi,
    val power: PowerUi,
    val diagnostics: DiagnosticsUi,
)

data class BikeUi(
    val title: String,
    val firmwareVersion: String,
    val imageUrl: String,
    val updatedAt: String,
)

data class BatteryUi(
    val stateOfCharge: String,
    val chargeFraction: Float,
    val range: String,
    val temperature: String,
    @StringRes val chargingStateRes: Int,
)

data class SessionUi(
    val duration: String,
    val distance: String,
    val maxSpeed: String,
)

data class PowerUi(
    val output: String,
    val maxOutput: String,
    val outputFraction: Float,
    val motorTemperature: String,
    @StringRes val powerMapRes: Int,
    val engineBraking: String,
    val engineBrakingFraction: Float,
    val regen: String,
    val regenFraction: Float,
)

data class DiagnosticsUi(
    val warnings: ImmutableList<WarningUi>,
    val faultCodes: ImmutableList<String>,
) {
    val isEmpty: Boolean get() = warnings.isEmpty() && faultCodes.isEmpty()
}

data class WarningUi(
    val code: String,
    val message: String,
    val severity: SeverityUi,
)

enum class SeverityUi {
    INFO,
    WARNING,
    CRITICAL,
    UNKNOWN,
}
