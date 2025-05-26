package com.segunfrancis.sparkshop.ui.auth.register

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
fun RegisterScreen(
    onRegisterComplete: (String?) -> Unit = { _ -> },
    onNavigateToLogin: () -> Unit = {}
) {
    val context = LocalContext.current
    val viewModel = hiltViewModel<RegisterViewModel>()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()

    RegisterContent(
        isLoading = isLoading,
        onNavigateToLogin = onNavigateToLogin,
        onRegisterInit = { email, password ->
            viewModel.register(email = email, password = password)
        })

    LaunchedEffect(Unit) {
        viewModel.events.collect {
            when (it) {
                is RegisterViewModel.RegisterEvents.OnRegisterFailure -> {
                    Toast.makeText(context, it.error, Toast.LENGTH_LONG).show()
                }

                is RegisterViewModel.RegisterEvents.OnRegisterSuccess -> {
                    onRegisterComplete(it.displayName)
                }
            }
        }
    }
}

@Composable
fun RegisterContent(
    isLoading: Boolean,
    onNavigateToLogin: () -> Unit = {},
    onRegisterInit: (email: String, password: String) -> Unit
) {

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }
    var emailError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }
    var confirmPasswordError by remember { mutableStateOf<String?>(null) }

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
        Text(text = "Register", style = MaterialTheme.typography.headlineLarge)
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
                imeAction = ImeAction.Next
            )
        )
        if (passwordError != null) {
            Text(passwordError ?: "", color = MaterialTheme.colorScheme.error)
        }

        Spacer(Modifier.height(18.dp))

        OutlinedTextField(
            value = confirmPassword,
            onValueChange = {
                confirmPassword = it
                confirmPasswordError = null
            },
            label = { Text("Confirm Password") },
            singleLine = true,
            isError = confirmPasswordError != null,
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                val image =
                    if (confirmPasswordVisible) R.drawable.ic_visibility else R.drawable.ic_visibility_off
                IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                    Icon(
                        painter = painterResource(image),
                        contentDescription = if (confirmPasswordVisible) "Hide password" else "Show password"
                    )
                }
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            )
        )
        if (confirmPasswordError != null) {
            Text(confirmPasswordError ?: "", color = MaterialTheme.colorScheme.error)
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
                confirmPasswordError =
                    if (confirmPassword != password) "Passwords do not match" else null
                if (emailError == null && passwordError == null && confirmPasswordError == null) {
                    onRegisterInit(email.trim(), password)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Register", style = MaterialTheme.typography.labelLarge)
        }

        Spacer(Modifier.height(16.dp))
        TextButton(onClick = onNavigateToLogin) {
            Text("Already have an account? Login")
        }
    }
}

@Preview
@Composable
fun RegisterScreenPreview() {
    SparkShopTheme {
        RegisterContent(isLoading = true, onRegisterInit = { _, _ -> })
    }
}
