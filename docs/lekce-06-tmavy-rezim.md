# Lekce 06 – tmavý režim: dvě cesty

Zkusil jsem obě cesty. Nejdřív `values/colors.xml` a `values-night/colors.xml`
se stejnými názvy `pozadi_karty` a `text_zvyrazneni`, použité přes `colorResource()`.
Pak totéž přes Compose téma: vlastní `lightColorScheme` a `darkColorScheme` v `Theme.kt`,
vypnuté dynamické barvy a v UI `MaterialTheme.colorScheme.surfaceVariant` a `primary`.

Pro Compose projekt je lepší téma (druhá varianta). Barvy mám na jednom místě, kompilátor mi nedovolí
zapomenout barvu v jedné variantě (u `colors.xml` stačí překlep a tmavý režim
spadne na výchozí barvu). Navíc díky `@Preview` vidím světlou i tmavou verzi bez emulátoru.

Cesta přes `values-night` je užitečná hlavně pro části mimo Compose (XML téma, startovací
okno, adaptivní ikona). Soubory `colors.xml`
a `values-night/colors.xml` jsem v projektu nechal pro porovnání, po přepnutí na téma
je nic nepoužívá.

Dynamické barvy jsem vypnul, protože na API 31+ by vlastní paletu
přebila tapeta a vlastní barvy bych neviděl.