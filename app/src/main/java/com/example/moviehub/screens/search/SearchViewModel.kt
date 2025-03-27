package com.example.moviehub.screens.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.moviehub.data.repository.Repository
import com.example.moviehub.models.MovieResults
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    private var _searchQuery =   MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    val searchedMovies: StateFlow<PagingData<MovieResults>> = searchQuery
        .debounce(300)
        .filter { it.isNotBlank() }
        .flatMapLatest { query ->
            repository.searchMovies(query).cachedIn(viewModelScope)
        }
        .stateIn(viewModelScope, SharingStarted.Lazily, PagingData.empty())

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }
}