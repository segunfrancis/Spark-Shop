package com.segunfrancis.sparkshop.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.segunfrancis.sparkshop.data.remote.Product
import com.segunfrancis.sparkshop.data.repository.CartRepository
import com.segunfrancis.sparkshop.data.repository.ProductRepository
import com.segunfrancis.sparkshop.data.repository.toDimensions
import com.segunfrancis.sparkshop.data.repository.toRating
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val productRepository: ProductRepository,
    cartRepository: CartRepository
) : ViewModel() {

    private val _uiState: MutableStateFlow<HomeUi> = MutableStateFlow(HomeUi())
    val uiState: StateFlow<HomeUi> = _uiState.asStateFlow()

    private val _action: MutableSharedFlow<HomeAction> =
        MutableSharedFlow(onBufferOverflow = BufferOverflow.DROP_OLDEST, extraBufferCapacity = 1)
    val action: SharedFlow<HomeAction> = _action.asSharedFlow()

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        throwable.printStackTrace()
        _action.tryEmit(HomeAction.Error(throwable.localizedMessage))
    }

    init {
        fetchProducts()
    }

    private var products: List<Product> = emptyList()

    val cartItemCount = cartRepository.getAllCartItems()
        .catch { }
        .map { cartItem -> cartItem.size }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), 0)

    fun fetchProducts() {
        viewModelScope.launch(exceptionHandler) {
            productRepository.getAllProducts()
                .catch { errorResponse ->
                    _uiState.update { it.copy(error = errorResponse.localizedMessage) }
                    _action.tryEmit(HomeAction.Error(errorResponse.localizedMessage))
                }
                .collect { successResponse ->
                    val products = successResponse.mapIndexed { index, response ->
                        Product(
                            id = response.product.id,
                            title = response.product.title,
                            description = response.product.description,
                            price = response.product.price,
                            stock = response.product.stock,
                            image = response.product.image,
                            category = response.product.category,
                            shippingInformation = response.product.shippingInformation,
                            returnPolicy = response.product.returnPolicy,
                            percentageOff = response.product.percentageOff,
                            rating = response.rating.toRating(),
                            dimensions = response.dimensions.toDimensions()
                        )
                    }
                    this@HomeViewModel.products = products
                    _uiState.update {
                        val categories = mutableSetOf("All")
                        val otherCategories =
                            products.map { product -> product.category }.toMutableSet()
                        categories.addAll(otherCategories)
                        it.copy(
                            products = products,
                            categories = if (otherCategories.isNotEmpty()) {
                                categories
                            } else emptySet()
                        )
                    }
                }
        }
    }

    fun filterByCategory(category: String) {
        _uiState.update { state ->
            state.copy(
                products = if (category.equals("All", ignoreCase = true)) products else {
                    products.filter { it.category.equals(category, ignoreCase = true) }
                },
                selectedCategory = category
            )
        }
    }

    val isLoading: StateFlow<Boolean> = uiState.map {
        it.products.isEmpty() && it.error == null
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), true)

    data class HomeUi(
        val products: List<Product> = emptyList(),
        val categories: Set<String> = emptySet(),
        val selectedCategory: String = "All",
        val error: String? = null
    )

    sealed interface HomeAction {
        data class Error(val errorMessage: String?) : HomeAction
    }
}
