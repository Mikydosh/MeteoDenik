package cz.utb.meteodenik.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "mesta",
    indices = [Index(value = ["nazev"], unique = true)]
)
data class MestoEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "nazev") val nazev: String,
    @ColumnInfo(name = "zem_sirka") val sirka: Double,
    @ColumnInfo(name = "zem_delka") val delka: Double
)