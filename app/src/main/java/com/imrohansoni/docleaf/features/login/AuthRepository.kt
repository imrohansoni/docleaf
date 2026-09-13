package com.imrohansoni.docleaf.features.login

import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.tasks.await


class AuthRepository(
    private val context: Context
) {

    val WEB_CLIENT_ID = "271277923173-1u8ltho2persri888ntm9e82f8u3s250.apps.googleusercontent.com"

    private val auth = FirebaseAuth.getInstance()

    suspend fun signInWithGoogle(): Result<Unit> {

        return try {

            val googleIdOption =
                GetGoogleIdOption.Builder()
                    .setFilterByAuthorizedAccounts(false)
                    .setServerClientId(
                        WEB_CLIENT_ID
                    )
                    .build()

            val request =
                GetCredentialRequest.Builder()
                    .addCredentialOption(googleIdOption)
                    .build()

            val credentialManager =
                CredentialManager.create(context)

            val result =
                credentialManager.getCredential(
                    context,
                    request
                )

            val credential = result.credential

            val googleCredential =
                GoogleIdTokenCredential.createFrom(
                    credential.data
                )

            val idToken =
                googleCredential.idToken

            val firebaseCredential =
                GoogleAuthProvider.getCredential(
                    idToken,
                    null
                )

            auth.signInWithCredential(
                firebaseCredential
            ).await()

            Result.success(Unit)

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun logout() {
        auth.signOut()
    }

    fun currentUser() = auth.currentUser
}