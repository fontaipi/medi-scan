package com.fontaipi.mediscan.feature.scanner

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fontaipi.mediscan.repository.PackagingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScannerViewModel @Inject constructor(
    private val packagingRepository: PackagingRepository
) : ViewModel() {
    val cisCode = MutableStateFlow<String?>(null)

    fun searchCisCode(cipCode: String) {
        viewModelScope.launch {
            val tmp = packagingRepository.getCisCodeByCipCode(cipCode)
            cisCode.value = tmp
        }
    }

    fun resetCisCode() {
        cisCode.value = null
    }
}
