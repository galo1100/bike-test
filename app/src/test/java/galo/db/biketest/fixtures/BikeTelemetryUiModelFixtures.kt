package galo.db.biketest.fixtures

import galo.db.biketest.R
import galo.db.biketest.presentation.telemetry.entities.BatteryUi
import galo.db.biketest.presentation.telemetry.entities.BikeUi
import galo.db.biketest.presentation.telemetry.entities.DiagnosticsUi
import galo.db.biketest.presentation.telemetry.entities.PowerUi
import galo.db.biketest.presentation.telemetry.entities.SessionUi
import galo.db.biketest.presentation.telemetry.entities.SeverityUi
import galo.db.biketest.presentation.telemetry.entities.BikeTelemetryUiModel
import galo.db.biketest.presentation.telemetry.entities.WarningUi
import kotlinx.collections.immutable.toImmutableList

internal fun bikeTelemetryUiModel(
    bike: BikeUi = bikeUi(),
    battery: BatteryUi = batteryUi(),
    session: SessionUi = sessionUi(),
    power: PowerUi = powerUi(),
    diagnostics: DiagnosticsUi = diagnosticsUi(),
) = BikeTelemetryUiModel(
    bike = bike,
    battery = battery,
    session = session,
    power = power,
    diagnostics = diagnostics,
)

internal fun bikeUi(
    title: String = "Stark VARG MX 1.2 Alpha",
    firmwareVersion: String = "3.4.1",
    imageUrl: String = SNAPSHOT_IMAGE_URL,
    updatedAt: String = "19 May 2025, 10:32",
) = BikeUi(
    title = title,
    firmwareVersion = firmwareVersion,
    imageUrl = imageUrl,
    updatedAt = updatedAt,
)

internal fun batteryUi(
    stateOfCharge: String = "73%",
    chargeFraction: Float = 0.73f,
    range: String = "38 km",
    temperature: String = "34.7 °C",
    chargingStateRes: Int = R.string.telemetry_charging_state_discharging,
) = BatteryUi(
    stateOfCharge = stateOfCharge,
    chargeFraction = chargeFraction,
    range = range,
    temperature = temperature,
    chargingStateRes = chargingStateRes,
)

internal fun sessionUi(
    duration: String = "1h 02m",
    distance: String = "24.7 km",
    maxSpeed: String = "94.1 km/h",
) = SessionUi(
    duration = duration,
    distance = distance,
    maxSpeed = maxSpeed,
)

internal fun powerUi(
    output: String = "52.4 hp",
    maxOutput: String = "80 hp",
    outputFraction: Float = 0.655f,
    motorTemperature: String = "61.2 °C",
    powerMapRes: Int = R.string.telemetry_power_map_enduro,
    engineBraking: String = "45%",
    engineBrakingFraction: Float = 0.45f,
    regen: String = "60%",
    regenFraction: Float = 0.60f,
) = PowerUi(
    output = output,
    maxOutput = maxOutput,
    outputFraction = outputFraction,
    motorTemperature = motorTemperature,
    powerMapRes = powerMapRes,
    engineBraking = engineBraking,
    engineBrakingFraction = engineBrakingFraction,
    regen = regen,
    regenFraction = regenFraction,
)

internal fun diagnosticsUi(
    warnings: List<WarningUi> = listOf(warningUi()),
    faultCodes: List<String> = emptyList(),
) = DiagnosticsUi(
    warnings = warnings.toImmutableList(),
    faultCodes = faultCodes.toImmutableList(),
)

internal fun warningUi(
    code: String = "W_MOT_TEMP_HIGH",
    message: String = "Motor temperature elevated",
    severity: SeverityUi = SeverityUi.WARNING,
) = WarningUi(
    code = code,
    message = message,
    severity = severity,
)
