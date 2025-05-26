package com.segunfrancis.sparkshop.ui.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.segunfrancis.sparkshop.data.remote.Product
import com.segunfrancis.sparkshop.data.repository.CartRepository
import com.segunfrancis.sparkshop.data.repository.ProductRepository
import com.segunfrancis.sparkshop.data.repository.toDimension
import com.segunfrancis.sparkshop.data.repository.toMeta
import com.segunfrancis.sparkshop.data.repository.toReview
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
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

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        throwable.printStackTrace()
    }

    init {
        fetchProducts()
    }

    private var products: List<Product> = emptyList()

    val cartItemCount = cartRepository.getAllCartItems()
        .catch {  }
        .map { cartItem -> cartItem.size }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), 0)

    fun fetchProducts() {
        viewModelScope.launch(exceptionHandler) {
            productRepository.getAllProducts()
                .catch { errorResponse -> _uiState.update { it.copy(error = errorResponse.localizedMessage) } }
                .collect { successResponse ->
                    Log.d("fetchProducts", "Response: $successResponse")
                    val products = successResponse.mapIndexed { index, response ->
                        Product(
                            id = response.product.id,
                            title = response.product.title,
                            description = response.product.description,
                            price = response.product.price,
                            weight = response.product.weight,
                            availabilityStatus = response.product.availabilityStatus,
                            stock = response.product.stock,
                            thumbnail = response.product.thumbnail,
                            sku = response.product.sku,
                            shippingInformation = response.product.shippingInformation,
                            tags = response.product.tags,
                            images = response.product.images,
                            minimumOrderQuantity = response.product.minimumOrderQuantity,
                            rating = response.product.rating,
                            category = response.product.category,
                            warrantyInformation = response.product.warrantyInformation,
                            discountPercentage = response.product.discountPercentage,
                            meta = response.meta.toMeta(),
                            reviews = response.review.map { review -> review.toReview() },
                            dimensions = response.dimensions.toDimension(),
                        )
                    }
                    this@HomeViewModel.products = products
                    _uiState.update {
                        val categories = mutableSetOf("All")
                        categories.addAll(products.map { product -> product.category }
                            .toMutableSet())
                        it.copy(
                            products = products,
                            categories = categories
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

    data class HomeUi(
        val isLoading: Boolean = false,
        val products: List<Product> = emptyList(),
        val categories: Set<String> = emptySet(),
        val error: String? = null,
        val selectedCategory: String = "All"
    )
}
