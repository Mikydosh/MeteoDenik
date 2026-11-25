package cz.utb.meteodenik.data.local

import cz.utb.meteodenik.model.Mesto
import cz.utb.meteodenik.model.MestoSPocasim
import cz.utb.meteodenik.model.MeteoZaznam
import cz.utb.meteodenik.model.StavPocasi

fun MestoEntity.doDomeny() = Mesto(id = id, nazev = nazev, sirka = sirka, delka = delka)

fun ZaznamEntity.doDomeny() = MeteoZaznam(
    cas = zmerenoV,
    teplotaC = teplotaC,
    vlhkostProcent = vlhkostProcent,
    rychlostVetruKmH = rychlostVetruKmH,
    stav = StavPocasi.entries.firstOrNull { it.name == kodStavu } ?: StavPocasi.NEZNAMY
)

/** Do UI jde vždy jen POSLEDNÍ měření města. */
fun MestoSMerenimi.doDomeny() = MestoSPocasim(
    mesto = mesto.doDomeny(),
    pocasi = mereni.maxByOrNull { it.zmerenoV }?.doDomeny()
)

fun MeteoZaznam.naEntitu(mestoId: Int) = ZaznamEntity(
    mestoId = mestoId,
    zmerenoV = cas,
    teplotaC = teplotaC,
    vlhkostProcent = vlhkostProcent,
    rychlostVetruKmH = rychlostVetruKmH,
    kodStavu = stav.name
)