package com.security.jwt.service

import org.springframework.security.core.userdetails.UserDetails

interface TokenService {
    fun extractEmail(token: String): String?

    fun generateToken(user: UserDetails): String

    fun generateToken(user: UserDetails, extra: Map<String, Any?>?): String
}