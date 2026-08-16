package galo.db.biketest.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import java.time.ZoneId
import java.util.Locale
import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class DeviceLocale

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class DeviceZone

@Module
@InstallIn(SingletonComponent::class)
object PresentationModule {

    @Provides
    @DeviceLocale
    fun provideDeviceLocale(): Locale = Locale.getDefault()

    @Provides
    @DeviceZone
    fun provideDeviceZone(): ZoneId = ZoneId.systemDefault()
}
