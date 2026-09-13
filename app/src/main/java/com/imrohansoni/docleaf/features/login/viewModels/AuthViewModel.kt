package com.imrohansoni.docleaf.features.login.viewModels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.imrohansoni.docleaf.features.login.AuthRepository
//import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.auth.AuthState
import kotlinx.coroutines.launch


sealed class AuthState {
    data object Idle : AuthState()
    data object Loading : AuthState()
    data object Success : AuthState()
    data class Error(val message: String) : AuthState()
}

class AuthViewModel(
    private val repository: AuthRepository
) : ViewModel() {

    var uiState by mutableStateOf<AuthState>(
        AuthState.Idle
    )

    fun signIn() {
        viewModelScope.launch {
            uiState = AuthState.Loading

            repository.signInWithGoogle()
                .onSuccess {
                    uiState = AuthState.Success
                }
                .onFailure {
                    uiState = AuthState.Error(
                        it.message ?: "Unknown Error"
                    )
                }
        }
    }

    fun signOut() {
        repository.logout()
    }

    fun getCurrentUser() {
        repository.currentUser()
    }
}