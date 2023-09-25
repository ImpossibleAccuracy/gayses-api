package com.workflow.api.service.auth

import com.workflow.api.data.model.Account
import com.workflow.api.exception.InvalidServiceArguments
import com.workflow.api.exception.OperationDeniedException
import com.workflow.api.exception.OperationRejectedException

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