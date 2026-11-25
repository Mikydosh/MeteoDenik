# Lekce 06 – žádost o polohu

## Čtyři stavy

| Stav | Kdy nastane | Co vidí uživatel | Screenshot |
|---|---|---|---|
| NEZNAMY | první spuštění, ještě jsme se neptali | tlačítko "Zjistit polohu", po klepnutí systémový dialog | docs/lekce-06-stav-1-neznamy.png |
| ODMITNUTO | jedno "Don't allow", smíme se zeptat znovu | po klepnutí nejdřív naše vysvětlení, až po "Pokračovat" dialog | docs/lekce-06-stav-2-odmitnuto.png |
| ODMITNUTO_NATRVALO | druhé "Don't allow" | text, že bez polohy funguje vše ostatní + "Otevřít nastavení", žádný dialog | docs/lekce-06-stav-3-natrvalo.png |
| UDELENO | uživatel povolil v dialogu nebo v Nastavení | "Poloha je povolena" + souřadnice (nebo hláška o neznámé poloze) | docs/lekce-06-stav-4-udeleno.png |

## Proč se trvalé zamítnutí vyhodnocuje v callbacku
Funkce shouldShowRequestPermissionRationale vrací true po prvním odmítnutí, ale false jak před prvním dotazem, tak po trvalém (druhém) zamítnutí, takže při startu ty dva stavy nerozliším. Callback spouštěče se zavolá až po tom, co jsme se ptali a uživatel odpověděl. V tu chvíli víme jistě, že jsme se ptali. Pak platí:

- uživatel povolil → UDELENO
- uživatel odmítl a shouldShow… vrací true → ODMITNUTO
- uživatel odmítl a shouldShow… vrací false → ODMITNUTO_NATRVALO

## Proč ACCESS_COARSE_LOCATION a ne ACCESS_FINE_LOCATION
COARSE je přibližná poloha (řádově kilometry), FINE obvykle desítky metrů. 
Na určení města stačí přibližná poloha, čímž dochází k menšímu ohrožení uživatele.