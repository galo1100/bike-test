package galo.db.biketest.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import galo.db.biketest.data.remote.mock.MockTelemetryConfig
import galo.db.biketest.data.remote.mock.TelemetrySnapshotAsset
import galo.db.biketest.data.remote.mock.mockTelemetryEngine
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
    fun provideMockTelemetryConfig(): MockTelemetryConfig = MockTelemetryConfig()

    @Provides
    @Singleton
    fun provideHttpClientEngine(
        config: MockTelemetryConfig,
        snapshotAsset: TelemetrySnapshotAsset,
    ): HttpClientEngine = mockTelemetryEngine(config, snapshotAsset::read)

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
