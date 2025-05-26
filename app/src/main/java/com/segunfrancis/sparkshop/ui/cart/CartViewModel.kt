package com.segunfrancis.sparkshop.ui.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.segunfrancis.sparkshop.data.repository.CartRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(private val repository: CartRepository) : ViewModel() {

    val cartItems = repository.getAllCartItems()
        .catch {  }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    fun increaseQuantity(id: Int) {
        viewModelScope.launch {
            repository.incrementQuantity(id)
        }
    }

    fun decreaseQuantity(id: Int) {
        viewModelScope.launch {
            repository.decrementQuantity(id)
        }
    }
}
