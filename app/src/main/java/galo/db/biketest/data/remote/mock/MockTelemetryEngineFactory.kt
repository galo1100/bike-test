package galo.db.biketest.data.remote.mock

import galo.db.biketest.data.remote.TelemetryApi
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.MockRequestHandleScope
import io.ktor.client.engine.mock.respond
import io.ktor.client.engine.mock.respondError
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.Url
import io.ktor.http.headersOf
import kotlinx.coroutines.delay

private const val MALFORMED_BODY = """{"bike":{"model":"Stark VARG MX 1.2","""

private val telemetryPath = Url(TelemetryApi.TELEMETRY_URL).encodedPath

fun mockTelemetryEngine(
    config: MockTelemetryConfig,
    snapshot: () -> String,
): MockEngine = MockEngine { request ->
    delay(config.responseDelay)
    when {
        request.url.encodedPath != telemetryPath -> respondError(HttpStatusCode.NotFound)
        config.scenario == MockTelemetryScenario.HTTP_ERROR ->
            respondError(HttpStatusCode.ServiceUnavailable)

        config.scenario == MockTelemetryScenario.MALFORMED -> respondJson(MALFORMED_BODY)
        else -> respondJson(snapshot())
    }
}

private fun MockRequestHandleScope.respondJson(body: String) = respond(
    content = body,
    status = HttpStatusCode.OK,
    headers = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString()),
)
