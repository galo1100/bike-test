package galo.db.biketest.data.remote.mock

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class BikeTelemetrySnapshotAsset @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    fun read(): String = context.assets.open(FILE_NAME).bufferedReader().use { it.readText() }

    companion object {
        const val FILE_NAME = "telemetry_snapshot.json"
    }
}
