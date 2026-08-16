package galo.db.biketest.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import galo.db.biketest.data.remote.mock.MockBikeTelemetryConfig
import galo.db.biketest.data.remote.mock.BikeTelemetrySnapshotAsset
import galo.db.biketest.data.remote.mock.mockBikeTelemetryEngine
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideJson(): Json = Json {
        ignoreUnknownKeys = true
    }

    @Provides
    @Singleton
    fun provideMockBikeTelemetryConfig(): MockBikeTelemetryConfig = MockBikeTelemetryConfig()

    @Provides
    @Singleton
    fun provideHttpClientEngine(
        config: MockBikeTelemetryConfig,
        snapshotAsset: BikeTelemetrySnapshotAsset,
    ): HttpClientEngine = mockBikeTelemetryEngine(config, snapshotAsset::read)

    @Provides
    @Singleton
    fun provideHttpClient(
        engine: HttpClientEngine,
        json: Json
    ): HttpClient = HttpClient(engine) {
        expectSuccess = true
        install(ContentNegotiation) {
            json(json)
        }
    }
}
