package galo.db.biketest.presentation.telemetry.entities

import androidx.annotation.StringRes

sealed interface BikeTelemetryState {

    data object Loading : BikeTelemetryState
    data class Error(@StringRes val messageRes: Int) : BikeTelemetryState

    data class Content(
        val bikeTelemetry: BikeTelemetryUiModel,
    ) : BikeTelemetryState
}
