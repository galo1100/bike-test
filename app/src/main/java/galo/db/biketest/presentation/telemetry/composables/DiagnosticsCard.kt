package galo.db.biketest.presentation.telemetry.composables

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import galo.db.biketest.R
import galo.db.biketest.presentation.telemetry.entities.DiagnosticsUi
import galo.db.biketest.presentation.telemetry.entities.SeverityUi
import galo.db.biketest.presentation.telemetry.entities.SeverityUi.CRITICAL
import galo.db.biketest.presentation.telemetry.entities.SeverityUi.INFO
import galo.db.biketest.presentation.telemetry.entities.SeverityUi.UNKNOWN
import galo.db.biketest.presentation.telemetry.entities.SeverityUi.WARNING
import galo.db.biketest.presentation.telemetry.entities.WarningUi

@Composable
internal fun DiagnosticsCard(
    diagnostics: DiagnosticsUi,
    modifier: Modifier = Modifier,
) {
    SectionCard(
        title = stringResource(R.string.telemetry_diagnostics),
        modifier = modifier,
    ) {
        if (diagnostics.isEmpty) {
            NoDiagnostics()
        } else {
            diagnostics.warnings.forEach { warning ->
                WarningRow(warning = warning)
            }
            if (diagnostics.faultCodes.isNotEmpty()) {
                Text(
                    text = stringResource(R.string.telemetry_fault_codes),
                    style = MaterialTheme.typography.titleSmall,
                )
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    diagnostics.faultCodes.forEach { code ->
                        InfoBadge(
                            text = code,
                            containerColor = MaterialTheme.colorScheme.errorContainer,
                            contentColor = MaterialTheme.colorScheme.onErrorContainer,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun NoDiagnostics(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_check_circle),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
        )
        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(
                text = stringResource(R.string.telemetry_diagnostics_empty_title),
                style = MaterialTheme.typography.bodyLarge,
            )
            Text(
                text = stringResource(R.string.telemetry_diagnostics_empty_message),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Composable
private fun WarningRow(
    warning: WarningUi,
    modifier: Modifier = Modifier,
) {
    val appearance = warning.severity.appearance()
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        color = appearance.containerColor,
        contentColor = appearance.contentColor,
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Icon(
                painter = painterResource(appearance.iconRes),
                contentDescription = stringResource(appearance.labelRes),
            )
            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(
                    text = warning.message,
                    style = MaterialTheme.typography.bodyMedium,
                )
                Text(
                    text = warning.code,
                    style = MaterialTheme.typography.labelSmall,
                )
            }
        }
    }
}

private data class SeverityAppearance(
    @DrawableRes val iconRes: Int,
    @StringRes val labelRes: Int,
    val containerColor: Color,
    val contentColor: Color,
)

@Composable
private fun SeverityUi.appearance(): SeverityAppearance = when (this) {
    CRITICAL -> SeverityAppearance(
        iconRes = R.drawable.ic_warning,
        labelRes = R.string.telemetry_severity_critical,
        containerColor = MaterialTheme.colorScheme.error,
        contentColor = MaterialTheme.colorScheme.onError,
    )

    WARNING -> SeverityAppearance(
        iconRes = R.drawable.ic_warning,
        labelRes = R.string.telemetry_severity_warning,
        containerColor = MaterialTheme.colorScheme.errorContainer,
        contentColor = MaterialTheme.colorScheme.onErrorContainer,
    )

    INFO -> SeverityAppearance(
        iconRes = R.drawable.ic_info,
        labelRes = R.string.telemetry_severity_info,
        containerColor = MaterialTheme.colorScheme.secondaryContainer,
        contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
    )

    UNKNOWN -> SeverityAppearance(
        iconRes = R.drawable.ic_info,
        labelRes = R.string.telemetry_severity_unknown,
        containerColor = MaterialTheme.colorScheme.surfaceVariant,
        contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
    )
}
