package com.fontaipi.mediscan.feature.details.component

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.fontaipi.mediscan.ui.theme.MediScanTheme
import com.fontaipi.mediscan.ui.theme.customColorsPalette

@Composable
fun WarningCard(
    modifier: Modifier = Modifier,
    title: String,
    text: String
) {
    Card(
        modifier = modifier.width(IntrinsicSize.Max),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.customColorsPalette.warningContainer,
            contentColor = MaterialTheme.customColorsPalette.onWarningContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = title, style = MaterialTheme.typography.titleMedium)
                Icon(imageVector = Icons.Default.Info, contentDescription = null)
            }
            Spacer(modifier = Modifier.height(5.dp))
            Text(text = text)
        }
    }
}

@Preview
@Composable
fun WarningCardLightPreview() {
    MediScanTheme {
        WarningCard(
            title = "Avis de retrait du médicament",
            text = "Ce médicament n'est ou ne sera bientôt plus disponible sur le marché. Si vous prenez actuellement ce médicament, il vous est recommandé d'en parler avec votre médecin ou avec votre pharmacien qui pourra vous orienter vers un autre traitement."
        )
    }
}

@Preview(uiMode = UI_MODE_NIGHT_YES)
@Composable
fun WarningCardDarkPreview() {
    MediScanTheme {
        WarningCard(
            title = "Alerte de sécurité sanitaire",
            text = "Ce médicament n'est ou ne sera bientôt plus disponible sur le marché. Si vous prenez actuellement ce médicament, il vous est recommandé d'en parler avec votre médecin ou avec votre pharmacien qui pourra vous orienter vers un autre traitement."
        )
    }
}
