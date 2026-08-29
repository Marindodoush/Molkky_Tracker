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
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.molkkytracker.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RulesScreen(onBack: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.menu_rules), style = MaterialTheme.typography.titleLarge) },
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
            // En-tête sur deux colonnes
            Row(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.weight(1f)) {
                    RuleTitle(stringResource(R.string.rules_materiel_title))
                    RuleText(stringResource(R.string.rules_materiel_content))
                }
                Spacer(Modifier.width(16.dp))
                Column(modifier = Modifier.weight(1f)) {
                    RuleTitle(stringResource(R.string.rules_but_title))
                    RuleText(stringResource(R.string.rules_but_content))
                }
            }

            Spacer(Modifier.height(24.dp))

            RuleTitle(stringResource(R.string.rules_deroulement_title))
            RuleText(stringResource(R.string.rules_deroulement_1))
            RuleText(stringResource(R.string.rules_deroulement_2))
            Spacer(Modifier.height(8.dp))
            RuleText(stringResource(R.string.rules_deroulement_3))

            Spacer(Modifier.height(24.dp))

            RuleTitle(stringResource(R.string.rules_score_title))
            RuleBullet(stringResource(R.string.rules_score_bullet_1))
            RuleBullet(stringResource(R.string.rules_score_bullet_2))
            RuleText(stringResource(R.string.rules_score_note))

            Spacer(Modifier.height(12.dp))
            RuleExample(stringResource(R.string.rules_score_example))

            Spacer(Modifier.height(24.dp))
            RuleText(stringResource(R.string.rules_overshoot), isBold = true)

            Spacer(Modifier.height(24.dp))
            RuleText(stringResource(R.string.rules_miss_rule))
            Spacer(Modifier.height(12.dp))
            RuleExample(stringResource(R.string.rules_miss_example))

            Spacer(Modifier.height(32.dp))

            RuleTitle(stringResource(R.string.rules_fin_title))
            RuleText(stringResource(R.string.rules_fin_content))
            
            Spacer(Modifier.height(40.dp))
        }
    }
}

@Composable
fun RuleTitle(text: String) {
    Text(
        text = text,
        style = androidx.compose.ui.text.TextStyle(
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = androidx.compose.ui.text.font.FontFamily.SansSerif
        ),
        modifier = Modifier.padding(bottom = 4.dp, top = 12.dp)
    )
}

@Composable
fun RuleText(text: String, isBold: Boolean = false) {
    Text(
        text = text,
        style = androidx.compose.ui.text.TextStyle(
            fontSize = 16.sp,
            fontWeight = if (isBold) FontWeight.Bold else FontWeight.Normal,
            fontFamily = androidx.compose.ui.text.font.FontFamily.SansSerif,
            lineHeight = 24.sp
        ),
        modifier = Modifier.padding(vertical = 4.dp)
    )
}

@Composable
fun RuleBullet(text: String) {
    Row(Modifier.padding(vertical = 4.dp)) {
        Text("• ", fontSize = 18.sp)
        Text(
            text = text,
            style = androidx.compose.ui.text.TextStyle(
                fontSize = 16.sp,
                fontFamily = androidx.compose.ui.text.font.FontFamily.SansSerif,
                lineHeight = 24.sp
            )
        )
    }
}

@Composable
fun RuleExample(text: String) {
    Surface(
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
        shape = MaterialTheme.shapes.small,
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
    ) {
        Text(
            text = text,
            style = androidx.compose.ui.text.TextStyle(
                fontSize = 15.sp,
                fontStyle = FontStyle.Italic,
                fontFamily = androidx.compose.ui.text.font.FontFamily.SansSerif,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 22.sp
            ),
            modifier = Modifier.padding(12.dp)
        )
    }
}
