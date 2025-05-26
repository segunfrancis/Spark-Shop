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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.segunfrancis.sparkshop.R
import com.segunfrancis.sparkshop.ui.components.SparkShopToolbar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutScreen(onBack: () -> Unit) {
    var showAlertDialog by remember { mutableStateOf(false) }
    CheckoutContent(total = 12, onBack = onBack, onPlaceOrder = { showAlertDialog = true })
    if (showAlertDialog) {
        BasicAlertDialog(onDismissRequest = { showAlertDialog = false }) {
            Surface(
                modifier = Modifier
                    .wrapContentWidth()
                    .wrapContentHeight(),
                shape = MaterialTheme.shapes.large,
                tonalElevation = AlertDialogDefaults.TonalElevation
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = "Congratulations!🥳", style = MaterialTheme.typography.titleLarge)
                    Spacer(Modifier.height(16.dp))
                    Text(text = "Your order has been placed successfully.\nYou will be contacted when it is ready for delivery")
                    Spacer(Modifier.height(24.dp))
                    TextButton(
                        onClick = { showAlertDialog = false },
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        Text(text = "Okay")
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun CheckoutContent(
    total: Int = 12,
    onBack: () -> Unit = {},
    onPlaceOrder: () -> Unit = {}
) {
    val paymentMethods = listOf("Credit Card", "Debit Card")
    var payment by remember { mutableStateOf(paymentMethods.first()) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
    ) {
        SparkShopToolbar(
            title = "Checkout",
            navIcon = R.drawable.ic_chevron_left,
            onNavIconClick = onBack
        )
        Spacer(Modifier.height(18.dp))
        Text(
            "Payment",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 24.dp)
        )
        paymentMethods.forEach { method ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { payment = method }
                    .padding(vertical = 8.dp, horizontal = 24.dp)
            ) {
                RadioButton(
                    selected = payment == method,
                    onClick = { payment = method }
                )
                Text(method, style = MaterialTheme.typography.bodyLarge)
            }
        }
        Spacer(Modifier.height(18.dp))
        Text(
            text = "Order Summary",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 24.dp)
        )
        Spacer(Modifier.height(8.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Total", style = MaterialTheme.typography.bodyLarge)
            Text("$${"%.2f".format(total.toDouble())}", style = MaterialTheme.typography.bodyLarge)
        }
        Spacer(Modifier.weight(1f))
        Button(
            onClick = onPlaceOrder,
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Place Order", style = MaterialTheme.typography.labelLarge)
        }
    }
}
