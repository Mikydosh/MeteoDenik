package cz.utb.meteodenik.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface MestoDao {

    /** ČTENÍ = Flow. Room sám vydá nový seznam po každé změně obou tabulek. */
    @Transaction
    @Query("SELECT * FROM mesta ORDER BY nazev")
    fun sledujMestaSMerenimi(): Flow<List<MestoSMerenimi>>

    /** Jednorázové čtení pro obnovu v Repository = suspend. */
    @Query("SELECT * FROM mesta ORDER BY nazev")
    suspend fun nactiMesta(): List<MestoEntity>

    /** ZÁPIS = suspend. IGNORE: duplicitní název tiše nic neudělá. */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun vlozMesto(mesto: MestoEntity): Long

    /** Nové měření (každá obnova přidá řádek, historie se hromadí). */
    @Upsert
    suspend fun ulozZaznam(zaznam: ZaznamEntity)

    @Delete
    suspend fun smazMesto(mesto: MestoEntity)
}