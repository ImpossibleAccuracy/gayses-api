package com.gayses.api.service.auth

import com.gayses.api.data.model.Account
import com.gayses.api.exception.InvalidServiceArguments
import com.gayses.api.exception.OperationDeniedException
import com.gayses.api.exception.OperationRejectedException

interface AuthService {
    @Throws(InvalidServiceArguments::class, OperationRejectedException::class)
    fun login(email: String, password: String): AuthResult

    @Throws(InvalidServiceArguments::class, OperationDeniedException::class)
    fun registration(email: String, password: String): AuthResult

    data class AuthResult(
        val account: Account,
        val token: String
    )
}