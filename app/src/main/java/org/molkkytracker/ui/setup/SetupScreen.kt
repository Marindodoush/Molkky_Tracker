package org.molkkytracker.ui.setup

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import org.molkkytracker.R
import org.molkkytracker.data.PlayerSlot
import org.molkkytracker.data.Team
import org.molkkytracker.data.TeamStyle
import org.molkkytracker.ui.theme.MolkkyTrackerTheme
import org.molkkytracker.ui.theme.TeamPattern

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SetupScreen(
    viewModel: SetupViewModel = viewModel(),
    onStartGame: (List<Team>) -> Unit
) {
    val slots = viewModel.slots
    val rememberedPlayers by viewModel.rememberedNames.collectAsStateWithLifecycle()
    val usedNames = slots.map { it.name }.filter { it.isNotBlank() }
    var menuExpanded by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current

    Column(
        Modifier
            .fillMaxSize()
            .padding(16.dp)
            .imePadding()
    ) {
        Text(
            text = stringResource(R.string.setup_title),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.fillMaxWidth(),
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
        Spacer(Modifier.height(16.dp))

        LazyColumn(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(slots.indices.toList()) { index ->
                PlayerRow(
                    name = slots[index].name,
                    style = slots[index].style,
                    incomplete = viewModel.isSlotIncomplete(index),
                    rememberedNames = rememberedPlayers.map { it.name }.filter { it !in usedNames || it == slots[index].name },
                    onNameChange = { viewModel.updateName(index, it) },
                    onStyleClick = { viewModel.cycleStyle(index) },
                    onSaveName = { viewModel.savePlayer(it) },
                    imeAction = if (index == slots.size - 1) ImeAction.Done else ImeAction.Next,
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                )
            }
        }

        Spacer(Modifier.height(12.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(R.string.setup_game_mode),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
            
            ExposedDropdownMenuBox(
                expanded = menuExpanded,
                onExpandedChange = { menuExpanded = !menuExpanded }
            ) {
                val selectedText = when (viewModel.teamCount) {
                    0 -> stringResource(R.string.setup_individual)
                    else -> stringResource(R.string.setup_teams, viewModel.teamCount)
                }
                
                OutlinedTextField(
                    value = selectedText,
                    onValueChange = {},
                    readOnly = true,
                    modifier = Modifier.menuAnchor().width(160.dp),
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = menuExpanded) },
                    textStyle = MaterialTheme.typography.bodyLarge,
                    shape = MaterialTheme.shapes.medium
                )
                
                ExposedDropdownMenu(
                    expanded = menuExpanded,
                    onDismissRequest = { menuExpanded = false },
                    modifier = Modifier.background(MaterialTheme.colorScheme.surface)
                ) {
                    listOf(0, 2, 3, 4).forEach { count ->
                        DropdownMenuItem(
                            text = { 
                                Text(
                                    text = if (count == 0) stringResource(R.string.setup_individual) else stringResource(R.string.setup_teams, count),
                                    style = MaterialTheme.typography.bodyLarge
                                ) 
                            },
                            onClick = {
                                viewModel.updateTeamCount(count)
                                menuExpanded = false
                            }
                        )
                    }
                }
            }
        }

        Spacer(Modifier.height(16.dp))
        Button(
            onClick = { viewModel.buildTeamsOrNull()?.let { onStartGame(it) } },
            modifier = Modifier.fillMaxWidth().height(64.dp)
        ) {
            Text(
                text = stringResource(R.string.setup_start_button),
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PlayerRow(
    name: String,
    style: TeamStyle?,
    incomplete: Boolean,
    rememberedNames: List<String>,
    onNameChange: (String) -> Unit,
    onStyleClick: () -> Unit,
    onSaveName: (String) -> Unit,
    imeAction: ImeAction,
    onNext: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val isNewName = name.isNotBlank() && !rememberedNames.contains(name)

    Row(verticalAlignment = Alignment.CenterVertically) {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { if (rememberedNames.any { it.contains(name, ignoreCase = true) }) expanded = !expanded },
            modifier = Modifier.weight(1f)
        ) {
            OutlinedTextField(
                value = name,
                onValueChange = {
                    onNameChange(it)
                    // N'ouvrir le menu que s'il y a des suggestions
                    expanded = it.isNotBlank() && rememberedNames.any { saved -> saved.contains(it, ignoreCase = true) }
                },
                placeholder = { Text(stringResource(R.string.setup_alias_placeholder)) },
                isError = incomplete,
                textStyle = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.menuAnchor(),
                keyboardOptions = KeyboardOptions(imeAction = imeAction),
                keyboardActions = KeyboardActions(onNext = { onNext() }, onDone = { onNext() }),
                trailingIcon = {
                    if (isNewName) {
                        IconButton(onClick = { onSaveName(name) }) {
                            Icon(
                                painter = painterResource(id = android.R.drawable.ic_menu_save),
                                contentDescription = "Save",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                },
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = if (incomplete) MaterialTheme.colorScheme.error else Color(0xFF8DB600),
                    focusedBorderColor = if (incomplete) MaterialTheme.colorScheme.error else Color(0xFF8DB600)
                ),
                shape = MaterialTheme.shapes.medium
            )

            val filteredNames = rememberedNames.filter { it.contains(name, ignoreCase = true) }
            if (filteredNames.isNotEmpty()) {
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier.background(MaterialTheme.colorScheme.surface)
                ) {
                    filteredNames.forEach { savedName ->
                        DropdownMenuItem(
                            text = { Text(savedName, style = MaterialTheme.typography.bodyLarge) },
                            onClick = {
                                onNameChange(savedName)
                                expanded = false
                            }
                        )
                    }
                }
            }
        }
        Spacer(Modifier.width(8.dp))
        Box(
            modifier = Modifier
                .size(56.dp)
                .background(
                    color = style?.let { Color(it.colorHex) } ?: Color.Transparent,
                    shape = MaterialTheme.shapes.small
                )
                .border(
                    width = if (incomplete) 3.dp else 1.dp,
                    color = if (incomplete) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.outline,
                    shape = MaterialTheme.shapes.small
                )
                .clickable(onClick = onStyleClick),
            contentAlignment = Alignment.Center
        ) {
            if (style != null) {
                TeamPattern(style.pattern, Color.Black.copy(alpha = 0.7f))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SetupScreenPreview() {
    MolkkyTrackerTheme {
        SetupScreen(onStartGame = {})
    }
}
