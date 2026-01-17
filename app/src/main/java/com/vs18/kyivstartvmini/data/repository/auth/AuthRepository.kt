package com.vs18.kyivstartvmini.data.repository.auth

import com.vs18.kyivstartvmini.data.api.auth.FirebaseAuthApi
import com.vs18.kyivstartvmini.data.api.auth.FirebaseAuthModule
import com.vs18.kyivstartvmini.data.model.auth.AuthRequest
import com.vs18.kyivstartvmini.data.remote.ForgotPasswordRequest

class AuthRepository(
    private val api: FirebaseAuthApi
) {

    suspend fun login(email: String, password: String) =
        runCatching {
            FirebaseAuthModule.api.login(
                FirebaseAuthModule.apiKey(),
                AuthRequest(
                    email = email,
                    password = password
                )
            )
        }

    suspend fun register(email: String, password: String) =
        runCatching {
            FirebaseAuthModule.api.register(
                FirebaseAuthModule.apiKey(),
                AuthRequest(
                    email = email,
                    password = password
                )
            )
        }

    suspend fun forgotPassword(email: String): Result<String> {
        return try {
            val response = api.forgotPassword(
                ForgotPasswordRequest(email)
            )
            Result.success(response.message)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}