package com.example.filmfolio.presentation.util

import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import com.example.filmfolio.R
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.tasks.await
import java.security.MessageDigest
import java.util.UUID

class GoogleAuthClient(
    private val auth: FirebaseAuth
) {
    suspend fun signIn(context: Context): AuthResult? {
        val credentialManager = CredentialManager.create(context)

        val rawNonce = UUID.randomUUID().toString()
        val bytes = rawNonce.toByteArray()
        val md = MessageDigest.getInstance("SHA-256")
        val digest = md.digest(bytes)
        val hashedNonce = digest.fold("") { str, it -> str + "%02x".format(it) }

        val googleIdOption: GetGoogleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId("1062868257106-rc9bp9jnl12rklu8amtffb8r27hl109p.apps.googleusercontent.com")
            .setNonce(hashedNonce)
            .build()

        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        return try {
            val result = credentialManager.getCredential(context, request)
            handleSignIn(result)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private suspend fun handleSignIn(result: GetCredentialResponse): AuthResult? {
        val credential = result.credential
        val googleIdTokenCredential = com.google.android.libraries.identity.googleid.GoogleIdTokenCredential.createFrom(credential.data)
        val googleIdToken = googleIdTokenCredential.idToken
        val firebaseCredential = GoogleAuthProvider.getCredential(googleIdToken, null)
        return auth.signInWithCredential(firebaseCredential).await()
    }

    fun getSignedInUser() = auth.currentUser

    suspend fun signOut() {
        auth.signOut()
    }
}
