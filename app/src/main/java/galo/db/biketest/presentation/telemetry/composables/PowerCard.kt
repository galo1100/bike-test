package galo.db.biketest.presentation.telemetry.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import galo.db.biketest.R
import galo.db.biketest.presentation.telemetry.entities.PowerUi

@Composable
internal fun PowerCard(
    power: PowerUi,
    modifier: Modifier = Modifier,
) {
    SectionCard(
        title = stringResource(R.string.telemetry_power),
        modifier = modifier,
        trailing = { InfoBadge(text = stringResource(power.powerMapRes)) },
    ) {
        Row(verticalAlignment = Alignment.Bottom) {
            Text(
                text = power.output,
                style = MaterialTheme.typography.displaySmall,
            )
            Text(
                text = stringResource(R.string.telemetry_power_of_max, power.maxOutput),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(start = 8.dp, bottom = 6.dp),
            )
        }
        BikeTelemetryProgressBar(fraction = power.outputFraction)
        StatItem(
            label = stringResource(R.string.telemetry_motor_temperature),
            value = power.motorTemperature,
        )

        HorizontalDivider()

        Text(
            text = stringResource(R.string.telemetry_ride_settings),
            style = MaterialTheme.typography.titleSmall,
        )
        LabelledProgress(
            label = stringResource(R.string.telemetry_engine_braking),
            value = power.engineBraking,
            fraction = power.engineBrakingFraction,
        )
        LabelledProgress(
            label = stringResource(R.string.telemetry_regen),
            value = power.regen,
            fraction = power.regenFraction,
        )
    }
}

@Composable
private fun LabelledProgress(
    label: String,
    value: String,
    fraction: Float,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.weight(1f),
            )
            Text(
                text = value,
                style = MaterialTheme.typography.labelLarge,
            )
        }
        BikeTelemetryProgressBar(fraction = fraction)
    }
}
