package com.segunfrancis.sparkshop.ui.cart

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.segunfrancis.sparkshop.R
import com.segunfrancis.sparkshop.data.local.CartItemEntity
import com.segunfrancis.sparkshop.ui.components.SparkShopToolbar
import com.segunfrancis.sparkshop.ui.theme.SparkShopTheme
import java.util.Locale

@Composable
fun CartScreen(onBack: () -> Unit = {}, onCheckout: () -> Unit = {}) {

    val viewModel = hiltViewModel<CartViewModel>()
    val cartItems by viewModel.cartItems.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
    ) {
        SparkShopToolbar(
            title = "Cart",
            navIcon = R.drawable.ic_chevron_left,
            onNavIconClick = onBack
        )
        if (cartItems.isEmpty()) {
            CartEmptyContent()
        } else {
            CartContent(
                cartItems = cartItems,
                total = cartItems.sumOf { it.price * it.quantity },
                onQuantityDecrease = { viewModel.decreaseQuantity(it) },
                onQuantityIncrease = { viewModel.increaseQuantity(it) },
                onCheckout = onCheckout
            )
        }
    }
}

@Composable
fun CartContent(
    cartItems: List<CartItemEntity>,
    total: Double,
    onQuantityIncrease: (Int) -> Unit,
    onQuantityDecrease: (Int) -> Unit,
    onCheckout: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
    ) {
        Spacer(Modifier.height(14.dp))
        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(items = cartItems, key = { it.cartItemId }) { item ->
                CartListItem(
                    item = item,
                    modifier = Modifier.animateItem(),
                    onQuantityIncrease = onQuantityIncrease,
                    onQuantityDecrease = onQuantityDecrease
                )
            }
        }
        HorizontalDivider(Modifier.padding(vertical = 10.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Total",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 24.dp)
            )
            Text(
                text = "$${String.format(Locale.getDefault(), "%.2f", total)}",
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
            shape = RoundedCornerShape(12.dp),
            enabled = total > 0.0
        ) {
            Text("Checkout", style = MaterialTheme.typography.labelLarge)
        }
    }
}

@Preview
@Composable
fun CartEmptyContent() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface),
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(R.drawable.il_vecteezy_empty_state_cart),
            contentDescription = null
        )
        Spacer(Modifier.height(24.dp))
        Text(
            text = "Your cart is empty",
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun CartListItem(
    item: CartItemEntity,
    modifier: Modifier = Modifier,
    onQuantityIncrease: (Int) -> Unit,
    onQuantityDecrease: (Int) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
    ) {
        AsyncImage(
            model = item.image,
            contentDescription = item.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(54.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(MaterialTheme.colorScheme.surfaceContainer)
        )
        Spacer(Modifier.width(10.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                item.title,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )
            Text("$${item.price}", style = MaterialTheme.typography.bodyMedium)
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .background(MaterialTheme.colorScheme.outline.copy(alpha = 0.4F))
        ) {
            IconButton(
                onClick = { onQuantityDecrease(item.cartItemId) },
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
                onClick = { onQuantityIncrease(item.cartItemId) },
                enabled = item.quantity < item.stock,
                modifier = Modifier.size(32.dp)
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Increase")
            }
        }
    }
}

@Preview
@Composable
fun CartContentPreview() {
    SparkShopTheme {
        CartContent(
            cartItems = listOf(),
            total = 0.0,
            onQuantityDecrease = {},
            onQuantityIncrease = {}
        )
    }
}
