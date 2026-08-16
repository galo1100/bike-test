package galo.db.biketest.domain.error

sealed class TelemetryError(message: String, cause: Throwable? = null) : Exception(message, cause) {

    // Never got an answer: no connectivity, timeout, bike out of range.
    class Unreachable(cause: Throwable) : TelemetryError("Could not reach the bike", cause)

    // The bike answered, but could not provide a snapshot. The status is for logs, not for the UI.
    class Unavailable(
        val statusCode: Int? = null,
        cause: Throwable? = null,
    ) : TelemetryError("Bike could not provide a snapshot", cause)

    // It answered with something this build cannot read: the telemetry contract drifted.
    class InvalidSnapshot(cause: Throwable) : TelemetryError("Telemetry payload could not be read", cause)

    class Unknown(cause: Throwable) : TelemetryError("Unexpected telemetry failure", cause)
}
