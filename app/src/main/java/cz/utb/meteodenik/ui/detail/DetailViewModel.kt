package cz.utb.meteodenik.ui.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import cz.utb.meteodenik.data.MeteoRepository
import cz.utb.meteodenik.model.Mesto
import cz.utb.meteodenik.model.MeteoZaznam
import cz.utb.meteodenik.navigace.Detail
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

data class DetailUiState(
    val mesto: Mesto? = null,
    val pocasi: MeteoZaznam? = null,
    val nacitaSe: Boolean = false
)

@HiltViewModel
class DetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    repository: MeteoRepository
) : ViewModel() {

    private val argumenty: Detail = savedStateHandle.toRoute()

    // Stejný vzor jako seznam: čteme z Roomu, síť sem nepatří.
    val uiState: StateFlow<DetailUiState> = repository.sledujMesta()
        .map { seznam ->
            val radek = seznam.firstOrNull { it.mesto.id == argumenty.mestoId }
            DetailUiState(mesto = radek?.mesto, pocasi = radek?.pocasi, nacitaSe = false)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = DetailUiState(nacitaSe = true)
        )
}