package galo.db.biketest.data.remote.mock

import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds


data class MockTelemetryConfig(
    val scenario: MockTelemetryScenario = MockTelemetryScenario.SUCCESS,
    val responseDelay: Duration = 600.milliseconds,
)

enum class MockTelemetryScenario {
    SUCCESS,
    HTTP_ERROR,
    MALFORMED,
}
