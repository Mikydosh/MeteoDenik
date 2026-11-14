package cz.utb.meteodenik.data

import cz.utb.meteodenik.model.Mesto
import cz.utb.meteodenik.model.MeteoZaznam

interface MeteoRepository {
    /** Aktuální počasí pro město. Vrací doménový model, nikdy DTO. */
    suspend fun aktualniPocasi(mesto: Mesto, vynutitObnovu: Boolean = false): VysledekSite<MeteoZaznam>
}