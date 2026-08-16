package galo.db.biketest.di

import galo.db.biketest.data.GetBikeTelemetryImpl
import galo.db.biketest.domain.GetBikeTelemetry
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Binds
    @Singleton
    abstract fun bindGetBikeTelemetry(
        implementation: GetBikeTelemetryImpl,
    ): GetBikeTelemetry
}
