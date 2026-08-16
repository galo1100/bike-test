package galo.db.biketest.data

import galo.db.biketest.data.mapper.toBikeTelemetryError
import kotlin.coroutines.cancellation.CancellationException

internal suspend fun <T> safeCall(block: suspend () -> T): Result<T> = try {
    Result.success(block())
} catch (cancellation: CancellationException) {
    throw cancellation
} catch (throwable: Throwable) {
    Result.failure(throwable.toBikeTelemetryError())
}
