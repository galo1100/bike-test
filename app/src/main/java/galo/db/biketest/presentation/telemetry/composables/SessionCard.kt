package galo.db.biketest.presentation.telemetry.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import galo.db.biketest.R
import galo.db.biketest.presentation.telemetry.entities.SessionUi

@Composable
internal fun SessionCard(
    session: SessionUi,
    modifier: Modifier = Modifier,
) {
    SectionCard(
        title = stringResource(R.string.telemetry_session),
        modifier = modifier,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            StatItem(
                label = stringResource(R.string.telemetry_session_duration),
                value = session.duration,
                modifier = Modifier.weight(1f),
            )
            StatItem(
                label = stringResource(R.string.telemetry_session_distance),
                value = session.distance,
                modifier = Modifier.weight(1f),
            )
            StatItem(
                label = stringResource(R.string.telemetry_session_max_speed),
                value = session.maxSpeed,
                modifier = Modifier.weight(1f),
            )
        }
    }
}
