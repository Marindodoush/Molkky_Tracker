# Mölkky Tracker

Mölkky_Tracker est une application Android de suivi de parties de Mölkky, sous licence GPL-3.
Mölkky_Tracker a été créée par Marindodoush (conception, visuels, choix des fonctionnalités) avec l'aide précieuse du logiciel Android-Studio pour la rédaction du code.

## Règles implémentées

- Score max 50 ; en cas de dépassement, retombe à 25 (`GameEngine.kt`)
- Gestion des 3 échecs consécutifs d'une équipe : passe-tour (`MissRule.kt`, `GameEngine.kt`)
- Le compteur d'échecs est propre à chaque équipe et se remet à zéro dès qu'un lancer touche une quille, ou après application de la pénalité

## Fonctionnalités

- Possibilité de mémoriser des Alias
- Gestion automatique de l'ordre de passage des personnes, en fonction de leur équipe.
- Possibilité de générer des équipes aléatoirement.
- Mölkky_Tracker est accessible en 5 langues (Dans la langue de votre support si prise en charge par l'application, anglais par défaut, puis Français, Anglais, Allemand, Espagnol, Suomi (finlandais) au choix)
- Lien de téléchargement d'un fichier de règles (fr uniquement) avec quelques conseils de fabrication d'un Mölkky.
- Gestion de statistiques et historique de jeu pour chaque personne lors d'une session.
- Application inclusive, prise en charge du daltonisme et inclusivité de genre dans le texte.

## État actuel

- ✅ Structure Gradle du projet (Kotlin, Compose, Room, licence GPL-3.0)
- ✅ Modèle de données (`data/`) : joueurs mémorisés (Room), équipes, moteur de règles
- ✅ Écran de configuration (`ui/setup/`) : jusqu'à 8 joueurs, choix couleur/motif d'équipe (5 options), nombre d'équipes (2-4, tailles inégales possibles), auto-génération équipes + ordre de passage si aucune couleur saisie manuellement, indicateur visuel (contour rouge) sur les champs incomplets
- ✅ Écran de jeu (`ui/game/`) : saisie des scores, affichage des compteurs d'échecs, gestion de l'ordre de passage automatique, application des règles du `GameEngine`
- ✅ Historique de session : consultation des scores passés du joueur durant la session en cours (effacé à la fermeture)

## Reste à faire

- écouter les retours d'expériences...

## Compilation

Projet Gradle standard : ouvrir le dossier dans Android Studio, ou `./gradlew assembleDebug` (le wrapper Gradle n'est pas inclus dans cet export — à générer via `gradle wrapper` si besoin).
