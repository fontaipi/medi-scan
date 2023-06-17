package com.fontaipi.mediscan.feature.details.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.fontaipi.mediscan.ui.theme.MediScanTheme

@Composable
fun Pill(
    modifier: Modifier = Modifier,
    text: String,
    color: Color
) {
    Surface(
        color = color,
        shape = RoundedCornerShape(20.dp)
    ) {
        Box(
            modifier = modifier.padding(10.dp)
        ) {
            Text(text = text)
        }
    }
}

@Preview
@Composable
fun PillPreview() {
    MediScanTheme {
        Pill(text = "Comprimé", color = MaterialTheme.colorScheme.tertiaryContainer)
    }
}
