package galo.db.biketest.fixtures

import io.ktor.client.engine.mock.MockRequestHandleScope
import io.ktor.client.engine.mock.respond
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf

object MockResponses {

    internal fun MockRequestHandleScope.respondJson(body: String) = respond(
        content = body,
        status = HttpStatusCode.OK,
        headers = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString()),
    )

    internal fun readPayload(fileName: String): String {
        val path = "/telemetry/$fileName"
        val stream = MockResponses::class.java.getResourceAsStream(path)
            ?: error("Missing test payload: $path")
        return stream.bufferedReader().use { it.readText() }
    }
}
