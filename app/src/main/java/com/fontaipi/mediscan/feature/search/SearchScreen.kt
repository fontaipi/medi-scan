package com.fontaipi.mediscan.feature.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import com.fontaipi.mediscan.domain.DrugSearch
import kotlinx.coroutines.ExperimentalCoroutinesApi

@OptIn(ExperimentalCoroutinesApi::class)
@Composable
fun SearchRoute(
    navigateToDrugDetails: (String) -> Unit,
    viewModel: SearchViewModel = hiltViewModel()
) {
    val query by viewModel.query.collectAsStateWithLifecycle()
    val searchUiState by viewModel.searchUiState.collectAsStateWithLifecycle()
    val quickSearchUiState by viewModel.quickSearchUiState.collectAsStateWithLifecycle()
    val results = viewModel.pager.collectAsLazyPagingItems()
    SearchScreen(
        navigateToDrugDetails = navigateToDrugDetails,
        query = query,
        searchUiState = searchUiState,
        quickSearchUiState = quickSearchUiState,
        results = results,
        updateQuery = viewModel::updateQuery,
        search = viewModel::search
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    navigateToDrugDetails: (String) -> Unit,
    query: String,
    searchUiState: SearchUiState,
    quickSearchUiState: QuickSearchUiState,
    results: LazyPagingItems<DrugSearch>,
    updateQuery: (String) -> Unit,
    search: () -> Unit
) {
    var active by rememberSaveable { mutableStateOf(false) }

    Column {
        SearchBar(
            query = query,
            onQueryChange = updateQuery,
            onSearch = {
                active = false
                search()
            },
            active = active,
            onActiveChange = { active = it },
            placeholder = { Text("Nom du médicament") },
            leadingIcon = {
                val icon = if (active) Icons.Default.ArrowBack else Icons.Default.Search
                IconButton(onClick = { active = !active }) {
                    Icon(imageVector = icon, contentDescription = null)
                }
            },
            trailingIcon = {
                if (active) {
                    IconButton(onClick = { updateQuery("") }) {
                        Icon(imageVector = Icons.Default.Clear, contentDescription = null)
                    }
                }
            }
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                item {
                    Text(text = "Recherches récentes")
                }
                items(1) { idx ->
                    val resultText = "Suggestion $idx"
                    ListItem(
                        headlineContent = { Text(resultText) },
                        supportingContent = { Text("Additional info") },
                        leadingContent = { Icon(Icons.Filled.History, contentDescription = null) },
                        modifier = Modifier.clickable {
                            updateQuery(resultText)
                            search()
                            active = false
                        }
                    )
                }
                item {
                    Text(text = "Résultats rapides")
                }

                if (quickSearchUiState is QuickSearchUiState.Success) {
                    items(quickSearchUiState.results) {
                        ListItem(
                            headlineContent = { Text(it.name) },
                            supportingContent = { Text("Additional info") },
                            leadingContent = {
                                Icon(
                                    Icons.Filled.History,
                                    contentDescription = null
                                )
                            },
                            modifier = Modifier.clickable {
                                navigateToDrugDetails(it.cisCode)
                            }
                        )
                    }
                }
            }
        }
        LazyColumn {
            items(
                count = results.itemCount,
                key = results.itemKey(),
                contentType = results.itemContentType()
            ) { index ->
                val item = results[index]
                if (item != null) {
                    ListItem(
                        headlineContent = { Text(item.name) },
                        supportingContent = { Text("Additional info") },
                        leadingContent = {
                            Icon(
                                Icons.Filled.MedicalServices,
                                contentDescription = null
                            )
                        },
                        modifier = Modifier.clickable {
                            navigateToDrugDetails(item.cisCode)
                        }
                    )
                }
            }
        }
    }

//    when (searchUiState) {
//        is SearchUiState.Success -> {
//            LazyColumn(
//                contentPadding = PaddingValues(
//                    start = 16.dp,
//                    top = 72.dp,
//                    end = 16.dp,
//                    bottom = 16.dp
//                ),
//                verticalArrangement = Arrangement.spacedBy(8.dp)
//            ) {
//                items(searchUiState.results) {
//                    ListItem(
//                        headlineContent = { Text(it.name) },
//                        supportingContent = { Text("Additional info") },
//                        leadingContent = {
//                            Icon(
//                                Icons.Filled.MedicalServices,
//                                contentDescription = null
//                            )
//                        },
//                        modifier = Modifier.clickable {
//                            navigateToDrugDetails(it.cisCode)
//                        }
//                    )
//                }
//            }
//        }
//
//        is SearchUiState.Error -> {
//            Box {
//                Text(text = searchUiState.message)
//            }
//        }
//
//        SearchUiState.Loading -> {
//            Box {
//                CircularProgressIndicator()
//            }
//        }
//    }
}
