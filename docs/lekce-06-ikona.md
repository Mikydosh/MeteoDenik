# Lekce 06 – adaptivní ikona

Jako motiv jsem použil clip art **cloud** z Vector Asset Studia, uložený
jako `ic_meteo.xml`. Pozadí ikony je **modré** (vrstva Background jako Color)
a motiv jsem v Image Asset Studiu zmenšil posuvníkem Resize na **75 %**.
Stejný mrak jsem použil i ve vrstvě Monochrome, aby fungovala tematizovaná ikona.

## Z čeho se adaptivní ikona skládá

Tři vrstvy: pozadí (`<background>`), motiv (`<foreground>`) a jednobarevná silueta
(`<monochrome>`) pro tematizované ikony od Androidu 13.

## Rozměr vrstvy a bezpečná zóna

Každá vrstva má rozměr 108 × 108 dp. Maska (kruh, squircle, čtverec – podle výrobce
telefonu) z ní ukáže nejvýš vnitřních 72 × 72 dp. Zbylý okraj 18 dp na každé straně je
rezerva pro vizuální efekty, například paralaxu při posunu plochy. Motiv se v každém tvaru
masky zaručeně vejde jen do středního kruhu o průměru 66 dp, to je bezpečná zóna.

## Proč jsem motiv zmenšil

Kdybych mrak nechal ve výchozí velikosti, na jednom telefonu by byl vidět celý a na jiném
oříznutý, protože každý výrobce používá jinou masku. Zmenšením na 75 % jsem ho dostal
dovnitř bezpečné zóny a v náhledu jsem prošel všechny tvary masky (Circle, Squircle,
Rounded Square…), aby nebyl nikde oříznutý.

## Screenshoty

- běžná ikona: `lekce-06-ikona.png`
- tematizovaná ikona (Themed icons): `lekce-06-ikona-themed.png`