# Mölkky Tracker

Mölkky_Tracker es una aplicación Android para el seguimiento de partidas de Mölkky, bajo licencia GPL-3.
Mölkky_Tracker ha sido creada por Marindodoush (diseño, visuales, elección de funcionalidades) con la valiosa ayuda de Android-Studio para la redacción del código.

## Reglas Implementadas

- Puntuación máxima 50; en caso de superarla, baja a 25 (`GameEngine.kt`)
- Gestión de 3 fallos consecutivos: saltar turno (`MissRule.kt`, `GameEngine.kt`)
- El contador de fallos es específico para cada equipo y se reinicia en cuanto un lanzamiento golpea un bolo, o tras aplicar la penalización.

## Funcionalidades

- Posibilidad de memorizar Alias.
- Gestión automática del orden de turno según el equipo.
- Generación aleatoria de equipos.
- Disponible en 5 idiomas: Inglés (por defecto), Francés, Alemán, Español y Suomi (finlandés).
- Enlace de descarga de una ficha de reglas (solo en francés) con consejos de fabricación.
- Estadísticas e historial de juego para cada persona durante una sesión.
- Aplicación inclusiva: adaptada para el daltonismo e inclusividad de género en el texto.
