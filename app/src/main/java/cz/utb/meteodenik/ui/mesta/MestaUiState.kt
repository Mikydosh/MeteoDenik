package cz.utb.meteodenik.ui.mesta

import cz.utb.meteodenik.data.DuvodSelhani
import cz.utb.meteodenik.model.MestoSPocasim

data class MestaUiState(
    val mesta: List<MestoSPocasim> = emptyList(),
    val nacitaSe: Boolean = false,
    val chyba: DuvodSelhani? = null
)