package com.vs18.kyivstartvmini.viewmodel.auth

import androidx.lifecycle.*
import com.vs18.kyivstartvmini.datastore.AuthPreferences
import com.vs18.kyivstartvmini.data.repository.auth.AuthRepository

class AuthViewModelFactory(
    private val repository: AuthRepository,
    private val prefs: AuthPreferences
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AuthViewModel(repository, prefs) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}