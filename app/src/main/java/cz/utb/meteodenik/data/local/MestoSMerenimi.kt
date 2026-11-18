package cz.utb.meteodenik.data.local

import androidx.room.Embedded
import androidx.room.Relation

/** NENÍ @Entity, jen tvar výsledku dotazu: město + jeho měření. */
data class MestoSMerenimi(
    @Embedded val mesto: MestoEntity,
    @Relation(parentColumn = "id", entityColumn = "mesto_id")
    val mereni: List<ZaznamEntity>
)