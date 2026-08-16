package galo.db.biketest.presentation.telemetry.entities

sealed interface BikeTelemetryAction {
    data object Retry : BikeTelemetryAction
}
