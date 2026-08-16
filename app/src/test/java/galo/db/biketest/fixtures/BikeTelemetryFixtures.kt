package galo.db.biketest.fixtures

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
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds
import kotlin.time.Instant

internal const val SNAPSHOT_TIMESTAMP = "2025-05-19T10:32:45Z"

internal const val SNAPSHOT_IMAGE_URL =
    "https://assets.starkfuture.com/frontend-assets/mx-product-images/" +
        "SMX1_side_stand_red_handbrake_enduro18_nosidestand.webp"

internal fun bikeTelemetry(
    bike: Bike = bike(),
    timestamp: Instant = Instant.parse(SNAPSHOT_TIMESTAMP),
    battery: Battery = battery(),
    motor: Motor = motor(),
    rideSettings: RideSettings = rideSettings(),
    session: Session = session(),
    diagnostics: Diagnostics = diagnostics(),
) = BikeTelemetry(
    bike = bike,
    timestamp = timestamp,
    battery = battery,
    motor = motor,
    rideSettings = rideSettings,
    session = session,
    diagnostics = diagnostics,
)

internal fun bike(
    model: String = "Stark VARG MX 1.2",
    variant: String = "Alpha",
    firmwareVersion: String = "3.4.1",
    imageUrl: String = SNAPSHOT_IMAGE_URL,
) = Bike(
    model = model,
    variant = variant,
    firmwareVersion = firmwareVersion,
    imageUrl = imageUrl,
)

internal fun battery(
    stateOfCharge: Percent = Percent(73),
    estimatedRange: Kilometers = Kilometers(38.0),
    temperature: Celsius = Celsius(34.7),
    chargingState: ChargingState = ChargingState.DISCHARGING,
) = Battery(
    stateOfCharge = stateOfCharge,
    estimatedRange = estimatedRange,
    temperature = temperature,
    chargingState = chargingState,
)

internal fun motor(
    power: Horsepower = Horsepower(52.4),
    temperature: Celsius = Celsius(61.2),
) = Motor(
    power = power,
    temperature = temperature,
)

internal fun rideSettings(
    powerMap: PowerMap = PowerMap.ENDURO,
    maxPower: Horsepower = Horsepower(80.0),
    engineBraking: Percent = Percent(45),
    regen: Percent = Percent(60),
) = RideSettings(
    powerMap = powerMap,
    maxPower = maxPower,
    engineBraking = engineBraking,
    regen = regen,
)

internal fun session(
    duration: Duration = 3742.seconds,
    distance: Kilometers = Kilometers(24.7),
    maxSpeed: KilometersPerHour = KilometersPerHour(94.1),
) = Session(
    duration = duration,
    distance = distance,
    maxSpeed = maxSpeed,
)

internal fun diagnostics(
    faultCodes: List<String> = emptyList(),
    warnings: List<Warning> = listOf(warning()),
) = Diagnostics(
    faultCodes = faultCodes,
    warnings = warnings,
)

internal fun warning(
    code: String = "W_MOT_TEMP_HIGH",
    message: String = "Motor temperature elevated",
    severity: Severity = Severity.WARNING,
) = Warning(
    code = code,
    message = message,
    severity = severity,
)
