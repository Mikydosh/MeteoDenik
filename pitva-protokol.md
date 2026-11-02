# Dekompilační protokol – lekce 04 (cvičení 4.1)

Soubor `Pitva.kt`. Tlačítko `Decompile` v mém Android Studiu padá na chybě IDE, a tak byly zkontrolovány zkompilované třídy `.class` v dekompilovaném zobrazení souborů.

## Tabulka

| Konstrukce | Co jsem napsal | Co z toho kompilátor vyrobil | Řádků v Javě ručně |
|---|---|---|---|
| data class | `data class Zapis(val mesto: String, val teplota: Double, val poznamka: String = "")` | `getMesto()`, `getTeplota()`, `getPoznamka()`, `component1()`, `component2()`, `component3()`, `copy(String, double, String)`, `copy$default(...)`, `toString()`, `hashCode()`, `equals(Object)` | cca 45 |
| object | `object Prevody { const val ABSOLUTNI_NULA …; fun naFahrenheita(...) }` | `public static final Prevody INSTANCE`, privátní konstruktor, `naFahrenheita(double)`; `const val` je statické pole `ABSOLUTNI_NULA` | cca 10 |
| extension funkce | `fun Double.zaokrouhlenoNaDesetiny(): Double` | `public static final double zaokrouhlenoNaDesetiny(double)` ve třídě `PitvaKt` | cca 4 |
| funkce s lambdou | `fun prumer(zapisy: List<Zapis>, vyber: (Zapis) -> Double): Double` | `prumer(List<Zapis>, Function1<? super Zapis, Double>)`, lambda se volá přes `invoke` | cca 5 |
| suspend funkce | `suspend fun nactiPoZdrzeni(zapisy: List<Zapis>): List<Zapis>` | `Object nactiPoZdrzeni(List<Zapis>, Continuation<...>)` a pomocná třída `PitvaKt$nactiPoZdrzeni$1` s polem `label` | cca 35 |

## Odpovědi

**1. Kolik metod má třída Zapis a které z nich nemají ve zdrojovém kódu žádný náznak?**
Třída `Zapis` má 11 metod a 2 konstruktory. Tři gettery vznikly z `val`, ale `component1`, `component2`, `component3`, `copy`, `copy$default`, `toString`, `hashCode` a `equals` jsem nikde nenapsal. Přidal je kompilátor kvůli slovu `data`.


**2. Proč má Zapis konstruktor navíc, ten s parametry `int` a `DefaultConstructorMarker`?**
Parametr `poznamka` má výchozí hodnotu `""`, ale JVM výchozí hodnoty nezná. Kompilátor proto vyrobí pomocný konstruktor, který pomocí čísla `int` pozná, které argumenty volající nezadal, a doplní je. 
`DefaultConstructorMarker` jen odliší jeho podpis od ostatních konstruktorů.

**3. Jaký je první parametr metody `zaokrouhlenoNaDesetiny` a co z toho plyne?**
První parametr je `double`, tedy samotné číslo, na kterém funkci volám. Extension funkce je ve skutečnosti obyčejná statická metoda ve třídě `PitvaKt`, ne metoda třídy `Double`. Statická metoda se vybírá už při překladu, takže extension funkce není polymorfní.

**4. Jaký typ má druhý parametr funkce `prumer` a co to říká o funkci jako hodnotě?**
Druhý parametr má typ `Function1<? super Zapis, Double>`, což je rozhraní z knihovny Kotlinu s jedinou metodou `invoke`. Lambda je tedy na JVM obyčejný objekt a funkce `prumer` ji volá jako `vyber.invoke(zapis)`.

**5. Co se stalo s návratovým typem `nactiPoZdrzeni` a proč se objevil `switch`?**
Funkce teď vrací `Object` a má navíc parametr `Continuation`. Může totiž vrátit buď výsledek, nebo značku `COROUTINE_SUSPENDED` (pozastavila se kvůli `delay`). Kompilátor z ní udělal stavový automat: pole `label` si pamatuje, kde funkce skončila, a `switch` podle něj po probuzení skočí na správné místo.