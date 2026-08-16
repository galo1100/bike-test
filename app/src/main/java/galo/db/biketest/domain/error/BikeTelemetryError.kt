package galo.db.biketest.domain.error

sealed class BikeTelemetryError(message: String, cause: Throwable? = null) : Exception(message, cause) {
    class Unreachable(cause: Throwable) : BikeTelemetryError("Could not reach the bike", cause)
    class Unavailable(
        val statusCode: Int? = null,
        cause: Throwable? = null,
    ) : BikeTelemetryError("Bike could not provide a snapshot", cause)
    class InvalidSnapshot(cause: Throwable) : BikeTelemetryError("Telemetry payload could not be read", cause)
    class Unknown(cause: Throwable) : BikeTelemetryError("Unexpected telemetry failure", cause)
}
