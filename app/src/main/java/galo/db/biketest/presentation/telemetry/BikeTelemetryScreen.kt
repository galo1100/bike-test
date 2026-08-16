@file:OptIn(ExperimentalMaterial3Api::class)

package galo.db.biketest.presentation.telemetry

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import galo.db.biketest.R
import galo.db.biketest.presentation.telemetry.composables.BatteryCard
import galo.db.biketest.presentation.telemetry.composables.BikeHeaderCard
import galo.db.biketest.presentation.telemetry.composables.DiagnosticsCard
import galo.db.biketest.presentation.telemetry.composables.PowerCard
import galo.db.biketest.presentation.telemetry.composables.SessionCard
import galo.db.biketest.presentation.telemetry.entities.BikeTelemetryAction
import galo.db.biketest.presentation.telemetry.entities.BikeTelemetryState.Content
import galo.db.biketest.presentation.telemetry.entities.BikeTelemetryState.Error
import galo.db.biketest.presentation.telemetry.entities.BikeTelemetryState.Loading
import galo.db.biketest.presentation.telemetry.entities.BikeTelemetryUiModel
import galo.db.biketest.presentation.telemetry.preview.BikeTelemetryPreviewData
import galo.db.biketest.presentation.theme.BikeTestTheme

object BikeTelemetryScreenTestTags {
    const val CONTENT = "telemetry_content"
}

@Composable
fun BikeTelemetryScreen(
    modifier: Modifier = Modifier,
    viewModel: BikeTelemetryViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val listState = rememberLazyListState()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(title = { Text(text = stringResource(R.string.telemetry_title)) })
        },
    ) { innerPadding ->
        when (val currentState = state) {
            Loading -> BikeTelemetryLoading(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
            )

            is Error -> BikeTelemetryError(
                messageRes = currentState.messageRes,
                onRetry = { viewModel.handleAction(BikeTelemetryAction.Retry) },
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
            )

            is Content -> BikeTelemetryContent(
                telemetry = currentState.bikeTelemetry,
                listState = listState,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
            )
        }
    }
}

@Composable
internal fun BikeTelemetryContent(
    telemetry: BikeTelemetryUiModel,
    modifier: Modifier = Modifier,
    listState: LazyListState = rememberLazyListState(),
) {
    LazyColumn(
        state = listState,
        modifier = modifier.testTag(BikeTelemetryScreenTestTags.CONTENT),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        item { BikeHeaderCard(bike = telemetry.bike) }
        item { BatteryCard(battery = telemetry.battery) }
        item { SessionCard(session = telemetry.session) }
        item { PowerCard(power = telemetry.power) }
        item { DiagnosticsCard(diagnostics = telemetry.diagnostics) }
    }
}

@Composable
internal fun BikeTelemetryLoading(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        CircularProgressIndicator()
    }
}

@Composable
internal fun BikeTelemetryError(
    messageRes: Int,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_warning),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.error,
        )
        Text(
            text = stringResource(messageRes),
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
        )
        Button(onClick = onRetry) {
            Text(text = stringResource(R.string.telemetry_retry))
        }
    }
}

@PreviewLightDark
@Composable
private fun BikeTelemetryScreenBikeTelemetryContentPreview() {
    BikeTestTheme(dynamicColor = false) {
        BikeTelemetryContent(
            telemetry = BikeTelemetryPreviewData.telemetry,
        )
    }
}

@PreviewLightDark
@Composable
private fun BikeTelemetryScreenBikeTelemetryLoadingPreview() {
    BikeTestTheme(dynamicColor = false) {
        BikeTelemetryLoading(modifier = Modifier.fillMaxSize())
    }
}

@PreviewLightDark
@Composable
private fun BikeTelemetryScreenBikeTelemetryErrorPreview() {
    BikeTestTheme(dynamicColor = false) {
        BikeTelemetryError(
            messageRes = R.string.telemetry_error_unreachable,
            onRetry = {},
            modifier = Modifier.fillMaxSize(),
        )
    }
}

