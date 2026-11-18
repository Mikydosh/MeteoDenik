package cz.utb.meteodenik.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [MestoEntity::class, ZaznamEntity::class],
    version = 1,
    exportSchema = true
)
@TypeConverters(Prevodniky::class)
abstract class MeteoDatabaze : RoomDatabase() {
    abstract fun mestoDao(): MestoDao        // tělo dopíše KSP
}