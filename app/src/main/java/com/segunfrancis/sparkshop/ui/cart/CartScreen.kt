package com.segunfrancis.sparkshop.ui.cart

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import coil3.compose.AsyncImage
import com.segunfrancis.sparkshop.R
import com.segunfrancis.sparkshop.data.remote.Product
import com.segunfrancis.sparkshop.ui.components.SparkShopToolbar

data class CartItem(val product: Product, val quantity: Int)

@Composable
fun CartScreen(onBack: () -> Unit = {}, onCheckout: () -> Unit = {}) {

}

@Composable
fun CartContent(
    cartItems: List<CartItem>,
    onBack: () -> Unit = {},
    onQuantityChange: (CartItem, Int) -> Unit = { _, _ -> },
    onCheckout: () -> Unit = {}
) {
    val total = cartItems.sumOf { it.product.price * it.quantity }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        SparkShopToolbar(
            title = "Cart",
            navIcon = R.drawable.ic_chevron_left,
            onNavIconClick = onBack
        )
        Spacer(Modifier.height(14.dp))
        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(cartItems) { item ->
                CartListItem(item, onQuantityChange)
            }
        }
        HorizontalDivider(Modifier.padding(vertical = 10.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                "Total",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 24.dp)
            )
            Text(
                "$${total}",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(end = 24.dp)
            )
        }
        Spacer(Modifier.height(16.dp))
        Button(
            onClick = onCheckout,
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Checkout", style = MaterialTheme.typography.labelLarge)
        }
    }
}

@Composable
fun CartListItem(item: CartItem, onQuantityChange: (CartItem, Int) -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
    ) {
        AsyncImage(
            model = item.product.images.first(),
            contentDescription = item.product.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(54.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(Color.LightGray)
        )
        Spacer(Modifier.width(10.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                item.product.title,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )
            Text("$${item.product.price}", style = MaterialTheme.typography.bodyMedium)
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .background(Color(0xFFF4F4F4))
        ) {
            IconButton(
                onClick = { onQuantityChange(item, item.quantity - 1) },
                enabled = item.quantity > 1,
                modifier = Modifier.size(32.dp)
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_remove),
                    contentDescription = "Decrease"
                )
            }
            Text(
                "${item.quantity}",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(horizontal = 4.dp)
            )
            IconButton(
                onClick = { onQuantityChange(item, item.quantity + 1) },
                modifier = Modifier.size(32.dp)
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Increase")
            }
        }
    }
}