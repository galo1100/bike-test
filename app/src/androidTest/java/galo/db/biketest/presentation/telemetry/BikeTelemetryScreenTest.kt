package galo.db.biketest.presentation.telemetry

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToNode
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import galo.db.biketest.R
import galo.db.biketest.presentation.telemetry.preview.BikeTelemetryPreviewData
import galo.db.biketest.presentation.theme.BikeTestTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.test.assertEquals

@RunWith(AndroidJUnit4::class)
class BikeTelemetryScreenTest {

    @get:Rule
    val composeRule = createComposeRule()

    private val context = InstrumentationRegistry.getInstrumentation().targetContext

    @Test
    fun showsTheSnapshot() {
        // Given
        composeRule.setContent {
            BikeTestTheme {
                BikeTelemetryContent(telemetry = BikeTelemetryPreviewData.telemetry)
            }
        }

        // When / Then
        composeRule.onNodeWithText("Stark VARG MX 1.2 Alpha").assertIsDisplayed()
        composeRule.onNodeWithText("73%").assertIsDisplayed()
        composeRule.onNodeWithText("1h 02m").assertIsDisplayed()
        scrollTo("Motor temperature elevated")
        composeRule.onNodeWithText("Motor temperature elevated").assertIsDisplayed()
    }

    @Test
    fun showsTheEmptyDiagnosticsStateWhenTheBikeHasNothingToReport() {
        // Given
        composeRule.setContent {
            BikeTestTheme {
                BikeTelemetryContent(
                    telemetry = BikeTelemetryPreviewData.telemetryWithoutDiagnostics,
                )
            }
        }

        // When / Then
        val emptyTitle = context.getString(R.string.telemetry_diagnostics_empty_title)
        scrollTo(emptyTitle)
        composeRule.onNodeWithText(emptyTitle).assertIsDisplayed()
    }

    @Test
    fun reportsARetryFromTheErrorState() {
        // Given
        var retries = 0
        composeRule.setContent {
            BikeTestTheme {
                BikeTelemetryError(
                    messageRes = R.string.telemetry_error_unreachable,
                    onRetry = { retries++ },
                )
            }
        }

        // When
        composeRule
            .onNodeWithText(context.getString(R.string.telemetry_retry))
            .performClick()

        // Then
        assertEquals(1, retries)
    }

    private fun scrollTo(text: String) {
        composeRule
            .onNodeWithTag(BikeTelemetryScreenTestTags.CONTENT)
            .performScrollToNode(hasText(text))
    }
}
