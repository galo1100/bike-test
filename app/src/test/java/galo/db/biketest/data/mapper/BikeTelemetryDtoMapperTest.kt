package galo.db.biketest.data.mapper

import galo.db.biketest.domain.model.ChargingState
import galo.db.biketest.domain.model.PowerMap
import galo.db.biketest.domain.model.Severity
import galo.db.biketest.fixtures.battery
import galo.db.biketest.fixtures.batteryDto
import galo.db.biketest.fixtures.bikeTelemetry
import galo.db.biketest.fixtures.diagnostics
import galo.db.biketest.fixtures.diagnosticsDto
import galo.db.biketest.fixtures.rideSettings
import galo.db.biketest.fixtures.rideSettingsDto
import galo.db.biketest.fixtures.telemetryDto
import galo.db.biketest.fixtures.warning
import galo.db.biketest.fixtures.warningDto
import kotlinx.serialization.SerializationException
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class BikeTelemetryDtoMapperTest {

    @Test
    fun `maps the contract payload to domain telemetry`() {
        // Given
        val dto = telemetryDto()

        // When
        val telemetry = dto.toDomain()

        // Then
        assertEquals(bikeTelemetry(), telemetry)
    }

    @Test
    fun `maps enum values regardless of casing`() {
        // Given
        val dto = telemetryDto(
            battery = batteryDto(chargingState = "CHARGING"),
            rideSettings = rideSettingsDto(powerMap = "Supermoto"),
            diagnostics = diagnosticsDto(warnings = listOf(warningDto(severity = "critical"))),
        )

        // When
        val telemetry = dto.toDomain()

        // Then
        assertEquals(
            bikeTelemetry(
                battery = battery(chargingState = ChargingState.CHARGING),
                rideSettings = rideSettings(powerMap = PowerMap.SUPERMOTO),
                diagnostics = diagnostics(warnings = listOf(warning(severity = Severity.CRITICAL))),
            ),
            telemetry,
        )
    }

    @Test
    fun `falls back to unknown for values this build does not model`() {
        // Given
        val dto = telemetryDto(
            battery = batteryDto(chargingState = "balancing"),
            rideSettings = rideSettingsDto(powerMap = "sand"),
            diagnostics = diagnosticsDto(warnings = listOf(warningDto(severity = "fatal"))),
        )

        // When
        val telemetry = dto.toDomain()

        // Then
        assertEquals(
            bikeTelemetry(
                battery = battery(chargingState = ChargingState.UNKNOWN),
                rideSettings = rideSettings(powerMap = PowerMap.UNKNOWN),
                diagnostics = diagnostics(warnings = listOf(warning(severity = Severity.UNKNOWN))),
            ),
            telemetry,
        )
    }

    @Test
    fun `maps a bike with nothing to report`() {
        // Given
        val dto = telemetryDto(diagnostics = diagnosticsDto(warnings = emptyList()))

        // When
        val telemetry = dto.toDomain()

        // Then
        assertEquals(bikeTelemetry(diagnostics = diagnostics(warnings = emptyList())), telemetry)
    }

    @Test
    fun `rejects a timestamp it cannot parse`() {
        // Given
        val dto = telemetryDto(timestamp = "yesterday")

        // When / Then
        assertFailsWith<SerializationException> { dto.toDomain() }
    }
}
