# Mölkky Tracker

Mölkky_Tracker on Android-sovellus Mölkky-pelien tulosten seurantaan, GPL-3-lisenssillä.
Mölkky_Trackerin on luonut Marindodoush (suunnittelu, ulkoasu, toiminnot) Android-Studion arvokkaalla avulla koodin kirjoittamisessa.

## Toteutetut Säännöt

- Maksimipisteet 50; jos ylittyy, pisteet putoavat 25:een (`GameEngine.kt`)
- Kolmen peräkkäisen ohilaukauksen rangaistus: vuoron väliinjättö (`MissRule.kt`, `GameEngine.kt`)
- Ohilaukauslaskuri on joukkuekohtainen ja nollautuu heti, kun heitto osuu keilaan tai rangaistus on suoritettu.

## Ominaisuudet

- Aliasten tallennusmahdollisuus.
- Automaattinen pelijärjestyksen hallinta joukkueiden perusteella.
- Mahdollisuus satunnaiseen joukkueiden muodostamiseen.
- Tukee 5 kieltä: englanti (oletus), ranska, saksa, espanja ja suomi.
- Latauslinkki sääntölomakkeeseen (vain ranskaksi), sisältää vinkkejä oman Mölkky-setin rakentamiseen.
- Tilastot ja pelihistoria jokaiselle pelaajalle istunnon aikana.
- Inklusiivinen sovellus: huomioi värisokeuden ja kielellisen inklusiivisuuden.
