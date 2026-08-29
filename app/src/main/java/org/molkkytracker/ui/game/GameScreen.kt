package org.molkkytracker.ui.game

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.res.stringResource
import org.molkkytracker.R
import org.molkkytracker.data.Team
import org.molkkytracker.data.TeamStyle
import org.molkkytracker.ui.theme.MolkkyTrackerTheme
import org.molkkytracker.ui.theme.TeamPattern

@Composable
fun GameScreen(
    initialTeams: List<Team>,
    viewModel: GameViewModel = viewModel(),
    onGameEnd: () -> Unit
) {
    LaunchedEffect(initialTeams) {
        if (viewModel.teams.isEmpty()) {
            viewModel.start(initialTeams)
        }
    }

    val teams = viewModel.teams
    val turnOrder = viewModel.turnOrder
    val currentTurnIndex = viewModel.currentTurnIndex
    val isGameOver = viewModel.isGameOver

    if (teams.isEmpty() || turnOrder.isEmpty()) return

    Box(Modifier.fillMaxSize()) {
        Column(Modifier.fillMaxSize().padding(horizontal = 16.dp, vertical = 8.dp)) {
            // Liste des joueurs dans l'ordre de passage
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp),
                contentPadding = PaddingValues(bottom = 16.dp, top = 8.dp)
            ) {
                itemsIndexed(turnOrder) { index, (tIdx, pIdx) ->
                    val team = teams[tIdx]
                    val playerName = team.playerNames[pIdx]
                    val isCurrent = index == currentTurnIndex && !isGameOver
                    val lastScore = team.lastScores[playerName]

                    PlayerScoreRow(
                        name = playerName,
                        team = team,
                        isCurrent = isCurrent,
                        lastScore = lastScore,
                        onNameClick = { viewModel.showHistory(playerName, team) },
                        onScoreSelected = { points ->
                            viewModel.applyPoints(points)
                        }
                    )
                }
            }

            // Résumé des équipes en bas (Grille adaptative)
            val gridColumns = when {
                teams.size <= 3 -> teams.size
                teams.size == 4 -> 2
                else -> 3
            }

            Surface(
                tonalElevation = 8.dp,
                shadowElevation = 8.dp,
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                shape = MaterialTheme.shapes.medium
            ) {
                Column(modifier = Modifier.padding(4.dp)) {
                    // Utilisation d'un FlowRow-like ou Grid simple pour le bas
                    val rows = teams.chunked(gridColumns)
                    rows.forEach { rowTeams ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            rowTeams.forEach { team ->
                                TeamScoreCard(team, modifier = Modifier.weight(1f).padding(2.dp))
                            }
                            // Compléter la ligne si nécessaire pour garder l'alignement
                            repeat(gridColumns - rowTeams.size) {
                                Spacer(Modifier.weight(1f).padding(2.dp))
                            }
                        }
                    }
                }
            }

            if (isGameOver) {
                Spacer(Modifier.height(16.dp))
                GameOverMenu(
                    onRestartSame = { viewModel.restartSameTeams() },
                    onRestartNew = { viewModel.restartNewTeams(teams.size) },
                    onHome = onGameEnd
                )
            }
        }

        // Dialog d'historique
        viewModel.selectedPlayerHistoryDetail?.let { detail ->
            AlertDialog(
                onDismissRequest = { viewModel.selectedPlayerHistoryDetail = null },
                confirmButton = {
                    TextButton(onClick = { viewModel.selectedPlayerHistoryDetail = null }) {
                        Text(stringResource(R.string.game_close), style = MaterialTheme.typography.bodyMedium)
                    }
                },
                title = { Text(stringResource(R.string.game_stats_title, detail.name), style = MaterialTheme.typography.titleLarge) },
                text = {
                    LazyColumn(modifier = Modifier.fillMaxWidth().heightIn(max = 400.dp)) {
                        item {
                            Text(stringResource(R.string.game_current_session), style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)
                            if (detail.currentGameScores.isEmpty()) {
                                Text(stringResource(R.string.game_no_score), style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurface)
                            } else {
                                Text(
                                    text = stringResource(R.string.game_throws, detail.currentGameScores.size, detail.currentGameScores.joinToString(", ")),
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = stringResource(R.string.game_total_points, detail.currentGameScores.sum()),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            Spacer(Modifier.height(16.dp))
                            HorizontalDivider()
                            Spacer(Modifier.height(16.dp))
                            Text(stringResource(R.string.game_session_history), style = MaterialTheme.typography.titleMedium)
                        }

                        if (detail.sessionHistory.isEmpty()) {
                            item { Text(stringResource(R.string.game_no_score), style = MaterialTheme.typography.bodyMedium) }
                        } else {
                            itemsIndexed(detail.sessionHistory) { idx, entry ->
                                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 4.dp)) {
                                    // Rappel de la couleur
                                    Box(
                                        Modifier.size(24.dp)
                                            .background(Color(entry.style.colorHex), MaterialTheme.shapes.extraSmall)
                                            .border(1.dp, MaterialTheme.colorScheme.onSurface, MaterialTheme.shapes.extraSmall),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        TeamPattern(entry.style.pattern, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f))
                                    }
                                    Spacer(Modifier.width(8.dp))
                                    Column {
                                        Text(stringResource(R.string.game_history_entry, entry.gameIndex), style = MaterialTheme.typography.bodyMedium)
                                        Text(
                                            text = stringResource(R.string.game_throws, entry.scores.size, entry.scores.joinToString(", ")) + " (" + stringResource(R.string.game_total_points, entry.scores.sum()) + ")",
                                            style = MaterialTheme.typography.bodySmall, 
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            )
        }
    }
}

@Composable
fun GameOverMenu(
    onRestartSame: () -> Unit,
    onRestartNew: () -> Unit,
    onHome: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.9f), MaterialTheme.shapes.medium)
            .border(2.dp, MaterialTheme.colorScheme.primary, MaterialTheme.shapes.medium)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Button(onClick = onRestartSame, modifier = Modifier.fillMaxWidth()) {
            Text(stringResource(R.string.game_restart_same), style = MaterialTheme.typography.bodyMedium)
        }
        Button(onClick = onRestartNew, modifier = Modifier.fillMaxWidth()) {
            Text(stringResource(R.string.game_restart_new), style = MaterialTheme.typography.bodyMedium)
        }
        OutlinedButton(onClick = onHome, modifier = Modifier.fillMaxWidth()) {
            Text(stringResource(R.string.game_home), style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Composable
fun PlayerScoreRow(
    name: String,
    team: Team,
    isCurrent: Boolean,
    lastScore: Int?,
    onNameClick: () -> Unit,
    onScoreSelected: (Int) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .background(
                if (isCurrent) MaterialTheme.colorScheme.primary.copy(alpha = 0.15f) else Color.Transparent,
                MaterialTheme.shapes.medium
            )
            .border(
                width = if (isCurrent) 3.dp else 1.dp,
                color = if (isCurrent) MaterialTheme.colorScheme.primary else Color.Gray.copy(alpha = 0.3f),
                shape = MaterialTheme.shapes.medium
            )
            .padding(vertical = 4.dp, horizontal = 0.dp) // Pas de padding horizontal ici
    ) {
        // Nom du joueur
        Box(
            modifier = Modifier
                .weight(1f)
                .height(56.dp)
                .clickable { onNameClick() }
                .padding(start = 12.dp, end = 8.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = name,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.weight(1f)
                )
                // Icône d'équipe à droite du nom
                TeamPattern(
                    pattern = team.style.pattern,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        Spacer(Modifier.width(0.dp)) // Plus besoin de spacer

        // Case de score/couleur
        Box(
            modifier = Modifier
                .padding(end = 4.dp) // Petit décalage pour ne pas toucher le bord
                .size(56.dp)
                .background(Color(team.style.colorHex), MaterialTheme.shapes.medium)
                .border(2.dp, MaterialTheme.colorScheme.onSurface, MaterialTheme.shapes.medium)
                .clickable { if (isCurrent) expanded = true },
            contentAlignment = Alignment.Center
        ) {
            if (lastScore != null) {
                Text(
                    text = lastScore.toString(),
                    style = MaterialTheme.typography.displayMedium,
                    color = Color.Black
                )
            } else {
                TeamPattern(team.style.pattern, Color.Black.copy(alpha = 0.7f), Modifier.size(24.dp))
            }

            DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                (0..12).forEach { points ->
                    DropdownMenuItem(
                        text = { 
                            Text(
                                text = points.toString(),
                                style = MaterialTheme.typography.displayMedium.copy(fontSize = 22.sp),
                                modifier = Modifier.padding(vertical = 2.dp)
                            ) 
                        },
                        onClick = {
                            onScoreSelected(points)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun TeamScoreCard(team: Team, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .height(56.dp)
            .background(Color(team.style.colorHex), MaterialTheme.shapes.medium)
            .border(2.dp, MaterialTheme.colorScheme.onSurface, MaterialTheme.shapes.medium)
    ) {
        // Rappel d'icône d'équipe (Haut Gauche)
        TeamPattern(
            pattern = team.style.pattern,
            color = Color.Black.copy(alpha = 0.4f),
            modifier = Modifier.padding(4.dp).size(16.dp).align(Alignment.TopStart)
        )

        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Score au centre
            Box(
                modifier = Modifier.weight(1f).fillMaxHeight(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = team.score.toString(),
                    style = MaterialTheme.typography.displayMedium,
                    color = Color.Black
                )
            }
            
            // Barre verticale de séparation
            Box(Modifier.width(2.dp).fillMaxHeight().background(MaterialTheme.colorScheme.onSurface))
            
            // Les 3 cases pour les coups manqués à droite
            Column(
                modifier = Modifier.width(24.dp).fillMaxHeight().background(Color.Black.copy(alpha = 0.1f)),
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                repeat(3) { i ->
                    val isMissed = i < team.consecutiveMisses
                    Box(
                        modifier = Modifier
                            .size(14.dp)
                            .background(
                                if (isMissed) Color.Red else Color.White.copy(alpha = 0.5f),
                                MaterialTheme.shapes.extraSmall
                            )
                            .border(1.dp, Color.Black, MaterialTheme.shapes.extraSmall),
                        contentAlignment = Alignment.Center
                    ) {
                        if (isMissed) {
                            Text(
                                text = "x",
                                color = Color.White,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Black
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GameScreenPreview() {
    val mockTeams = listOf(
        Team(0, TeamStyle.CYAN, listOf("Raphaël", "Julie"), score = 24),
        Team(1, TeamStyle.YELLOW, listOf("Manu", "Framboise"), score = 37, consecutiveMisses = 1)
    )
    MolkkyTrackerTheme {
        GameScreen(initialTeams = mockTeams, onGameEnd = {})
    }
}
