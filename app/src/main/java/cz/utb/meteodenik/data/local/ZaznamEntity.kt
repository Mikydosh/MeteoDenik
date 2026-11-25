package cz.utb.meteodenik.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(
    tableName = "zaznamy",
    foreignKeys = [ForeignKey(
        entity = MestoEntity::class,
        parentColumns = ["id"],
        childColumns = ["mesto_id"],
        onDelete = ForeignKey.CASCADE      // smazané město smaže i svá měření
    )],
    indices = [Index("mesto_id")]
)
data class ZaznamEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "mesto_id") val mestoId: Int,
    @ColumnInfo(name = "zmereno_v") val zmerenoV: LocalDateTime,
    @ColumnInfo(name = "teplota_c") val teplotaC: Double,
    @ColumnInfo(name = "vlhkost_procent") val vlhkostProcent: Int,
    @ColumnInfo(name = "vitr_kmh") val rychlostVetruKmH: Double,
    @ColumnInfo(name = "kod_stavu") val kodStavu: String
)