package com.segunfrancis.sparkshop.ui.home

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.segunfrancis.sparkshop.R
import com.segunfrancis.sparkshop.data.remote.Product
import com.segunfrancis.sparkshop.ui.components.SparkShopToolbar
import com.segunfrancis.sparkshop.ui.home.HomeViewModel.HomeUi
import com.segunfrancis.sparkshop.utils.toTitleCase
import java.text.NumberFormat
import java.util.Locale

@Composable
fun HomeScreen(title: String?, onCartClick: () -> Unit, onProductClick: (Product) -> Unit) {
    val context = LocalContext.current
    val viewModel = hiltViewModel<HomeViewModel>()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val cartItemCount by viewModel.cartItemCount.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    HomeContent(
        title = title,
        uiState = uiState,
        cartItemCount = cartItemCount,
        onCartClick = onCartClick,
        onProductClick = onProductClick,
        isLoading = isLoading,
        onFilterProduct = {
            viewModel.filterByCategory(it)
        })
    LaunchedEffect(Unit) {
        viewModel.action.collect {
            when (it) {
                is HomeViewModel.HomeAction.Error -> Toast.makeText(
                    context,
                    it.errorMessage,
                    Toast.LENGTH_LONG
                )
                    .show()
            }
        }
    }
}

@Composable
@Preview
fun HomeContent(
    title: String? = null,
    uiState: HomeUi = HomeUi(),
    cartItemCount: Int = 1,
    onCartClick: () -> Unit = {},
    isLoading: Boolean = true,
    onProductClick: (Product) -> Unit = {},
    onFilterProduct: (String) -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        val greeting = title?.let { name ->
            "Hello, $name"
        } ?: stringResource(R.string.app_name)
        SparkShopToolbar(
            title = greeting,
            actionIcon = R.drawable.ic_shopping_cart,
            onActionIconClick = { onCartClick() },
            cartItemCount = cartItemCount
        )
        if (isLoading) {
            LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
        }
        Spacer(Modifier.height(24.dp))
        LazyRow(
            contentPadding = PaddingValues(horizontal = 24.dp, vertical = 4.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(uiState.categories.toList()) { category ->
                val selected = uiState.selectedCategory.equals(category, ignoreCase = true)
                FilterChip(
                    selected = selected,
                    onClick = {
                        onFilterProduct(category)
                    },
                    label = {
                        Text(
                            text = category.toTitleCase(),
                            color = if (selected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.onPrimaryContainer,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        containerColor = MaterialTheme.colorScheme.surfaceContainer,
                        selectedContainerColor = MaterialTheme.colorScheme.onSurface
                    ),
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .height(34.dp)
                )
            }
        }
        LazyColumn(
            contentPadding = PaddingValues(bottom = 20.dp, start = 24.dp, end = 24.dp, top = 24.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(items = uiState.products, key = { it.id }) { product ->
                ProductListItem(
                    product = product,
                    modifier = Modifier.animateItem(),
                    onClick = { onProductClick(product) }
                )
            }
        }
    }
}

@Composable
fun ProductListItem(product: Product, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(74.dp)
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        border = _root_ide_package_.androidx.compose.foundation.BorderStroke(
            width = 0.5.dp,
            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.4F)
        )
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            AsyncImage(
                model = product.image,
                contentDescription = product.title,
                contentScale = ContentScale.Crop,
                error = painterResource(android.R.drawable.ic_menu_report_image),
                placeholder = painterResource(android.R.drawable.ic_menu_report_image),
                modifier = Modifier
                    .size(74.dp)
                    .background(MaterialTheme.colorScheme.background)
            )
            Spacer(Modifier.width(10.dp))
            Column {
                Text(
                    product.title,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "$${
                        NumberFormat.getInstance(Locale.getDefault()).format(product.price)
                    }",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}
