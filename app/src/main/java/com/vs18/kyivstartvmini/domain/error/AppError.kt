package com.vs18.kyivstartvmini.domain.error

sealed class AppError {
    object Network : AppError()
    object NotFound : AppError()
    object Parse : AppError()
    data class Unknown(val message: String?) : AppError()
}