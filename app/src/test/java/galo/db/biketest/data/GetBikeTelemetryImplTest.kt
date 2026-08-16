package galo.db.biketest.data

import galo.db.biketest.di.NetworkModule
import galo.db.biketest.domain.error.BikeTelemetryError
import galo.db.biketest.fixtures.PayloadReader.readPayload
import galo.db.biketest.fixtures.PayloadReader.respondJson
import galo.db.biketest.fixtures.bikeTelemetry
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.MockRequestHandler
import io.ktor.client.engine.mock.respondError
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Test
import java.io.IOException
import kotlin.coroutines.cancellation.CancellationException
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertIs

class GetBikeTelemetryImplTest {

    private var respondWith: MockRequestHandler = { respondJson(readPayload("valid_snapshot.json")) }

    private val engine = MockEngine { request -> respondWith(request) }

    private val testDispatcher = StandardTestDispatcher()

    private val getBikeTelemetry = GetBikeTelemetryImpl(
        client = NetworkModule.provideHttpClient(
            engine = engine,
            json = NetworkModule.provideJson()
        ),
        dispatcher = testDispatcher,
    )

    @Test
    fun `returns the snapshot as domain telemetry`() = runTest(testDispatcher) {
        // Given
        respondWith = { respondJson(readPayload("valid_snapshot.json")) }

        // When
        val result = getBikeTelemetry()

        // Then
        assertEquals(bikeTelemetry(), result.getOrNull())
    }

    @Test
    fun `reports an error status as the bike being unavailable`() = runTest(testDispatcher) {
        // Given
        respondWith = { respondError(HttpStatusCode.ServiceUnavailable) }

        // When
        val result = getBikeTelemetry()

        // Then
        assertIs<BikeTelemetryError.Unavailable>(result.exceptionOrNull())
    }

    @Test
    fun `reports a payload that breaks the contract as an invalid snapshot`() = runTest(testDispatcher) {
        // Given
        respondWith = { respondJson(readPayload("snapshot_missing_state_of_charge.json")) }

        // When
        val result = getBikeTelemetry()

        // Then
        assertIs<BikeTelemetryError.InvalidSnapshot>(result.exceptionOrNull())
    }

    @Test
    fun `reports a timestamp the domain cannot represent as an invalid snapshot`() = runTest(testDispatcher) {
        // Given
        respondWith = { respondJson(readPayload("snapshot_invalid_timestamp.json")) }

        // When
        val result = getBikeTelemetry()

        // Then
        assertIs<BikeTelemetryError.InvalidSnapshot>(result.exceptionOrNull())
    }

    @Test
    fun `reports a connectivity failure as the bike being unreachable`() = runTest(testDispatcher) {
        // Given
        respondWith = { throw IOException("bike out of range") }

        // When
        val result = getBikeTelemetry()

        // Then
        assertIs<BikeTelemetryError.Unreachable>(result.exceptionOrNull())
    }

    @Test
    fun `reports anything else as unknown`() = runTest(testDispatcher) {
        // Given
        respondWith = { throw IllegalStateException() }

        // When
        val result = getBikeTelemetry()

        // Then
        assertIs<BikeTelemetryError.Unknown>(result.exceptionOrNull())
    }

    @Test
    fun `lets cancellation through instead of reporting it as a failure`() = runTest(testDispatcher) {
        // Given
        respondWith = { throw CancellationException("caller went away") }

        // When / Then
        assertFailsWith<CancellationException> { getBikeTelemetry() }
    }
}
