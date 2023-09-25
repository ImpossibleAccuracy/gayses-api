package com.gayses.api.service.auth

import com.gayses.api.data.model.Account
import com.gayses.api.data.repository.AccountRepository
import com.gayses.api.exception.InvalidServiceArguments
import com.gayses.api.exception.OperationDeniedException
import com.gayses.api.exception.OperationRejectedException
import com.gayses.api.service.auth.AuthService.AuthResult
import com.security.jwt.service.TokenService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class AuthServiceImpl(
    private val tokenService: TokenService,
    private val accountRepository: AccountRepository,
    private val passwordEncoder: PasswordEncoder
) : AuthService {
    override fun login(email: String, password: String): AuthResult {
        if (email.isBlank()) {
            throw InvalidServiceArguments("Email cannot be blank")
        }

        if (password.isBlank()) {
            throw InvalidServiceArguments("Password cannot be blank")
        }

        val validEmail = email.trim()
        val validPassword = password.trim()

        val account = accountRepository.findByEmailIgnoreCase(validEmail)
            .orElseThrow {
                OperationRejectedException("User with such credentials not found")
            }

        if (!passwordEncoder.matches(validPassword, account.passwordHash)) {
            throw OperationRejectedException("User with such credentials not found")
        }

        return AuthResult(
            account,
            tokenService.generateToken(account)
        )
    }

    override fun registration(email: String, password: String): AuthResult {
        if (email.isBlank()) {
            throw InvalidServiceArguments("Email cannot be blank")
        }

        if (password.isBlank()) {
            throw InvalidServiceArguments("Password cannot be blank")
        }

        val validEmail = email.trim()
        val validPassword = password.trim()

        if (accountRepository.existsByEmailIgnoreCase(validEmail)) {
            throw OperationDeniedException("User with such credentials already exists")
        }

        val passwordHash = passwordEncoder.encode(validPassword)

        val account = Account(
            null,
            validEmail,
            passwordHash
        ).let {
            accountRepository.save(it)
        }

        return AuthResult(
            account,
            tokenService.generateToken(account)
        )
    }
}