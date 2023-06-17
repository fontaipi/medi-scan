package com.fontaipi.mediscan.feature.details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.fontaipi.mediscan.database.entity.BdmStatus
import com.fontaipi.mediscan.domain.DrugDetails
import com.fontaipi.mediscan.domain.samplePresentation
import com.fontaipi.mediscan.feature.details.component.AlertCard
import com.fontaipi.mediscan.feature.details.component.InlineLabelText
import com.fontaipi.mediscan.feature.details.component.PresentationCard
import com.fontaipi.mediscan.feature.details.component.WarningCard
import com.fontaipi.mediscan.ui.theme.MediScanTheme

@Composable
fun DetailsRoute(
    viewModel: DetailsViewModel = hiltViewModel()
) {
    val detailsUiState by viewModel.detailsState.collectAsStateWithLifecycle()
    DetailsScreen(detailsUiState = detailsUiState)
}

@Composable
fun DetailsScreen(
    detailsUiState: DetailsUiState
) {
    when (detailsUiState) {
        is DetailsUiState.Success -> {
            val scrollState = rememberScrollState()
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(scrollState)
            ) {
                Text(
                    text = detailsUiState.drugDetails.name.split(",")[0],
                    style = MaterialTheme.typography.headlineMedium
                )
                Text(
                    text = detailsUiState.drugDetails.name.split(",")[1].trimStart()
                        .replaceFirstChar(Char::titlecase),
                    style = MaterialTheme.typography.titleMedium
                )

                InlineLabelText(label = "Code CIS", text = detailsUiState.drugDetails.cisCode)
                when (detailsUiState.drugDetails.bdmStatus) {
                    BdmStatus.ALERT -> {
                        AlertCard(
                            modifier = Modifier.fillMaxWidth(),
                            title = "Alerte de sécurité sanitaire",
                            text = "Attention ce médicament fait l'objet d'un retrait ou d'une suspension d'autorisation ou d'utilisation pour des raisons de santé publique. Si vous prenez ce médicament, il vous est recommandé de prendre contact, dans les meilleurs délais, avec votre médecin ou avec votre pharmacien qui vous indiquera la conduite à tenir."
                        )
                    }

                    BdmStatus.AVAILABILITY_WARNING -> {
                        WarningCard(
                            modifier = Modifier.fillMaxWidth(),
                            title = "Alerte de sécurité sanitaire",
                            text = "Ce médicament n'est ou ne sera bientôt plus disponible sur le marché. Si vous prenez actuellement ce médicament, il vous est recommandé d'en parler avec votre médecin ou avec votre pharmacien qui pourra vous orienter vers un autre traitement."
                        )
                    }

                    null -> {}
                }
                Spacer(modifier = Modifier.height(10.dp))
                Text(text = "Présentation(s)", style = MaterialTheme.typography.headlineSmall)
                LazyRow(
                    // contentPadding = PaddingValues(10.dp),
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    items(detailsUiState.drugDetails.presentations) {
                        PresentationCard(presentation = it)
                    }
                }
            }
        }

        is DetailsUiState.Error -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(detailsUiState.message)
            }
        }

        DetailsUiState.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
    }
}

@Preview
@Composable
fun DetailsScreenPreview() {
    MediScanTheme {
        DetailsScreen(
            detailsUiState = DetailsUiState.Success(
                DrugDetails(
                    cisCode = "",
                    name = "DOLIPRANE 1000 mg, comprimés",
                    bdmStatus = BdmStatus.ALERT,
                    presentations = listOf(samplePresentation, samplePresentation)
                )
            )
        )
    }
}
