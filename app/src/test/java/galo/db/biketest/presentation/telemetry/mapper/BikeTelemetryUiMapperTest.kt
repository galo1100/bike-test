package galo.db.biketest.presentation.telemetry.mapper

import galo.db.biketest.R
import galo.db.biketest.domain.model.BikeTelemetry
import galo.db.biketest.domain.model.PowerMap
import galo.db.biketest.domain.model.Severity
import galo.db.biketest.fixtures.bikeTelemetry
import galo.db.biketest.fixtures.bikeTelemetryUiModel
import galo.db.biketest.fixtures.diagnostics
import galo.db.biketest.fixtures.diagnosticsUi
import galo.db.biketest.fixtures.powerUi
import galo.db.biketest.fixtures.rideSettings
import galo.db.biketest.fixtures.session
import galo.db.biketest.fixtures.sessionUi
import galo.db.biketest.fixtures.warning
import galo.db.biketest.fixtures.warningUi
import galo.db.biketest.presentation.telemetry.entities.SeverityUi
import org.junit.Test
import java.time.ZoneId
import java.util.Locale
import kotlin.test.assertEquals
import kotlin.time.Duration.Companion.seconds

class BikeTelemetryUiMapperTest {

    private fun BikeTelemetry.toUiModel() = toUiModel(
        locale = Locale.US,
        zoneId = ZoneId.of("UTC"),
    )

    @Test
    fun `maps the contract snapshot to display-ready telemetry`() {
        // Given
        val telemetry = bikeTelemetry()

        // When
        val result = telemetry.toUiModel()

        // Then
        assertEquals(bikeTelemetryUiModel(), result)
    }

    @Test
    fun `drops the hours part from a session shorter than an hour`() {
        // Given
        val telemetry = bikeTelemetry(session = session(duration = 750.seconds))

        // When
        val result = telemetry.toUiModel()

        // Then
        assertEquals(sessionUi(duration = "12m 30s"), result.session)
    }

    @Test
    fun `reports no diagnostics when there are neither warnings nor fault codes`() {
        // Given
        val telemetry = bikeTelemetry(
            diagnostics = diagnostics(warnings = emptyList(), faultCodes = emptyList()),
        )

        // When
        val result = telemetry.toUiModel()

        // Then
        assertEquals(true, result.diagnostics.isEmpty)
    }

    @Test
    fun `reports diagnostics when only fault codes are present`() {
        // Given
        val telemetry = bikeTelemetry(
            diagnostics = diagnostics(warnings = emptyList(), faultCodes = listOf("F_BMS_02")),
        )

        // When
        val result = telemetry.toUiModel()

        // Then
        assertEquals(
            diagnosticsUi(warnings = emptyList(), faultCodes = listOf("F_BMS_02")),
            result.diagnostics,
        )
    }

    @Test
    fun `labels a power map this build does not recognise as unknown`() {
        // Given
        val telemetry = bikeTelemetry(rideSettings = rideSettings(powerMap = PowerMap.UNKNOWN))

        // When
        val result = telemetry.toUiModel()

        // Then
        assertEquals(powerUi(powerMapRes = R.string.telemetry_power_map_unknown), result.power)
    }

    @Test
    fun `carries warning severity through to the presentation model`() {
        // Given
        val telemetry = bikeTelemetry(
            diagnostics = diagnostics(warnings = listOf(warning(severity = Severity.CRITICAL))),
        )

        // When
        val result = telemetry.toUiModel()

        // Then
        assertEquals(
            diagnosticsUi(warnings = listOf(warningUi(severity = SeverityUi.CRITICAL))),
            result.diagnostics,
        )
    }
}
