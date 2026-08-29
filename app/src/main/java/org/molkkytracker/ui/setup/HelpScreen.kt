package org.molkkytracker.ui.setup

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.molkkytracker.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HelpScreen(onBack: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.help_title), style = MaterialTheme.typography.titleLarge) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            HelpSection(
                title = stringResource(R.string.help_config_title),
                description = stringResource(R.string.help_config_content)
            )
            HelpSection(
                title = stringResource(R.string.help_score_title),
                description = stringResource(R.string.help_score_content)
            )
            HelpSection(
                title = stringResource(R.string.help_stats_title),
                description = stringResource(R.string.help_stats_content)
            )
            HelpSection(
                title = stringResource(R.string.help_nav_title),
                description = stringResource(R.string.help_nav_content)
            )
            
            Spacer(Modifier.height(40.dp))
        }
    }
}

@Composable
fun HelpSection(title: String, description: String) {
    Column(modifier = Modifier.padding(vertical = 12.dp)) {
        Text(
            text = title, 
            style = androidx.compose.ui.text.TextStyle(
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = androidx.compose.ui.text.font.FontFamily.SansSerif
            ),
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            text = description, 
            style = androidx.compose.ui.text.TextStyle(
                fontSize = 16.sp,
                fontFamily = androidx.compose.ui.text.font.FontFamily.SansSerif,
                lineHeight = 24.sp
            )
        )
    }
}
