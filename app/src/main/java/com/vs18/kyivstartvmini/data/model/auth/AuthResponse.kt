package com.vs18.kyivstartvmini.data.model.auth

data class AuthResponse(
    val idToken: String,
    val email: String,
    val refreshToken: String,
    val expiresIn: String,
    val localId: String
)