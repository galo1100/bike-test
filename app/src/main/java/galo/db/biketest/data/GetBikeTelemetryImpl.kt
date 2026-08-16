package galo.db.biketest.data

import galo.db.biketest.data.dto.BikeTelemetryDto
import galo.db.biketest.data.mapper.toDomain
import galo.db.biketest.data.remote.TelemetryApi
import galo.db.biketest.di.DispatcherIo
import galo.db.biketest.domain.GetBikeTelemetry
import galo.db.biketest.domain.model.BikeTelemetry
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetBikeTelemetryImpl @Inject constructor(
    private val client: HttpClient,
    @DispatcherIo private val dispatcher: CoroutineDispatcher,
) : GetBikeTelemetry {

    override suspend fun invoke(): Result<BikeTelemetry> = withContext(dispatcher) {
        safeCall { client.get(TelemetryApi.TELEMETRY_URL).body<BikeTelemetryDto>().toDomain() }
    }
}
