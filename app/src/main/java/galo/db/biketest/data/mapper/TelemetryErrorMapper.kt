package galo.db.biketest.data.mapper

import galo.db.biketest.domain.error.TelemetryError
import galo.db.biketest.domain.error.TelemetryError.InvalidSnapshot
import galo.db.biketest.domain.error.TelemetryError.Unavailable
import galo.db.biketest.domain.error.TelemetryError.Unknown
import galo.db.biketest.domain.error.TelemetryError.Unreachable
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.plugins.ResponseException
import io.ktor.serialization.JsonConvertException
import kotlinx.serialization.SerializationException
import java.io.IOException

internal fun Throwable.toTelemetryError(): TelemetryError = when (this) {
    is ResponseException -> Unavailable(response.status.value, this)
    is HttpRequestTimeoutException, is IOException -> Unreachable(this)
    is JsonConvertException, is SerializationException -> InvalidSnapshot(this)
    else -> Unknown(this)
}
