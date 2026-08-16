package galo.db.biketest.presentation.telemetry.mapper

import galo.db.biketest.R
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
import kotlin.time.Duration
import kotlin.time.Instant

private const val TIMESTAMP_PATTERN = "d MMM yyyy, HH:mm"

internal fun BikeTelemetry.toUiModel(
    locale: Locale = Locale.getDefault(),
    zoneId: ZoneId = ZoneId.systemDefault(),
): BikeTelemetryUiModel {
    val timestampFormatter = DateTimeFormatter.ofPattern(TIMESTAMP_PATTERN, locale).withZone(zoneId)
    return BikeTelemetryUiModel(
        bike = bike.toUiModel(timestamp, timestampFormatter),
        battery = battery.toUiModel(locale),
        session = session.toUiModel(locale),
        power = toPowerUi(motor, rideSettings, locale),
        diagnostics = diagnostics.toUiModel(),
    )
}

private fun Bike.toUiModel(timestamp: Instant, timestampFormatter: DateTimeFormatter) = BikeUi(
    title = "$model $variant",
    firmwareVersion = firmwareVersion,
    imageUrl = imageUrl,
    updatedAt = timestamp.format(timestampFormatter),
)

private fun Battery.toUiModel(locale: Locale) = BatteryUi(
    stateOfCharge = stateOfCharge.format(locale),
    chargeFraction = stateOfCharge.fraction(),
    range = estimatedRange.formatWhole(locale),
    temperature = temperature.format(locale),
    chargingStateRes = chargingState.labelRes(),
)

private fun Session.toUiModel(locale: Locale) = SessionUi(
    duration = duration.format(locale),
    distance = distance.format(locale),
    maxSpeed = maxSpeed.format(locale),
)

private fun toPowerUi(motor: Motor, rideSettings: RideSettings, locale: Locale) = PowerUi(
    output = motor.power.format(locale),
    maxOutput = rideSettings.maxPower.formatWhole(locale),
    outputFraction = motor.power.fractionOf(rideSettings.maxPower),
    motorTemperature = motor.temperature.format(locale),
    powerMapRes = rideSettings.powerMap.labelRes(),
    engineBraking = rideSettings.engineBraking.format(locale),
    engineBrakingFraction = rideSettings.engineBraking.fraction(),
    regen = rideSettings.regen.format(locale),
    regenFraction = rideSettings.regen.fraction(),
)

private fun Diagnostics.toUiModel() = DiagnosticsUi(
    warnings = warnings.map { it.toUiModel() }.toImmutableList(),
    faultCodes = faultCodes.toImmutableList(),
)

private fun Warning.toUiModel() = WarningUi(
    code = code,
    message = message,
    severity = severity.toUiModel(),
)

private fun Instant.format(timestampFormatter: DateTimeFormatter): String =
    timestampFormatter.format(java.time.Instant.ofEpochSecond(epochSeconds))

private fun Duration.format(locale: Locale): String = toComponents { hours, minutes, seconds, _ ->
    when {
        hours > 0L -> String.format(locale, "%dh %02dm", hours, minutes)
        minutes > 0 -> String.format(locale, "%dm %02ds", minutes, seconds)
        else -> String.format(locale, "%ds", seconds)
    }
}

private fun Percent.format(locale: Locale): String = String.format(locale, "%d%%", value)

private fun Percent.fraction(): Float = (value / 100f).coerceIn(0f, 1f)

private fun Celsius.format(locale: Locale): String = String.format(locale, "%.1f °C", value)

private fun Kilometers.format(locale: Locale): String = String.format(locale, "%.1f km", value)

private fun Kilometers.formatWhole(locale: Locale): String = String.format(locale, "%.0f km", value)

private fun KilometersPerHour.format(locale: Locale): String =
    String.format(locale, "%.1f km/h", value)

private fun Horsepower.format(locale: Locale): String = String.format(locale, "%.1f hp", value)

private fun Horsepower.formatWhole(locale: Locale): String = String.format(locale, "%.0f hp", value)

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

private fun Severity.toUiModel(): SeverityUi = when (this) {
    Severity.INFO -> SeverityUi.INFO
    Severity.WARNING -> SeverityUi.WARNING
    Severity.CRITICAL -> SeverityUi.CRITICAL
    Severity.UNKNOWN -> SeverityUi.UNKNOWN
}
