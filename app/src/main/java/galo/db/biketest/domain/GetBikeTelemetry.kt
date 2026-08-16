package galo.db.biketest.domain

import galo.db.biketest.domain.model.BikeTelemetry

interface GetBikeTelemetry {
    suspend operator fun invoke(): Result<BikeTelemetry>
}
