package com.segunfrancis.sparkshop.ui.checkout

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun CheckoutScreen(
    total: Int,
    selectedPayment: String = "Credit Card",
    onBack: () -> Unit = {},
    onPaymentChange: (String) -> Unit = {},
    onPlaceOrder: () -> Unit = {}
) {
    val paymentMethods = listOf("Credit Card")
    var payment by remember { mutableStateOf(selectedPayment) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(18.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBack) {
                Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
            }
            Spacer(Modifier.width(4.dp))
            Text("Checkout", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        }
        Spacer(Modifier.height(18.dp))
        Text("Payment", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        paymentMethods.forEach { method ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { payment = method; onPaymentChange(method) }
                    .padding(vertical = 8.dp)
            ) {
                RadioButton(
                    selected = payment == method,
                    onClick = { payment = method; onPaymentChange(method) }
                )
                Text(method, style = MaterialTheme.typography.bodyLarge)
            }
        }
        Spacer(Modifier.height(18.dp))
        Text("Order Summary", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Total", style = MaterialTheme.typography.bodyLarge)
            Text("$${"%.2f".format(total.toDouble())}", style = MaterialTheme.typography.bodyLarge)
        }
        Spacer(Modifier.weight(1f))
        Button(
            onClick = onPlaceOrder,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Place Order", style = MaterialTheme.typography.labelLarge)
        }
    }
}
