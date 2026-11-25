# Lekce 7 – cvičení 7.6: Debug (čtyři tiché chyby)

Soubor: `ui/RozbityMestaSeznam.kt`. Chyby nevyvolaly ani chybovou hlášku, ani pád.

| # | Chyba | Vinný řádek | Proč se to děje | Oprava |
|---|---|---|---|---|
| A | Do pole nejde psát, kurzor bliká, nic se nezobrazí. Po klepnutí na Přidat se nic nevytvoří. | `var novyNazev = ""` | Je to lokální proměnná, ne sledovaný stav. `onValueChange` do ní zapíše, ale Compose o změně neví, takže nerekomponuje a pole dál ukazuje původní prázdný text. Tlačítko čte tutéž proměnnou, ale ta je v okamžiku kliknutí prázdná, proto se větev s přidáním města přeskočí. | `var novyNazev by rememberSaveable { mutableStateOf("") }` |
| B | Barevná ploška u „Počet měst“ těsně obepíná text a kolem ní je 8 dp nevybarvené mezery. | `.padding(8.dp).background(...)` | Modifikátory se aplikují zleva doprava a každý obalí všechno za sebou. Nejdřív se odsadí a pozadí se nakreslí až uvnitř odsazení, takže obepíná jen text. | Prohodit pořadí: `.background(...).padding(8.dp)` |
| C | Po smazání „Zlína“ je rozbalená „Olomouc“ místo „Ostravy“. | `items(mesta) { ... }` bez `key` (a `var rozbaleno by remember` uvnitř položky) | Bez klíče se stav položky váže na její pozici v seznamu, ne na položku samotnou. Po smazání se pozice posunou a stav `rozbaleno` zůstane u pozice, na které teď stojí jiné město. | `items(mesta, key = { it })` |
| D | Texty natvrdo v kódu, aplikace nejde přeložit (v angličtině zůstanou česky). | `Text("Přidat")`, `Text("Smazat")`, `label = { Text("Název města") }`, `"Počet měst: ..."`, `"Teplotu doplníme v lekci 11"` | Lint v Compose nehlídá texty v kódu jako v XML, takže nic nevaruje. Texty musí být ve `strings.xml`, aby šly přeložit. | Přesunout do `strings.xml` (cs + en) a volat `stringResource(...)` nebo `pluralStringResource(...)` |
