package cz.utb.meteodenik.ui.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import cz.utb.meteodenik.data.MestaZdroj
import cz.utb.meteodenik.data.MeteoRepository
import cz.utb.meteodenik.data.Repozitare
import cz.utb.meteodenik.data.VysledekSite
import cz.utb.meteodenik.model.Mesto
import cz.utb.meteodenik.model.MeteoZaznam
import cz.utb.meteodenik.navigace.Detail
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class DetailUiState(
    val mesto: Mesto? = null,
    val pocasi: MeteoZaznam? = null,
    val nacitaSe: Boolean = false
)

class DetailViewModel(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val argumenty: Detail = savedStateHandle.toRoute()
    private val repository: MeteoRepository = Repozitare.meteo

    private val _uiState = MutableStateFlow(
        DetailUiState(mesto = MestaZdroj.podleId(argumenty.mestoId))
    )
    val uiState: StateFlow<DetailUiState> = _uiState.asStateFlow()

    init {
        nacti()
    }

    private fun nacti() {
        val mesto = _uiState.value.mesto ?: return
        viewModelScope.launch {
            _uiState.update { it.copy(nacitaSe = true) }
            try {
                when (val vysledek = repository.aktualniPocasi(mesto)) {
                    is VysledekSite.Uspech -> _uiState.update { it.copy(pocasi = vysledek.data) }
                    is VysledekSite.Selhani -> Unit   // zobrazení chyby v detailu není v zadání
                }
            } finally {
                _uiState.update { it.copy(nacitaSe = false) }
            }
        }
    }
}