package com.segunfrancis.sparkshop.ui.auth.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.segunfrancis.sparkshop.utils.formatEmailUsername
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val firebaseAuth: FirebaseAuth) : ViewModel() {

    private val _events: MutableSharedFlow<LoginEvents> =
        MutableSharedFlow(onBufferOverflow = BufferOverflow.DROP_OLDEST, extraBufferCapacity = 1)
    val events: SharedFlow<LoginEvents> = _events.asSharedFlow()

    val isLoading = MutableStateFlow(false)

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        isLoading.update { false }
        throwable.printStackTrace()
        _events.tryEmit(LoginEvents.OnLoginFailure(throwable.localizedMessage))
    }

    fun signIn(email: String, password: String) {
        viewModelScope.launch(exceptionHandler) {
            isLoading.update { true }
            firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    isLoading.update { false }
                    _events.tryEmit(LoginEvents.OnLoginSuccess(formatEmailUsername(task.result.user?.email)))
                } else {
                    isLoading.update { false }
                    _events.tryEmit(LoginEvents.OnLoginFailure(task.exception?.localizedMessage))
                }
            }
        }
    }

    sealed interface LoginEvents {
        data class OnLoginSuccess(val displayName: String?) : LoginEvents
        data class OnLoginFailure(val error: String?) : LoginEvents
    }
}
