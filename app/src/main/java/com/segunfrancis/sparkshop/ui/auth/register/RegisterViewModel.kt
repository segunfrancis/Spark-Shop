package com.segunfrancis.sparkshop.ui.auth.register

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
class RegisterViewModel @Inject constructor(private val auth: FirebaseAuth) : ViewModel() {

    private val _events: MutableSharedFlow<RegisterEvents> =
        MutableSharedFlow(onBufferOverflow = BufferOverflow.DROP_OLDEST, extraBufferCapacity = 1)
    val events: SharedFlow<RegisterEvents> = _events.asSharedFlow()

    val isLoading = MutableStateFlow(false)

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        isLoading.update { false }
        throwable.printStackTrace()
        _events.tryEmit(RegisterEvents.OnRegisterFailure(throwable.localizedMessage))
    }

    fun register(email: String, password: String) {
        viewModelScope.launch(exceptionHandler) {
            isLoading.update { true }
            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    isLoading.update { false }
                    _events.tryEmit(RegisterEvents.OnRegisterSuccess(formatEmailUsername(task.result.user?.email)))
                } else {
                    isLoading.update { false }
                    _events.tryEmit(RegisterEvents.OnRegisterFailure(task.exception?.localizedMessage))
                }
            }
        }
    }

    sealed interface RegisterEvents {
        data class OnRegisterSuccess(val displayName: String?) : RegisterEvents
        data class OnRegisterFailure(val error: String?) : RegisterEvents
    }
}
