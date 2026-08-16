package galo.db.biketest.data.remote.mock

import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds


data class MockBikeTelemetryConfig(
    val scenario: MockBikeTelemetryScenario = MockBikeTelemetryScenario.SUCCESS,
    val responseDelay: Duration = 600.milliseconds,
)

enum class MockBikeTelemetryScenario {
    SUCCESS,
    HTTP_ERROR,
    MALFORMED,
}
