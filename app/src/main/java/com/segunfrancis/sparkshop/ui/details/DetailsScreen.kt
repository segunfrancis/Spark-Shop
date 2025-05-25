package com.segunfrancis.sparkshop.ui.details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.segunfrancis.sparkshop.R
import com.segunfrancis.sparkshop.data.remote.Product
import com.segunfrancis.sparkshop.ui.components.SparkShopToolbar
import com.segunfrancis.sparkshop.utils.dummyProduct

@Composable
fun DetailsScreen(
    onBack: () -> Unit = {},
    onAddToCart: () -> Unit = {}
) {

}

@Composable
fun DetailsContent(
    product: Product,
    onBack: () -> Unit = {},
    onAddToCart: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        SparkShopToolbar(
            title = "Product Details",
            onNavIconClick = { onBack() },
            navIcon = R.drawable.ic_chevron_left
        )
        Spacer(Modifier.height(18.dp))
        AsyncImage(
            model = product.images.first(),
            contentDescription = product.title,
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .background(Color.LightGray)
        )
        Spacer(Modifier.height(18.dp))
        Text(
            product.title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 24.dp)
        )
        Spacer(Modifier.height(6.dp))
        Text(
            product.description,
            style = MaterialTheme.typography.bodyMedium,
            maxLines = 3,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(horizontal = 24.dp)
        )
        Spacer(Modifier.height(8.dp))
        Text(
            if (product.stock > 0) "In Stock" else "Out of Stock",
            color = if (product.stock > 0) Color(0xFF1BBF4B) else Color.Red,
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
            Text("Add to Cart", style = MaterialTheme.typography.labelLarge)
        }
    }
}

@Preview
@Composable
fun DetailsContentPreview() {
    DetailsContent(product = dummyProduct)
}
