package cz.utb.meteodenik.data

sealed interface VysledekSite<out T> {
    data class Uspech<out T>(val data: T) : VysledekSite<T>
    data class Selhani(val duvod: DuvodSelhani) : VysledekSite<Nothing>
}

/** Důvody pojmenované z pohledu uživatele, ne knihovny. Texty jsou ve strings.xml. */
enum class DuvodSelhani {
    BEZ_PRIPOJENI,
    SERVER_NEODPOVIDA,
    PRILIS_MNOHO_DOTAZU,
    CHYBA_POZADAVKU,
    CHYBA_SERVERU,
    NEPLATNA_ODPOVED,
    JINA_CHYBA_SITE
}