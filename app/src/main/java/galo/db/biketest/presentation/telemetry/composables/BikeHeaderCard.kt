package galo.db.biketest.presentation.telemetry.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImagePainter
import coil3.compose.rememberAsyncImagePainter
import galo.db.biketest.R
import galo.db.biketest.presentation.telemetry.entities.BikeUi

@Composable
internal fun BikeHeaderCard(
    bike: BikeUi,
    modifier: Modifier = Modifier,
) {
    Card(modifier = modifier.fillMaxWidth()) {
        Column {
            val painter = rememberAsyncImagePainter(
                model = bike.imageUrl,
                contentScale = ContentScale.Fit,
            )
            val state by painter.state.collectAsState()

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(MaterialTheme.colorScheme.surfaceVariant),
            ) {
                Image(
                    painter = painter,
                    contentDescription = stringResource(R.string.telemetry_bike_image),
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.fillMaxSize(),
                )
                if (state is AsyncImagePainter.State.Loading) {
                    ShimmerBox(modifier = Modifier.matchParentSize())
                }
            }
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Text(
                    text = bike.title,
                    style = MaterialTheme.typography.headlineSmall,
                )
                InfoBadge(text = stringResource(R.string.telemetry_firmware, bike.firmwareVersion))
                Text(
                    text = stringResource(R.string.telemetry_updated_at, bike.updatedAt),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}
