package cz.utb.meteodenik.di

import android.content.Context
import android.util.Log
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import cz.utb.meteodenik.data.local.MestoDao
import cz.utb.meteodenik.data.local.MeteoDatabaze
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabazeModul {

    @Provides
    @Singleton
    fun poskytniDatabazi(@ApplicationContext context: Context): MeteoDatabaze {
        Log.d("MeteoDb", "Vytvářím instanci databáze")   // v Logcatu právě JEDNOU
        return Room.databaseBuilder(context, MeteoDatabaze::class.java, "meteo.db")
            .addCallback(VychoziMesta)
            // POUZE VE VÝVOJI: chybí-li migrace, databázi smaže a založí prázdnou.
            .fallbackToDestructiveMigration(dropAllTables = true)
            .build()
    }

    /** DAO není @Singleton, je to jen ukazatel do databáze, která singleton je. */
    @Provides
    fun poskytniMestoDao(databaze: MeteoDatabaze): MestoDao = databaze.mestoDao()
}

/** Spustí se jednou, když se databázový soubor poprvé vytvoří. Města dřív držel MestaZdroj. */
private object VychoziMesta : RoomDatabase.Callback() {
    override fun onCreate(db: SupportSQLiteDatabase) {
        db.execSQL(
            "INSERT INTO mesta (nazev, zem_sirka, zem_delka) VALUES " +
                    "('Zlín', 49.2265, 17.6683), " +
                    "('Brno', 49.1951, 16.6068), " +
                    "('Ostrava', 49.8209, 18.2625)"
        )
    }
}