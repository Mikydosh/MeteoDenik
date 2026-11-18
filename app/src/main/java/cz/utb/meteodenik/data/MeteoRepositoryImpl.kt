package cz.utb.meteodenik.data

import cz.utb.meteodenik.data.local.MestoDao
import cz.utb.meteodenik.data.local.doDomeny
import cz.utb.meteodenik.data.local.naEntitu
import cz.utb.meteodenik.data.remote.MeteoApi
import cz.utb.meteodenik.data.remote.toDomain
import cz.utb.meteodenik.model.MestoSPocasim
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MeteoRepositoryImpl @Inject constructor(
    private val dao: MestoDao,
    private val meteoApi: MeteoApi
) : MeteoRepository {

    override fun sledujMesta(): Flow<List<MestoSPocasim>> =
        dao.sledujMestaSMerenimi().map { radky -> radky.map { it.doDomeny() } }

    override suspend fun obnovPocasi(): DuvodSelhani? {
        var prvniChyba: DuvodSelhani? = null
        for (mesto in dao.nactiMesta()) {
            // toDomain() zůstává UVNITŘ bezpecneVolani, aby se chytila i chyba parsování
            val vysledek = bezpecneVolani {
                meteoApi.aktualniPocasi(sirka = mesto.sirka, delka = mesto.delka).toDomain()
            }
            when (vysledek) {
                is VysledekSite.Uspech ->
                    // Jediný zápis do zdroje pravdy, UI se o něm dozví přes Flow.
                    dao.ulozZaznam(vysledek.data.naEntitu(mesto.id))
                is VysledekSite.Selhani ->
                    if (prvniChyba == null) prvniChyba = vysledek.duvod
            }
        }
        return prvniChyba
    }
}