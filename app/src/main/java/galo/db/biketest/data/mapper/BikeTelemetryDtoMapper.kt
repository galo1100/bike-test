package galo.db.biketest.data.mapper

import galo.db.biketest.data.dto.BatteryDto
import galo.db.biketest.data.dto.BikeDto
import galo.db.biketest.data.dto.BikeTelemetryDto
import galo.db.biketest.data.dto.DiagnosticsDto
import galo.db.biketest.data.dto.MotorDto
import galo.db.biketest.data.dto.RideSettingsDto
import galo.db.biketest.data.dto.SessionDto
import galo.db.biketest.data.dto.WarningDto
import galo.db.biketest.domain.model.Battery
import galo.db.biketest.domain.model.Bike
import galo.db.biketest.domain.model.BikeTelemetry
import galo.db.biketest.domain.model.ChargingState
import galo.db.biketest.domain.model.Diagnostics
import galo.db.biketest.domain.model.Motor
import galo.db.biketest.domain.model.PowerMap
import galo.db.biketest.domain.model.RideSettings
import galo.db.biketest.domain.model.Session
import galo.db.biketest.domain.model.Severity
import galo.db.biketest.domain.model.Warning
import galo.db.biketest.domain.model.Celsius
import galo.db.biketest.domain.model.Horsepower
import galo.db.biketest.domain.model.Kilometers
import galo.db.biketest.domain.model.KilometersPerHour
import galo.db.biketest.domain.model.Percent
import kotlinx.serialization.SerializationException
import kotlin.time.Duration.Companion.seconds
import kotlin.time.Instant

internal fun BikeTelemetryDto.toDomain(): BikeTelemetry = BikeTelemetry(
    bike = bike.toDomain(),
    timestamp = timestamp.toInstant(),
    battery = battery.toDomain(),
    motor = motor.toDomain(),
    rideSettings = rideSettings.toDomain(),
    session = session.toDomain(),
    diagnostics = diagnostics.toDomain(),
)

private fun BikeDto.toDomain(): Bike = Bike(
    model = model,
    variant = variant,
    firmwareVersion = firmwareVersion,
    imageUrl = imageUrl,
)

private fun BatteryDto.toDomain(): Battery = Battery(
    stateOfCharge = Percent(stateOfChargePct),
    estimatedRange = Kilometers(estimatedRangeKm.toDouble()),
    temperature = Celsius(temperatureC),
    chargingState = ChargingState.from(chargingState),
)

private fun MotorDto.toDomain(): Motor = Motor(
    power = Horsepower(powerHp),
    temperature = Celsius(temperatureC),
)

private fun RideSettingsDto.toDomain(): RideSettings = RideSettings(
    powerMap = PowerMap.from(powerMap),
    maxPower = Horsepower(maxPowerHp.toDouble()),
    engineBraking = Percent(engineBrakingPct),
    regen = Percent(regenPct),
)

private fun SessionDto.toDomain(): Session = Session(
    duration = durationS.seconds,
    distance = Kilometers(distanceKm),
    maxSpeed = KilometersPerHour(maxSpeedKmh),
)

private fun DiagnosticsDto.toDomain(): Diagnostics = Diagnostics(
    faultCodes = faultCodes,
    warnings = warnings.map { it.toDomain() },
)

private fun WarningDto.toDomain(): Warning = Warning(
    code = code,
    message = message,
    severity = Severity.from(severity),
)

private fun String.toInstant(): Instant = try {
    Instant.parse(this)
} catch (invalid: IllegalArgumentException) {
    throw SerializationException("Invalid telemetry timestamp: $this", invalid)
}
