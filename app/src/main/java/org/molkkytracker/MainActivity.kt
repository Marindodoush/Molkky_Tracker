package org.molkkytracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.saveable.rememberSaveable
import org.molkkytracker.data.Team
import org.molkkytracker.ui.setup.*
import org.molkkytracker.ui.theme.MolkkyTrackerTheme

enum class Screen { HOME, SETUP, GAME, ALIAS, RULES, CREDITS, SETTINGS, HELP }

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MolkkyTrackerTheme {
                var currentScreen by rememberSaveable { mutableStateOf(Screen.HOME) }
                var currentTeams by remember { mutableStateOf<List<Team>?>(null) }

                // Gestion du bouton retour Android
                BackHandler(enabled = currentScreen != Screen.HOME) {
                    currentScreen = when (currentScreen) {
                        Screen.SETUP, Screen.ALIAS, Screen.RULES, Screen.CREDITS, Screen.SETTINGS, Screen.HELP -> Screen.HOME
                        Screen.GAME -> Screen.SETUP
                        Screen.HOME -> Screen.HOME
                    }
                }

                when (currentScreen) {
                    Screen.HOME -> HomeScreen(
                        onStartGame = { currentScreen = Screen.SETUP },
                        onManageAlias = { currentScreen = Screen.ALIAS },
                        onShowRules = { currentScreen = Screen.RULES },
                        onShowCredits = { currentScreen = Screen.CREDITS },
                        onShowSettings = { currentScreen = Screen.SETTINGS },
                        onShowHelp = { currentScreen = Screen.HELP }
                    )
                    Screen.SETUP -> SetupScreen(
                        onStartGame = { teams ->
                            currentTeams = teams
                            currentScreen = Screen.GAME
                        }
                    )
                    Screen.GAME -> org.molkkytracker.ui.game.GameScreen(
                        initialTeams = currentTeams!!,
                        onGameEnd = { 
                            currentTeams = null
                            currentScreen = Screen.HOME 
                        }
                    )
                    Screen.ALIAS -> AliasScreen(
                        onBack = { currentScreen = Screen.HOME }
                    )
                    Screen.RULES -> RulesScreen(
                        onBack = { currentScreen = Screen.HOME }
                    )
                    Screen.SETTINGS -> SettingsScreen(
                        onBack = { currentScreen = Screen.HOME }
                    )
                    Screen.CREDITS -> CreditsScreen(
                        onBack = { currentScreen = Screen.HOME }
                    )
                    Screen.HELP -> HelpScreen(
                        onBack = { currentScreen = Screen.HOME }
                    )
                }
            }
        }
    }
}
