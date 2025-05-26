package com.segunfrancis.sparkshop.ui.auth.login

import android.util.Patterns
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.segunfrancis.sparkshop.R
import com.segunfrancis.sparkshop.ui.components.AuthLoading
import com.segunfrancis.sparkshop.ui.theme.SparkShopTheme

@Composable
fun LoginScreen(
    onNavigateToRegister: () -> Unit = {},
    onLoginComplete: (String?) -> Unit = { _ -> }
) {
    val context = LocalContext.current
    val viewModel = hiltViewModel<LoginViewModel>()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    LaunchedEffect(Unit) {
        viewModel.events.collect {
            when (it) {
                is LoginViewModel.LoginEvents.OnLoginFailure -> {
                    Toast.makeText(context, it.error, Toast.LENGTH_LONG).show()
                }

                is LoginViewModel.LoginEvents.OnLoginSuccess -> {
                    onLoginComplete(it.displayName)
                }
            }
        }
    }

    LoginContent(
        onLoginInit = { email, password ->
            viewModel.signIn(email = email, password = password)
        }, onNavigateToRegister = onNavigateToRegister, isLoading = isLoading
    )
}

@Composable
fun LoginContent(
    isLoading: Boolean = false,
    onLoginInit: (email: String, password: String) -> Unit = { _, _ -> },
    onNavigateToRegister: () -> Unit = {}
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var emailError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }

    if (isLoading) {
        AuthLoading()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.app_name),
            style = MaterialTheme.typography.displayMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(32.dp))
        Text(text = "Login", style = MaterialTheme.typography.headlineLarge)
        Spacer(Modifier.height(32.dp))

        OutlinedTextField(
            value = email,
            onValueChange = {
                email = it
                emailError = null
            },
            label = { Text("Email") },
            singleLine = true,
            isError = emailError != null,
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            )
        )
        if (emailError != null) {
            Text(emailError ?: "", color = MaterialTheme.colorScheme.error)
        }

        Spacer(Modifier.height(18.dp))

        OutlinedTextField(
            value = password,
            onValueChange = {
                password = it
                passwordError = null
            },
            label = { Text("Password") },
            singleLine = true,
            isError = passwordError != null,
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                val image =
                    if (passwordVisible) R.drawable.ic_visibility else R.drawable.ic_visibility_off
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        painter = painterResource(image),
                        contentDescription = if (passwordVisible) "Hide password" else "Show password"
                    )
                }
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            )
        )
        if (passwordError != null) {
            Text(passwordError ?: "", color = MaterialTheme.colorScheme.error)
        }

        Spacer(Modifier.height(28.dp))
        Button(
            onClick = {
                emailError = if (email.isBlank()) "Email is required" else if (!email.matches(
                        Patterns.EMAIL_ADDRESS.toRegex()
                    )
                ) "Enter a valid email" else null
                passwordError =
                    if (password.length < 6) "Password must be at least 6 characters" else null
                if (emailError == null && passwordError == null) {
                    onLoginInit(email.trim(), password)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Login", style = MaterialTheme.typography.labelLarge)
        }

        Spacer(Modifier.height(16.dp))
        TextButton(onClick = onNavigateToRegister) {
            Text("Don't have an account? Register")
        }
    }
}

@Preview
@Composable
fun LoginScreenPreview() {
    SparkShopTheme {
        LoginContent()
    }
}
