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
import androidx.compose.material.icons.filled.Warning
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

@Composable
fun AlertCard(
    modifier: Modifier = Modifier,
    title: String,
    text: String
) {
    Card(
        modifier = modifier.width(IntrinsicSize.Max),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.errorContainer,
            contentColor = MaterialTheme.colorScheme.onErrorContainer
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
                Icon(imageVector = Icons.Default.Warning, contentDescription = null)
            }
            Spacer(modifier = Modifier.height(5.dp))
            Text(text = text)
        }
    }
}

@Preview
@Composable
fun AlertCardLightPreview() {
    MediScanTheme {
        AlertCard(
            title = "Alerte de sécurité sanitaire",
            text = "Attention ce médicament fait l'objet d'un retrait ou d'une suspension d'autorisation ou d'utilisation pour des raisons de santé publique. Si vous prenez ce médicament, il vous est recommandé de prendre contact, dans les meilleurs délais, avec votre médecin ou avec votre pharmacien qui vous indiquera la conduite à tenir."
        )
    }
}

@Preview(uiMode = UI_MODE_NIGHT_YES)
@Composable
fun AlertCardDarkPreview() {
    MediScanTheme {
        AlertCard(
            title = "Alerte de sécurité sanitaire",
            text = "Attention ce médicament fait l'objet d'un retrait ou d'une suspension d'autorisation ou d'utilisation pour des raisons de santé publique. Si vous prenez ce médicament, il vous est recommandé de prendre contact, dans les meilleurs délais, avec votre médecin ou avec votre pharmacien qui vous indiquera la conduite à tenir."
        )
    }
}
