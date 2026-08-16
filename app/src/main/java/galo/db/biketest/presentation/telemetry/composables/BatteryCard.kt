package galo.db.biketest.presentation.telemetry.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import galo.db.biketest.R
import galo.db.biketest.presentation.telemetry.entities.BatteryUi

@Composable
internal fun BatteryCard(
    battery: BatteryUi,
    modifier: Modifier = Modifier,
) {
    SectionCard(
        title = stringResource(R.string.telemetry_battery),
        modifier = modifier,
        trailing = { InfoBadge(text = stringResource(battery.chargingStateRes)) },
    ) {
        Text(
            text = battery.stateOfCharge,
            style = MaterialTheme.typography.displaySmall,
        )
        BikeTelemetryProgressBar(fraction = battery.chargeFraction)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            StatItem(
                label = stringResource(R.string.telemetry_battery_range),
                value = battery.range,
                modifier = Modifier.weight(1f),
            )
            StatItem(
                label = stringResource(R.string.telemetry_battery_temperature),
                value = battery.temperature,
                modifier = Modifier.weight(1f),
            )
        }
    }
}
