package com.segunfrancis.sparkshop.ui.details

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.AsyncImage
import com.segunfrancis.sparkshop.R
import com.segunfrancis.sparkshop.data.remote.Product
import com.segunfrancis.sparkshop.ui.components.PagerIndicator
import com.segunfrancis.sparkshop.ui.components.SparkShopToolbar
import com.segunfrancis.sparkshop.ui.theme.SparkShopTheme
import com.segunfrancis.sparkshop.utils.dummyProduct
import java.text.NumberFormat
import java.util.Locale

@Composable
fun DetailsScreen(
    product: Product,
    onBack: () -> Unit = {}
) {
    val context = LocalContext.current
    val viewModel = hiltViewModel<DetailsViewModel, DetailsViewModel.Factory>(
        creationCallback = { factory -> factory.create(product) }
    )
    DetailsContent(product = product, onBack = onBack, onAddToCart = {
        viewModel.addToCart()
    })
    LaunchedEffect(Unit) {
        viewModel.action.collect {
            when (it) {
                DetailsViewModel.DetailsAction.AddedToCart -> {
                    Toast.makeText(
                        context,
                        "Product was added successfully!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}

@Composable
fun DetailsContent(
    product: Product,
    onBack: () -> Unit = {},
    onAddToCart: () -> Unit = {}
) {
    val pagerState = rememberPagerState(pageCount = { 1 })
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
    ) {
        SparkShopToolbar(
            title = "Product Details",
            onNavIconClick = { onBack() },
            navIcon = R.drawable.ic_chevron_left
        )
        HorizontalPager(state = pagerState) {
            AsyncImage(
                model = product.image,
                contentDescription = product.title,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
                    .background(MaterialTheme.colorScheme.surfaceContainer)
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = MaterialTheme.colorScheme.error)
        ) {
            Column {
                Text(
                    text = "$${
                        NumberFormat.getInstance(Locale.getDefault()).format(product.price)
                    }",
                    style = MaterialTheme.typography.headlineLarge,
                    modifier = Modifier
                        .wrapContentWidth()
                        .padding(start = 16.dp, top = 8.dp),
                    color = MaterialTheme.colorScheme.onError
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = "Only ${product.stock} left",
                    style = MaterialTheme.typography.labelLarge,
                    modifier = Modifier
                        .wrapContentWidth()
                        .padding(start = 16.dp, bottom = 8.dp),
                    color = MaterialTheme.colorScheme.onError
                )
            }
            Text(
                text = "${product.percentageOff}% off",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onError,
                modifier = Modifier
                    .padding(end = 16.dp, top = 8.dp)
                    .fillMaxWidth(),
                textAlign = TextAlign.End
            )
        }
        Spacer(Modifier.height(16.dp))
        PagerIndicator(
            pageCount = 1,
            currentPage = pagerState.currentPage,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(16.dp))
        Text(
            product.title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 24.dp)
        )
        Spacer(Modifier.height(16.dp))
        Text(
            text = "Product Details",
            style = MaterialTheme.typography.titleSmall,
            modifier = Modifier.padding(horizontal = 24.dp)
        )
        Text(
            text = "Description: ${product.description}",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(horizontal = 24.dp)
        )
        Text(
            text = "Dimension: ${product.dimensions.width} x ${product.dimensions.height} x ${product.dimensions.length}",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(horizontal = 24.dp)
        )
        Spacer(Modifier.height(16.dp))
        Text(
            text = "Delivery and Returns info",
            style = MaterialTheme.typography.titleSmall,
            modifier = Modifier.padding(horizontal = 24.dp)
        )
        Text(
            text = "Shipping Information: ${product.shippingInformation}",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(horizontal = 24.dp)
        )
        Text(
            text = "Return Policy: ${product.returnPolicy}",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(horizontal = 24.dp)
        )
        Spacer(Modifier.height(8.dp))
        Text(
            if (product.stock > 0) "In Stock" else "Out of Stock",
            color = if (product.stock > 0) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(horizontal = 24.dp)
        )
        Spacer(Modifier.weight(1F))
        Button(
            onClick = onAddToCart,
            enabled = product.stock > 0,
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Add to Cart")
            Spacer(Modifier.width(24.dp))
            Icon(
                painter = painterResource(R.drawable.ic_add_shopping_cart),
                contentDescription = null
            )
        }
    }
}

@Preview
@Composable
fun DetailsContentPreview() {
    SparkShopTheme {
        DetailsContent(product = dummyProduct)
    }
}
