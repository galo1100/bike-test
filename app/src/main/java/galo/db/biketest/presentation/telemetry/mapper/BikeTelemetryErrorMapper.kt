package galo.db.biketest.presentation.telemetry.mapper

import androidx.annotation.StringRes
import galo.db.biketest.R
import galo.db.biketest.domain.error.BikeTelemetryError.InvalidSnapshot
import galo.db.biketest.domain.error.BikeTelemetryError.Unavailable
import galo.db.biketest.domain.error.BikeTelemetryError.Unknown
import galo.db.biketest.domain.error.BikeTelemetryError.Unreachable

@StringRes
internal fun Throwable.toMessageRes(): Int = when (this) {
    is Unreachable -> R.string.telemetry_error_unreachable
    is Unavailable -> R.string.telemetry_error_unavailable
    is InvalidSnapshot -> R.string.telemetry_error_invalid_snapshot
    is Unknown -> R.string.telemetry_error_unknown
    else -> R.string.telemetry_error_unknown
}
