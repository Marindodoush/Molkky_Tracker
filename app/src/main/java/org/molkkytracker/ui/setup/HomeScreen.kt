package org.molkkytracker.ui.setup

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import org.molkkytracker.R

/** Forme inclinée pour les boutons de gauche (coupé en haut à droite) */
private val LeftAngledShape = GenericShape { size, _ ->
    moveTo(0f, 0f)
    lineTo(size.width * 0.70f, 0f)
    lineTo(size.width, size.height)
    lineTo(0f, size.height)
    close()
}

/** Forme inclinée pour les boutons de droite (coupé en haut à gauche) */
private val RightAngledShape = GenericShape { size, _ ->
    moveTo(size.width * 0.30f, 0f)
    lineTo(size.width, 0f)
    lineTo(size.width, size.height)
    lineTo(0f, size.height)
    close()
}

@Composable
fun HomeScreen(
    onStartGame: () -> Unit,
    onManageAlias: () -> Unit,
    onShowRules: () -> Unit,
    onShowCredits: () -> Unit,
    onShowSettings: () -> Unit,
    onShowHelp: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        // Fond d'écran réel (bg_home.png)
        Image(
            painter = painterResource(id = R.drawable.bg_home),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        
        // Contenu par dessus le fond
        Column(
            modifier = Modifier.fillMaxSize().padding(top = 280.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Première ligne de boutons
            Row(
                modifier = Modifier.width(320.dp),
                horizontalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                HomeMenuButton(
                    text = stringResource(R.string.menu_rules),
                    shape = LeftAngledShape,
                    modifier = Modifier.weight(1f),
                    textOffset = (-8).dp, // Décaler vers la gauche
                    onClick = onShowRules
                )
                HomeMenuButton(
                    text = stringResource(R.string.menu_start),
                    shape = RightAngledShape,
                    modifier = Modifier.weight(1f),
                    backgroundColor = Color.White,
                    textColor = Color.Black,
                    textOffset = 8.dp, // Décaler vers la droite
                    onClick = onStartGame
                )
            }

            Spacer(Modifier.height(30.dp))

            // Deuxième ligne de boutons
            Row(
                modifier = Modifier.width(320.dp),
                horizontalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                HomeMenuButton(
                    text = stringResource(R.string.menu_alias),
                    shape = LeftAngledShape,
                    modifier = Modifier.weight(1f),
                    textOffset = (-8).dp,
                    onClick = onManageAlias
                )
                HomeMenuButton(
                    text = stringResource(R.string.menu_credits),
                    shape = RightAngledShape,
                    modifier = Modifier.weight(1f),
                    textOffset = 8.dp,
                    onClick = onShowCredits
                )
            }
        }

        // Ampoule pour l'aide (Bas Gauche)
        IconButton(
            onClick = onShowHelp,
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(24.dp)
                .size(48.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Info, // Changé ici
                contentDescription = "Help",
                tint = Color.Gray,
                modifier = Modifier.fillMaxSize()
            )
        }

        // Roue crantée pour les réglages (Bas Droite)
        IconButton(
            onClick = onShowSettings,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(24.dp)
                .size(48.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Settings,
                contentDescription = "Settings",
                tint = Color.Gray,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@Composable
fun HomeMenuButton(
    text: String,
    shape: GenericShape,
    modifier: Modifier = Modifier,
    backgroundColor: Color = Color.White,
    textColor: Color = Color.Black,
    textOffset: androidx.compose.ui.unit.Dp = 0.dp,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .height(54.dp)
            .clip(shape)
            .background(backgroundColor)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.titleMedium,
            color = textColor,
            modifier = Modifier.offset(x = textOffset)
        )
    }
}
