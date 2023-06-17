package com.fontaipi.mediscan.feature.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fontaipi.mediscan.domain.DrugDetails
import com.fontaipi.mediscan.domain.GetDrugDetails
import com.fontaipi.mediscan.utils.Result
import com.fontaipi.mediscan.utils.asResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    getDrugDetails: GetDrugDetails
) : ViewModel() {
    private val detailsArgs: DetailArgs = DetailArgs(savedStateHandle)

    private val cisCode = detailsArgs.cisCode

    val detailsState: StateFlow<DetailsUiState> = getDrugDetails(cisCode).asResult().map { result ->
        when (result) {
            is Result.Success -> {
                if (result.data != null) {
                    DetailsUiState.Success(result.data)
                } else {
                    DetailsUiState.Error("Code CIP : $cisCode inconnu")
                }
            }

            is Result.Error -> DetailsUiState.Error("Erreur lors du chargement des données.")
            Result.Loading -> DetailsUiState.Loading
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = DetailsUiState.Loading
    )
}

sealed interface DetailsUiState {
    data class Success(val drugDetails: DrugDetails) : DetailsUiState
    data class Error(val message: String) : DetailsUiState
    object Loading : DetailsUiState
}
