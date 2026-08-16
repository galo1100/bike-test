package galo.db.biketest.presentation.telemetry.preview

import galo.db.biketest.R
import galo.db.biketest.presentation.telemetry.entities.BatteryUi
import galo.db.biketest.presentation.telemetry.entities.BikeUi
import galo.db.biketest.presentation.telemetry.entities.DiagnosticsUi
import galo.db.biketest.presentation.telemetry.entities.PowerUi
import galo.db.biketest.presentation.telemetry.entities.SessionUi
import galo.db.biketest.presentation.telemetry.entities.SeverityUi
import galo.db.biketest.presentation.telemetry.entities.BikeTelemetryUiModel
import galo.db.biketest.presentation.telemetry.entities.WarningUi
import kotlinx.collections.immutable.persistentListOf

object BikeTelemetryPreviewData {

    val telemetry = BikeTelemetryUiModel(
        bike = BikeUi(
            title = "Stark VARG MX 1.2 Alpha",
            firmwareVersion = "3.4.1",
            imageUrl = "https://assets.starkfuture.com/frontend-assets/mx-product-images/" +
                "SMX1_side_stand_red_handbrake_enduro18_nosidestand.webp",
            updatedAt = "19 May 2025, 10:32",
        ),
        battery = BatteryUi(
            stateOfCharge = "73%",
            chargeFraction = 0.73f,
            range = "38 km",
            temperature = "34.7 °C",
            chargingStateRes = R.string.telemetry_charging_state_discharging,
        ),
        session = SessionUi(
            duration = "1h 02m",
            distance = "24.7 km",
            maxSpeed = "94.1 km/h",
        ),
        power = PowerUi(
            output = "52.4 hp",
            maxOutput = "80 hp",
            outputFraction = 0.655f,
            motorTemperature = "61.2 °C",
            powerMapRes = R.string.telemetry_power_map_enduro,
            engineBraking = "45%",
            engineBrakingFraction = 0.45f,
            regen = "60%",
            regenFraction = 0.60f,
        ),
        diagnostics = DiagnosticsUi(
            warnings = persistentListOf(
                WarningUi(
                    code = "W_MOT_TEMP_HIGH",
                    message = "Motor temperature elevated",
                    severity = SeverityUi.WARNING,
                ),
            ),
            faultCodes = persistentListOf(),
        ),
    )

    val telemetryWithoutDiagnostics = telemetry.copy(
        diagnostics = DiagnosticsUi(warnings = persistentListOf(), faultCodes = persistentListOf()),
    )
}
