package galo.db.biketest.domain.model

import kotlin.enums.enumEntries
import kotlin.time.Duration
import kotlin.time.Instant

data class BikeTelemetry(
    val bike: Bike,
    val timestamp: Instant,
    val battery: Battery,
    val motor: Motor,
    val rideSettings: RideSettings,
    val session: Session,
    val diagnostics: Diagnostics,
)

data class Bike(
    val model: String,
    val variant: String,
    val firmwareVersion: String,
    val imageUrl: String,
)

data class Battery(
    val stateOfCharge: Percent,
    val estimatedRange: Kilometers,
    val temperature: Celsius,
    val chargingState: ChargingState,
)

enum class ChargingState {
    CHARGING,
    DISCHARGING,
    UNKNOWN;

    companion object {
        fun from(raw: String): ChargingState = enumFromRaw(raw, UNKNOWN)
    }
}

data class Motor(
    val power: Horsepower,
    val temperature: Celsius,
)

data class RideSettings(
    val powerMap: PowerMap,
    val maxPower: Horsepower,
    val engineBraking: Percent,
    val regen: Percent,
)

enum class PowerMap {
    ENDURO,
    MOTOCROSS,
    SUPERMOTO,
    UNKNOWN;

    companion object {
        fun from(raw: String): PowerMap = enumFromRaw(raw, UNKNOWN)
    }
}

data class Session(
    val duration: Duration,
    val distance: Kilometers,
    val maxSpeed: KilometersPerHour,
)

data class Diagnostics(
    val faultCodes: List<String>,
    val warnings: List<Warning>,
)

data class Warning(
    val code: String,
    val message: String,
    val severity: Severity,
)

enum class Severity {
    INFO,
    WARNING,
    CRITICAL,
    UNKNOWN;

    companion object {
        fun from(raw: String): Severity = enumFromRaw(raw, UNKNOWN)
    }
}

private inline fun <reified T : Enum<T>> enumFromRaw(raw: String, fallback: T): T =
    enumEntries<T>().firstOrNull { it.name.equals(raw, ignoreCase = true) } ?: fallback
