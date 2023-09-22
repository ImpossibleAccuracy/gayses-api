package com.gayses.api.service.auth

import com.gayses.api.data.model.Account
import com.gayses.api.exception.OperationRejectedException

interface AuthService {
    @Throws(OperationRejectedException::class)
    fun login(email: String, password: String): AuthResult

    @Throws(OperationRejectedException::class)
    fun registration(email: String, password: String): AuthResult

    data class AuthResult(
        val account: Account,
        val token: String
    )
}