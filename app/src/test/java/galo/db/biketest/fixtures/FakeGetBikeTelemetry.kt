package galo.db.biketest.fixtures

import galo.db.biketest.domain.GetBikeTelemetry
import galo.db.biketest.domain.model.BikeTelemetry

internal class FakeGetBikeTelemetry(
    var result: Result<BikeTelemetry> = Result.success(bikeTelemetry()),
) : GetBikeTelemetry {

    var callCount: Int = 0
        private set

    override suspend fun invoke(): Result<BikeTelemetry> {
        callCount++
        return result
    }
}
