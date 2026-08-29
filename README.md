# Mölkky Tracker

Mölkky_Tracker is an Android application for tracking Mölkky games, under GPL-3 license.
Mölkky_Tracker was created by Marindodoush (design, visuals, feature selection) with the valuable help of Android-Studio for the code.

## Implemented Rules

- Max score 50; if exceeded, drops back to 25 (`GameEngine.kt`)
- Consecutive misses penalty: Turn-skipping after 3 misses (`MissRule.kt`, `GameEngine.kt`)
- The miss counter is specific to each team and resets as soon as a throw hits a pin, or after applying the penalty.

## Features

- Alias memorization (Player management)
- Automatic turn-order management based on teams.
- Random team generation.
- Support for 5 languages: English (default), French, German, Spanish, and Suomi (Finnish).
- Direct download link for a rules sheet (French only) with DIY build tips.
- Game stats and history for each person during a session.
- Inclusive design: Color-blind friendly icons and gender-neutral text.

## Compilation

Standard Gradle project: open in Android Studio or run `./gradlew assembleDebug`.
