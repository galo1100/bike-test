package galo.db.biketest.presentation.telemetry.mapper

import galo.db.biketest.R
import galo.db.biketest.di.DeviceLocale
import galo.db.biketest.di.DeviceZone
import galo.db.biketest.domain.model.Battery
import galo.db.biketest.domain.model.Bike
import galo.db.biketest.domain.model.BikeTelemetry
import galo.db.biketest.domain.model.Celsius
import galo.db.biketest.domain.model.ChargingState
import galo.db.biketest.domain.model.Diagnostics
import galo.db.biketest.domain.model.Horsepower
import galo.db.biketest.domain.model.Kilometers
import galo.db.biketest.domain.model.KilometersPerHour
import galo.db.biketest.domain.model.Motor
import galo.db.biketest.domain.model.Percent
import galo.db.biketest.domain.model.PowerMap
import galo.db.biketest.domain.model.RideSettings
import galo.db.biketest.domain.model.Session
import galo.db.biketest.domain.model.Severity
import galo.db.biketest.domain.model.Warning
import galo.db.biketest.presentation.telemetry.entities.BatteryUi
import galo.db.biketest.presentation.telemetry.entities.BikeTelemetryUiModel
import galo.db.biketest.presentation.telemetry.entities.BikeUi
import galo.db.biketest.presentation.telemetry.entities.DiagnosticsUi
import galo.db.biketest.presentation.telemetry.entities.PowerUi
import galo.db.biketest.presentation.telemetry.entities.SessionUi
import galo.db.biketest.presentation.telemetry.entities.SeverityUi
import galo.db.biketest.presentation.telemetry.entities.WarningUi
import kotlinx.collections.immutable.toImmutableList
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale
import javax.inject.Inject
import kotlin.time.Duration
import kotlin.time.Instant

class BikeTelemetryUiMapper @Inject constructor(
    @DeviceLocale private val locale: Locale,
    @DeviceZone private val zoneId: ZoneId,
) {

    private val timestampFormatter: DateTimeFormatter =
        DateTimeFormatter.ofPattern(TIMESTAMP_PATTERN, locale).withZone(zoneId)

    fun map(telemetry: BikeTelemetry): BikeTelemetryUiModel = BikeTelemetryUiModel(
        bike = telemetry.bike.toUi(telemetry.timestamp),
        battery = telemetry.battery.toUi(),
        session = telemetry.session.toUi(),
        power = toPowerUi(telemetry.motor, telemetry.rideSettings),
        diagnostics = telemetry.diagnostics.toUi(),
    )

    private fun Bike.toUi(timestamp: Instant) = BikeUi(
        title = "$model $variant",
        firmwareVersion = firmwareVersion,
        imageUrl = imageUrl,
        updatedAt = timestamp.format(),
    )

    private fun Battery.toUi() = BatteryUi(
        stateOfCharge = stateOfCharge.format(),
        chargeFraction = stateOfCharge.fraction(),
        range = estimatedRange.formatWhole(),
        temperature = temperature.format(),
        chargingStateRes = chargingState.labelRes(),
    )

    private fun Session.toUi() = SessionUi(
        duration = duration.format(),
        distance = distance.format(),
        maxSpeed = maxSpeed.format(),
    )

    private fun toPowerUi(motor: Motor, rideSettings: RideSettings) = PowerUi(
        output = motor.power.format(),
        maxOutput = rideSettings.maxPower.formatWhole(),
        outputFraction = motor.power.fractionOf(rideSettings.maxPower),
        motorTemperature = motor.temperature.format(),
        powerMapRes = rideSettings.powerMap.labelRes(),
        engineBraking = rideSettings.engineBraking.format(),
        engineBrakingFraction = rideSettings.engineBraking.fraction(),
        regen = rideSettings.regen.format(),
        regenFraction = rideSettings.regen.fraction(),
    )

    private fun Diagnostics.toUi() = DiagnosticsUi(
        warnings = warnings.map { it.toUi() }.toImmutableList(),
        faultCodes = faultCodes.toImmutableList(),
    )

    private fun Warning.toUi() = WarningUi(
        code = code,
        message = message,
        severity = severity.toUi(),
    )

    private fun Instant.format(): String =
        timestampFormatter.format(java.time.Instant.ofEpochSecond(epochSeconds))

    private fun Duration.format(): String = toComponents { hours, minutes, seconds, _ ->
        when {
            hours > 0L -> String.format(locale, "%dh %02dm", hours, minutes)
            minutes > 0 -> String.format(locale, "%dm %02ds", minutes, seconds)
            else -> String.format(locale, "%ds", seconds)
        }
    }

    private fun Percent.format(): String = String.format(locale, "%d%%", value)

    private fun Percent.fraction(): Float = (value / 100f).coerceIn(0f, 1f)

    private fun Celsius.format(): String = String.format(locale, "%.1f °C", value)

    private fun Kilometers.format(): String = String.format(locale, "%.1f km", value)

    private fun Kilometers.formatWhole(): String = String.format(locale, "%.0f km", value)

    private fun KilometersPerHour.format(): String = String.format(locale, "%.1f km/h", value)

    private fun Horsepower.format(): String = String.format(locale, "%.1f hp", value)

    private fun Horsepower.formatWhole(): String = String.format(locale, "%.0f hp", value)

    private fun Horsepower.fractionOf(max: Horsepower): Float =
        if (max.value <= 0.0) 0f else (value / max.value).toFloat().coerceIn(0f, 1f)

    private fun ChargingState.labelRes(): Int = when (this) {
        ChargingState.CHARGING -> R.string.telemetry_charging_state_charging
        ChargingState.DISCHARGING -> R.string.telemetry_charging_state_discharging
        ChargingState.UNKNOWN -> R.string.telemetry_charging_state_unknown
    }

    private fun PowerMap.labelRes(): Int = when (this) {
        PowerMap.ENDURO -> R.string.telemetry_power_map_enduro
        PowerMap.MOTOCROSS -> R.string.telemetry_power_map_motocross
        PowerMap.SUPERMOTO -> R.string.telemetry_power_map_supermoto
        PowerMap.UNKNOWN -> R.string.telemetry_power_map_unknown
    }

    private fun Severity.toUi(): SeverityUi = when (this) {
        Severity.INFO -> SeverityUi.INFO
        Severity.WARNING -> SeverityUi.WARNING
        Severity.CRITICAL -> SeverityUi.CRITICAL
        Severity.UNKNOWN -> SeverityUi.UNKNOWN
    }

    private companion object {
        const val TIMESTAMP_PATTERN = "d MMM yyyy, HH:mm"
    }
}
