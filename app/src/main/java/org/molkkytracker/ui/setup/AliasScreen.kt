package org.molkkytracker.ui.setup

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.res.stringResource
import org.molkkytracker.R
import org.molkkytracker.data.Player

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AliasScreen(
    viewModel: SetupViewModel = viewModel(),
    onBack: () -> Unit
) {
    val aliasList by viewModel.rememberedNames.collectAsStateWithLifecycle()
    var aliasToEdit by remember { mutableStateOf<Player?>(null) }
    var newAliasName by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.alias_manage_title), style = MaterialTheme.typography.titleLarge) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.alias_back),
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }
            )
        }
    ) { padding ->
        Column(Modifier.padding(padding).fillMaxSize().padding(16.dp)) {
            // Ajouter un nouvel alias
            Row(verticalAlignment = Alignment.CenterVertically) {
                OutlinedTextField(
                    value = newAliasName,
                    onValueChange = { newAliasName = it },
                    label = { Text(stringResource(R.string.alias_new_placeholder)) },
                    modifier = Modifier.weight(1f),
                    shape = MaterialTheme.shapes.medium
                )
                Spacer(Modifier.width(8.dp))
                Button(
                    onClick = { 
                        if (newAliasName.isNotBlank()) {
                            viewModel.savePlayer(newAliasName)
                            newAliasName = ""
                        }
                    },
                    modifier = Modifier.height(56.dp)
                ) {
                    Text(stringResource(R.string.alias_add_button))
                }
            }

            Spacer(Modifier.height(24.dp))

            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(aliasList) { player ->
                    AliasRow(
                        player = player,
                        onDelete = { viewModel.deletePlayer(player) },
                        onEdit = { aliasToEdit = player }
                    )
                }
            }
        }
    }

    // Dialog de modification (simplifié pour l'instant)
    if (aliasToEdit != null) {
        AlertDialog(
            onDismissRequest = { aliasToEdit = null },
            title = { Text(stringResource(R.string.alias_edit_dialog)) },
            confirmButton = {
                TextButton(onClick = { aliasToEdit = null }) { Text("OK") }
            }
        )
    }
}

@Composable
fun AliasRow(player: Player, onDelete: () -> Unit, onEdit: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = player.name,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.weight(1f)
            )
            IconButton(onClick = onEdit) {
                Icon(Icons.Default.Edit, contentDescription = "Modifier")
            }
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "Supprimer", tint = MaterialTheme.colorScheme.error)
            }
        }
    }
}
