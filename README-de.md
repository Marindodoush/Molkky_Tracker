# Mölkky Tracker

Mölkky_Tracker ist eine Android-Anwendung zur Verfolgung von Mölkky-Spielen, lizenziert unter GPL-3.
Mölkky_Tracker wurde von Marindodoush (Design, Grafik, Funktionsauswahl) mit der wertvollen Hilfe von Android-Studio für die Codeerstellung erstellt.

## Implementierte Regeln

- Höchstpunktzahl 50; bei Überschreitung Rückfall auf 25 (`GameEngine.kt`)
- Regel für 3 aufeinanderfolgende Fehlwürfe: Aussetzen einer Runde (`MissRule.kt`, `GameEngine.kt`)
- Der Fehlwurfzähler gilt pro Team und wird zurückgesetzt, sobald ein Kegel getroffen wird oder die Strafe angewendet wurde.

## Funktionen

- Alias-Speicherung (Spielerverwaltung)
- Automatische Verwaltung der Spielreihenfolge basierend auf den Teams.
- Zufällige Teamgenerierung.
- Unterstützung von 5 Sprachen: Englisch (Standard), Französisch, Deutsch, Spanisch und Suomi (Finnisch).
- Download-Link für ein Regelblatt (nur auf Französisch) mit Tipps zum Eigenbau.
- Statistiken und Spielverlauf für jede Person während einer Sitzung.
- Inklusives Design: Unterstützung bei Farbenblindheit und geschlechtergerechte Texte.
