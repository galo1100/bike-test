package galo.db.biketest.presentation.telemetry

import galo.db.biketest.R
import galo.db.biketest.domain.error.BikeTelemetryError
import galo.db.biketest.fixtures.DefaultLocaleRule
import galo.db.biketest.fixtures.FakeGetBikeTelemetry
import galo.db.biketest.fixtures.MainDispatcherRule
import galo.db.biketest.fixtures.bikeTelemetry
import galo.db.biketest.fixtures.bikeTelemetryUiModel
import galo.db.biketest.presentation.telemetry.entities.BikeTelemetryAction
import galo.db.biketest.presentation.telemetry.entities.BikeTelemetryState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import java.io.IOException
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class BikeTelemetryViewModelTest {

    @get:Rule
    internal val mainDispatcherRule = MainDispatcherRule()

    @get:Rule
    internal val defaultLocaleRule = DefaultLocaleRule()

    private val getBikeTelemetry = FakeGetBikeTelemetry()

    private fun viewModel() = BikeTelemetryViewModel(getBikeTelemetry = getBikeTelemetry)

    @Test
    fun `is loading while the first snapshot is in flight`() = runTest(mainDispatcherRule.testDispatcher) {
        // Given
        val viewModel = viewModel()

        // When
        val state = viewModel.state.value

        // Then
        assertEquals(BikeTelemetryState.Loading, state)
    }

    @Test
    fun `shows the snapshot once it arrives`() = runTest(mainDispatcherRule.testDispatcher) {
        // Given
        val viewModel = viewModel()

        // When
        advanceUntilIdle()

        // Then
        assertEquals(BikeTelemetryState.Content(bikeTelemetryUiModel()), viewModel.state.value)
    }

    @Test
    fun `tells the rider the bike is out of reach`() = runTest(mainDispatcherRule.testDispatcher) {
        // Given
        getBikeTelemetry.result = Result.failure(BikeTelemetryError.Unreachable(IOException()))
        val viewModel = viewModel()

        // When
        advanceUntilIdle()

        // Then
        assertEquals(
            BikeTelemetryState.Error(R.string.telemetry_error_unreachable),
            viewModel.state.value,
        )
    }

    @Test
    fun `tells the rider the bike cannot answer`() = runTest(mainDispatcherRule.testDispatcher) {
        // Given
        getBikeTelemetry.result = Result.failure(BikeTelemetryError.Unavailable(statusCode = 503))
        val viewModel = viewModel()

        // When
        advanceUntilIdle()

        // Then
        assertEquals(
            BikeTelemetryState.Error(R.string.telemetry_error_unavailable),
            viewModel.state.value,
        )
    }

    @Test
    fun `tells the rider the app cannot read the payload`() = runTest(mainDispatcherRule.testDispatcher) {
        // Given
        getBikeTelemetry.result = Result.failure(BikeTelemetryError.InvalidSnapshot(IOException()))
        val viewModel = viewModel()

        // When
        advanceUntilIdle()

        // Then
        assertEquals(
            BikeTelemetryState.Error(R.string.telemetry_error_invalid_snapshot),
            viewModel.state.value,
        )
    }

    @Test
    fun `falls back to a generic message for a failure it does not recognise`() =
        runTest(mainDispatcherRule.testDispatcher) {
            // Given
            getBikeTelemetry.result = Result.failure(IllegalStateException())
            val viewModel = viewModel()

            // When
            advanceUntilIdle()

            // Then
            assertEquals(
                BikeTelemetryState.Error(R.string.telemetry_error_unknown),
                viewModel.state.value,
            )
        }

    @Test
    fun `goes back to loading while a retry is in flight`() = runTest(mainDispatcherRule.testDispatcher) {
        // Given
        getBikeTelemetry.result = Result.failure(BikeTelemetryError.Unreachable(IOException()))
        val viewModel = viewModel()
        advanceUntilIdle()

        // When
        viewModel.handleAction(BikeTelemetryAction.Retry)

        // Then
        assertEquals(BikeTelemetryState.Loading, viewModel.state.value)
    }

    @Test
    fun `replaces the error with the snapshot when a retry succeeds`() =
        runTest(mainDispatcherRule.testDispatcher) {
            // Given
            getBikeTelemetry.result = Result.failure(BikeTelemetryError.Unreachable(IOException()))
            val viewModel = viewModel()
            advanceUntilIdle()
            getBikeTelemetry.result = Result.success(bikeTelemetry())

            // When
            viewModel.handleAction(BikeTelemetryAction.Retry)
            advanceUntilIdle()

            // Then
            assertEquals(BikeTelemetryState.Content(bikeTelemetryUiModel()), viewModel.state.value)
        }

    @Test
    fun `ignores a second retry while the first is still running`() =
        runTest(mainDispatcherRule.testDispatcher) {
            // Given
            getBikeTelemetry.result = Result.failure(BikeTelemetryError.Unreachable(IOException()))
            val viewModel = viewModel()
            advanceUntilIdle()

            // When
            viewModel.handleAction(BikeTelemetryAction.Retry)
            viewModel.handleAction(BikeTelemetryAction.Retry)
            advanceUntilIdle()

            // Then
            assertEquals(2, getBikeTelemetry.callCount)
        }
}
