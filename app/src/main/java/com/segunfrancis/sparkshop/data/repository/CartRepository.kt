package com.segunfrancis.sparkshop.data.repository

import com.segunfrancis.sparkshop.data.local.CartDao
import com.segunfrancis.sparkshop.data.local.CartItemEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CartRepositoryImpl @Inject constructor(
    private val dao: CartDao,
    private val dispatcher: CoroutineDispatcher
) :
    CartRepository {
    override suspend fun addToCartOrIncrease(cartItem: CartItemEntity) {
        withContext(dispatcher) {
            val existingItem = dao.getCartItemById(cartItem.cartItemId)
            if (existingItem != null) {
                val updatedQuantity = (existingItem.quantity + 1).coerceAtMost(existingItem.stock)
                val updatedItem = existingItem.copy(quantity = updatedQuantity)
                dao.updateCartItem(updatedItem)
            } else {
                val itemToInsert = cartItem.copy(quantity = 1)
                dao.insertCartItem(itemToInsert)
            }
        }
    }

    override fun getAllCartItems(): Flow<List<CartItemEntity>> {
        return dao.getAllCartItems().flowOn(dispatcher)
    }

    override suspend fun decrementQuantity(cartItemId: Int) {
        withContext(dispatcher) {
            val item = dao.getCartItemById(cartItemId) ?: return@withContext
            if (item.quantity > 1) {
                dao.updateCartItem(item.copy(quantity = item.quantity - 1))
            } else {
                dao.deleteCartItem(item)
            }
        }
    }

    override suspend fun incrementQuantity(cartItemId: Int) {
        withContext(dispatcher) {
            val item = dao.getCartItemById(cartItemId) ?: return@withContext
            if (item.quantity < item.stock) {
                dao.updateCartItem(item.copy(quantity = item.quantity + 1))
            }
        }
    }

    override suspend fun clearCart() {
        withContext(dispatcher) {
            dao.clearCart()
        }
    }
}

interface CartRepository {
    suspend fun addToCartOrIncrease(cartItem: CartItemEntity)
    fun getAllCartItems(): Flow<List<CartItemEntity>>
    suspend fun decrementQuantity(cartItemId: Int)
    suspend fun incrementQuantity(cartItemId: Int)
    suspend fun clearCart()
}
