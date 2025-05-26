package com.segunfrancis.sparkshop.ui.details

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.segunfrancis.sparkshop.data.local.CartItemEntity
import com.segunfrancis.sparkshop.data.remote.Product
import com.segunfrancis.sparkshop.data.repository.CartRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel(assistedFactory = DetailsViewModel.Factory::class)
class DetailsViewModel @AssistedInject constructor(
    private val cartRepository: CartRepository,
    @Assisted val product: Product
) : ViewModel() {

    private val _action: MutableSharedFlow<DetailsAction> =
        MutableSharedFlow(onBufferOverflow = BufferOverflow.DROP_LATEST, extraBufferCapacity = 1)
    val action: SharedFlow<DetailsAction> = _action.asSharedFlow()

    fun addToCart() {
        viewModelScope.launch {
            product.run {
                cartRepository.addToCartOrIncrease(
                    CartItemEntity(
                        cartItemId = id,
                        price = price,
                        title = title,
                        thumbnail = thumbnail,
                        stock = stock,
                        quantity = 1
                    )
                )
                _action.tryEmit(DetailsAction.AddedToCart)
            }
        }
    }

    sealed interface DetailsAction {
        data object AddedToCart : DetailsAction
    }

    @AssistedFactory interface Factory {
        fun create(product: Product): DetailsViewModel
    }
}
