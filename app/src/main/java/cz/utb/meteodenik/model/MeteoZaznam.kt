package cz.utb.meteodenik.model

import java.time.LocalDateTime

/** Doménový model: jazyk aplikace, ne serveru (výčet místo čísla, datum místo textu). */
data class MeteoZaznam(
    val cas: LocalDateTime,
    val teplotaC: Double,
    val vlhkostProcent: Int,
    val rychlostVetruKmH: Double,
    val stav: StavPocasi
)

enum class StavPocasi {
    JASNO, POLOJASNO, ZATAZENO, MLHA, MRHOLENI,
    DEST, SNEZENI, PREHANKY, BOURKA, NEZNAMY
}