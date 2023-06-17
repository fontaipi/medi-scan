package com.fontaipi.mediscan.feature.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import androidx.paging.map
import com.fontaipi.mediscan.database.dao.DrugDao
import com.fontaipi.mediscan.domain.DrugSearch
import com.fontaipi.mediscan.repository.DrugRepository
import com.fontaipi.mediscan.utils.Result
import com.fontaipi.mediscan.utils.asResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class SearchViewModel @Inject constructor(
    drugRepository: DrugRepository,
    drugDao: DrugDao
) : ViewModel() {
    val query = MutableStateFlow("")
    val cisCode = MutableStateFlow("")

    val pager = cisCode.flatMapLatest { cisCode ->
        Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            )
        ) {
            drugDao.pagingSource(cisCode)
        }.flow.map { pagingData -> pagingData.map { DrugSearch(it) } }
            .cachedIn(viewModelScope)
    }

    val quickSearchUiState: StateFlow<QuickSearchUiState> =
        query.flatMapLatest { code ->
            drugRepository.searchByName(code, 10).asResult().map { result ->
                when (result) {
                    is Result.Success -> QuickSearchUiState.Success(result.data.map { DrugSearch(it) })
                    is Result.Error -> QuickSearchUiState.Error("Erreur lors de la recherche")
                    Result.Loading -> QuickSearchUiState.Loading
                }
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = QuickSearchUiState.Loading
        )

    val searchUiState: StateFlow<SearchUiState> =
        cisCode.flatMapLatest { code ->
            drugRepository.searchByName(code, 10).asResult().map { result ->
                when (result) {
                    is Result.Success -> SearchUiState.Success(result.data.map { DrugSearch(it) })
                    is Result.Error -> SearchUiState.Error("Erreur lors de la recherche")
                    Result.Loading -> SearchUiState.Loading
                }
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = SearchUiState.Loading
        )

    fun updateQuery(str: String) {
        query.value = str
    }

    fun search() {
        cisCode.value = query.value
    }
}

data class QuickSearch(
    val results: List<DrugSearch>
)

sealed interface SearchUiState {
    data class Success(val results: List<DrugSearch>) : SearchUiState
    data class Error(val message: String) : SearchUiState
    object Loading : SearchUiState
}

sealed interface QuickSearchUiState {
    data class Success(val results: List<DrugSearch>) : QuickSearchUiState
    data class Error(val message: String) : QuickSearchUiState
    object Loading : QuickSearchUiState
}
