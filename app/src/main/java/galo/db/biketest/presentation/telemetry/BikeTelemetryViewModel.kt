package galo.db.biketest.presentation.telemetry

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import galo.db.biketest.domain.GetBikeTelemetry
import galo.db.biketest.presentation.telemetry.entities.BikeTelemetryState
import galo.db.biketest.presentation.telemetry.entities.BikeTelemetryAction
import galo.db.biketest.presentation.telemetry.entities.BikeTelemetryAction.Retry
import galo.db.biketest.presentation.telemetry.entities.BikeTelemetryState.Content
import galo.db.biketest.presentation.telemetry.entities.BikeTelemetryState.Error
import galo.db.biketest.presentation.telemetry.mapper.toMessageRes
import galo.db.biketest.presentation.telemetry.mapper.toUiModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BikeTelemetryViewModel @Inject constructor(
    private val getBikeTelemetry: GetBikeTelemetry,
) : ViewModel() {

    private val _state = MutableStateFlow<BikeTelemetryState>(BikeTelemetryState.Loading)
    val state: StateFlow<BikeTelemetryState> = _state.asStateFlow()

    private var loadJob: Job? = null

    init {
        load()
    }

    fun handleAction(action: BikeTelemetryAction) {
        when (action) {
            Retry -> load()
        }
    }

    private fun load() {
        if (loadJob?.isActive == true) return
        _state.value = BikeTelemetryState.Loading
        loadJob = viewModelScope.launch {
            getBikeTelemetry()
                .onSuccess { telemetry ->
                    _state.value = Content(telemetry.toUiModel())
                }
                .onFailure { failure ->
                    _state.value = Error(failure.toMessageRes())
                }
        }
    }
}
