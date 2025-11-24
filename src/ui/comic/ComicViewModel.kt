package com.shortcut.myapplication.ui.comic

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shortcut.myapplication.data.ComicRepository
import com.shortcut.myapplication.data.Result
import com.shortcut.myapplication.domain.Comic
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ComicUiState(
    val comic: Comic? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class ComicViewModel @Inject constructor(
    private val repository: ComicRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ComicUiState())
    val uiState: StateFlow<ComicUiState> = _uiState.asStateFlow()

    init {
        loadLatestComic()
    }

    fun loadLatestComic() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            when (val result = repository.getLatestComic()) {
                is Result.Success -> {
                    _uiState.value = _uiState.value.copy(
                        comic = result.data,
                        isLoading = false
                    )
                }
                is Result.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = result.message
                    )
                }
                Result.Loading -> {}
            }
        }
    }

    fun loadComic(num: Int) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            when (val result = repository.getComic(num)) {
                is Result.Success -> {
                    _uiState.value = _uiState.value.copy(
                        comic = result.data,
                        isLoading = false
                    )
                }
                is Result.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = result.message
                    )
                }
                Result.Loading -> {}
            }
        }
    }

    fun loadPreviousComic() {
        val current = _uiState.value.comic?.num ?: return
        if (current > 1) {
            loadComic(current - 1)
        }
    }

    fun loadNextComic() {
        viewModelScope.launch {
            val current = _uiState.value.comic?.num ?: return@launch
            val max = repository.getMaxComicNumber() ?: current
            if (current < max) {
                loadComic(current + 1)
            }
        }
    }

    fun loadRandomComic() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            when (val result = repository.getRandomComic()) {
                is Result.Success -> {
                    _uiState.value = _uiState.value.copy(
                        comic = result.data,
                        isLoading = false
                    )
                }
                is Result.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = result.message
                    )
                }
                Result.Loading -> {}
            }
        }
    }

    fun toggleFavorite() {
        viewModelScope.launch {
            val comic = _uiState.value.comic ?: return@launch
            repository.toggleFavorite(comic.num)
            _uiState.value = _uiState.value.copy(
                comic = comic.copy(isFavorite = !comic.isFavorite)
            )
        }
    }

    fun searchComic(comicNumber: String) {
        val num = comicNumber.toIntOrNull()
        if (num != null && num > 0) {
            loadComic(num)
        } else {
            _uiState.value = _uiState.value.copy(error = "Invalid comic number")
        }
    }
}