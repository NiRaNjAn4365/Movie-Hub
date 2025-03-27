package com.example.moviehub.screens.details

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
class CategoriesViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    private val _genreId = MutableStateFlow<Int?>(null)
    val genreId = _genreId.asStateFlow()

    fun setGenreId(id: Int) {
        _genreId.value = id
    }

    val getCategoryMovies: StateFlow<PagingData<MovieResults>> = genreId
        .filterNotNull()
        .filter { it > 0 }
        .flatMapLatest { id ->
            repository.getMoviesByCateogeries(id).cachedIn(viewModelScope)
        }
        .stateIn(viewModelScope, SharingStarted.Lazily, PagingData.empty())
}