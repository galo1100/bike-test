package galo.db.biketest.fixtures

import galo.db.biketest.data.dto.BatteryDto
import galo.db.biketest.data.dto.BikeDto
import galo.db.biketest.data.dto.BikeTelemetryDto
import galo.db.biketest.data.dto.DiagnosticsDto
import galo.db.biketest.data.dto.MotorDto
import galo.db.biketest.data.dto.RideSettingsDto
import galo.db.biketest.data.dto.SessionDto
import galo.db.biketest.data.dto.WarningDto

internal fun telemetryDto(
    bike: BikeDto = bikeDto(),
    timestamp: String = SNAPSHOT_TIMESTAMP,
    battery: BatteryDto = batteryDto(),
    motor: MotorDto = motorDto(),
    rideSettings: RideSettingsDto = rideSettingsDto(),
    session: SessionDto = sessionDto(),
    diagnostics: DiagnosticsDto = diagnosticsDto(),
) = BikeTelemetryDto(
    bike = bike,
    timestamp = timestamp,
    battery = battery,
    motor = motor,
    rideSettings = rideSettings,
    session = session,
    diagnostics = diagnostics,
)

internal fun bikeDto(
    model: String = "Stark VARG MX 1.2",
    variant: String = "Alpha",
    firmwareVersion: String = "3.4.1",
    imageUrl: String = SNAPSHOT_IMAGE_URL,
) = BikeDto(
    model = model,
    variant = variant,
    firmwareVersion = firmwareVersion,
    imageUrl = imageUrl,
)

internal fun batteryDto(
    stateOfChargePct: Int = 73,
    estimatedRangeKm: Int = 38,
    temperatureC: Double = 34.7,
    chargingState: String = "discharging",
) = BatteryDto(
    stateOfChargePct = stateOfChargePct,
    estimatedRangeKm = estimatedRangeKm,
    temperatureC = temperatureC,
    chargingState = chargingState,
)

internal fun motorDto(
    powerHp: Double = 52.4,
    temperatureC: Double = 61.2,
) = MotorDto(
    powerHp = powerHp,
    temperatureC = temperatureC,
)

internal fun rideSettingsDto(
    powerMap: String = "enduro",
    maxPowerHp: Int = 80,
    engineBrakingPct: Int = 45,
    regenPct: Int = 60,
) = RideSettingsDto(
    powerMap = powerMap,
    maxPowerHp = maxPowerHp,
    engineBrakingPct = engineBrakingPct,
    regenPct = regenPct,
)

internal fun sessionDto(
    durationS: Int = 3742,
    distanceKm: Double = 24.7,
    maxSpeedKmh: Double = 94.1,
) = SessionDto(
    durationS = durationS,
    distanceKm = distanceKm,
    maxSpeedKmh = maxSpeedKmh,
)

internal fun diagnosticsDto(
    faultCodes: List<String> = emptyList(),
    warnings: List<WarningDto> = listOf(warningDto()),
) = DiagnosticsDto(
    faultCodes = faultCodes,
    warnings = warnings,
)

internal fun warningDto(
    code: String = "W_MOT_TEMP_HIGH",
    message: String = "Motor temperature elevated",
    severity: String = "warning",
) = WarningDto(
    code = code,
    message = message,
    severity = severity,
)
