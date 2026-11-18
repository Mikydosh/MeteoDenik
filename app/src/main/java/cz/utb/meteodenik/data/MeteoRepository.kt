package cz.utb.meteodenik.data

import cz.utb.meteodenik.model.MestoSPocasim
import kotlinx.coroutines.flow.Flow

interface MeteoRepository {
    /** ČTENÍ: živý pohled do databáze. Nikdy nevolá síť. */
    fun sledujMesta(): Flow<List<MestoSPocasim>>

    /** OBNOVA: síť → zápis do databáze. null = úspěch, jinak důvod selhání. Data nevrací. */
    suspend fun obnovPocasi(): DuvodSelhani?
}