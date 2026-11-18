package cz.utb.meteodenik.model

data class MestoSPocasim(
    val mesto: Mesto,
    val pocasi: MeteoZaznam? = null      // null = pro město ještě nemáme měření
)

