package com.gayses.api.controller.auth

import com.gayses.api.controller.auth.payload.AuthResponse
import com.gayses.api.controller.auth.payload.LoginRequest
import com.gayses.api.controller.auth.payload.RegistrationRequest
import com.gayses.api.data.model.Account
import com.gayses.api.service.auth.AuthService
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/auth/")
@Validated
class AuthController(
    private val authService: AuthService,
    private val authManager: AuthenticationManager
) {
    @PostMapping("/signin/")
    fun login(@RequestBody data: LoginRequest): ResponseEntity<AuthResponse> =
        authService.login(data.email, data.password).let {
            authenticateInternal(it.account, data.password.trim())

            val response = AuthResponse(it.token)

            ResponseEntity.ok(response)
        }

    @PostMapping("/signup/")
    fun registration(@RequestBody data: RegistrationRequest): ResponseEntity<AuthResponse> =
        authService.registration(data.email, data.password).let {
            authenticateInternal(it.account, data.password.trim())

            val response = AuthResponse(it.token)

            ResponseEntity.ok(response)
        }

    private fun authenticateInternal(account: Account, password: String) {
        authManager.authenticate(
            UsernamePasswordAuthenticationToken(
                account.username,
                password
            )
        )
    }
}