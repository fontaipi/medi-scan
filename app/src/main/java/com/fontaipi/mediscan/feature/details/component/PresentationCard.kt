package com.fontaipi.mediscan.feature.details.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PlainTooltipBox
import androidx.compose.material3.Text
import androidx.compose.material3.rememberPlainTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.fontaipi.mediscan.domain.Presentation
import com.fontaipi.mediscan.domain.samplePresentation
import com.fontaipi.mediscan.ui.theme.MediScanTheme

@Composable
fun InlineLabelText(
    modifier: Modifier = Modifier,
    label: String,
    text: String
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = "$label :", style = MaterialTheme.typography.labelLarge)
        Spacer(modifier = Modifier.width(4.dp))
        Text(text = text)
    }
}

@Composable
fun InlineIconText(
    icon: ImageVector,
    text: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(imageVector = icon, contentDescription = null)
        Spacer(modifier = Modifier.width(4.dp))
        Text(text = text)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PresentationCard(
    modifier: Modifier = Modifier,
    presentation: Presentation
) {
    val tooltipState = rememberPlainTooltipState()
    val scope = rememberCoroutineScope()
    Card(modifier) {
        Column(
            modifier = Modifier
                .width(320.dp)
                .padding(5.dp)
        ) {
            Text(text = presentation.label, style = MaterialTheme.typography.titleMedium)
            InlineLabelText(label = "Code CIP", text = presentation.cipCode)
            Spacer(modifier = Modifier.height(5.dp))
            Text(
                text = "Déclaration de commercialisation :",
                style = MaterialTheme.typography.labelLarge
            )
            InlineIconText(
                icon = Icons.Default.CalendarMonth,
                text = presentation.marketingDeclarationDate
            )
            Spacer(modifier = Modifier.height(5.dp))
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.End
            ) {
                InlineLabelText(label = "Taux de remboursement", text = presentation.refundRate)
                if (presentation.priceExcludingDispensingFee != null) {
                    PlainTooltipBox(tooltip = { Text(text = "oskfpskdfposdk") }) {
                        InlineLabelText(
                            modifier = Modifier.tooltipTrigger(),
                            label = "Prix",
                            text = "${presentation.priceExcludingDispensingFee}€"
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun PresentationCardPreview() {
    MediScanTheme {
        PresentationCard(presentation = samplePresentation)
    }
}
