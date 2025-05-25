package com.segunfrancis.sparkshop.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.segunfrancis.sparkshop.data.remote.Product
import com.segunfrancis.sparkshop.data.repository.SparkShopRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: SparkShopRepository) : ViewModel() {

    private val _uiState: MutableStateFlow<HomeUi> = MutableStateFlow(HomeUi())
    val uiState: StateFlow<HomeUi> = _uiState.asStateFlow()

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->

    }

    init {
        fetchProducts()
    }

    fun fetchProducts() {
        viewModelScope.launch(exceptionHandler) {
            _uiState.update { it.copy(isLoading = true) }
            repository.getAllProducts()
                .onFailure { errorResponse -> _uiState.update { it.copy(error = errorResponse.localizedMessage) } }
                .onSuccess { successResponse ->
                    _uiState.update {
                        val categories = mutableSetOf("All")
                        categories.addAll(successResponse.map { product -> product.category }.toMutableSet())
                        it.copy(
                            products = successResponse,
                            categories = categories
                        )
                    }
                }
            _uiState.update { it.copy(isLoading = false) }
        }
    }

    data class HomeUi(
        val isLoading: Boolean = false,
        val products: List<Product> = emptyList(),
        val categories: Set<String> = emptySet(),
        val error: String? = null
    )
}
