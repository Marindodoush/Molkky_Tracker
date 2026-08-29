package org.molkkytracker.ui.setup

import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.os.LocaleListCompat
import org.molkkytracker.R

data class LanguageOption(val name: String, val code: String)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(onBack: () -> Unit) {
    val languages = listOf(
        LanguageOption("🌐 " + stringResource(R.string.settings_auto), ""),
        LanguageOption("🇫🇷 Français", "fr"),
        LanguageOption("🇬🇧 English", "en"),
        LanguageOption("🇩🇪 Deutsch", "de"),
        LanguageOption("🇪🇸 Español", "es"),
        LanguageOption("🇫🇮 Suomi", "fi")
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.settings_title), style = MaterialTheme.typography.titleLarge) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                    }
                }
            )
        }
    ) { padding ->
        Column(Modifier.padding(padding).fillMaxSize().padding(16.dp)) {
            Text(
                text = stringResource(R.string.settings_auto),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            
            Spacer(Modifier.height(16.dp))

            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(languages) { lang ->
                    LanguageRow(
                        language = lang,
                        onClick = {
                            val appLocale: LocaleListCompat = if (lang.code.isEmpty()) {
                                LocaleListCompat.getEmptyLocaleList()
                            } else {
                                LocaleListCompat.forLanguageTags(lang.code)
                            }
                            AppCompatDelegate.setApplicationLocales(appLocale)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun LanguageRow(language: LanguageOption, onClick: () -> Unit) {
    val currentLocale = AppCompatDelegate.getApplicationLocales().toLanguageTags()
    val isSelected = (language.code == currentLocale) || (language.code == "" && currentLocale.isEmpty())

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface
        ),
        border = if (isSelected) androidx.compose.foundation.BorderStroke(2.dp, MaterialTheme.colorScheme.primary) else null
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = language.name,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.weight(1f)
            )
            if (isSelected) {
                Text("✓", color = MaterialTheme.colorScheme.primary, style = MaterialTheme.typography.headlineSmall)
            }
        }
    }
}
