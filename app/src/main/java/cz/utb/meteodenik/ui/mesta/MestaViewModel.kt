package cz.utb.meteodenik.ui.mesta

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.utb.meteodenik.data.DuvodSelhani
import cz.utb.meteodenik.data.MeteoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MestaViewModel @Inject constructor(
    private val repository: MeteoRepository
) : ViewModel() {

    /** Stav, který nepatří do databáze: probíhá obnova? jaká je chyba? */
    private data class StavAkce(
        val nacitaSe: Boolean = false,
        val chyba: DuvodSelhani? = null
    )

    private val stavAkce = MutableStateFlow(StavAkce())

    val uiState: StateFlow<MestaUiState> = combine(
        repository.sledujMesta(),     // z Roomu
        stavAkce                      // z paměti
    ) { mesta, akce ->
        MestaUiState(mesta = mesta, nacitaSe = akce.nacitaSe, chyba = akce.chyba)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = MestaUiState(nacitaSe = true)
    )

    init {
        obnov()
    }

    fun obnov() {
        viewModelScope.launch {
            stavAkce.update { it.copy(nacitaSe = true, chyba = null) }
            val chyba = repository.obnovPocasi()      // vrací jen důvod selhání, ne data
            stavAkce.update { it.copy(nacitaSe = false, chyba = chyba) }
        }
    }
}