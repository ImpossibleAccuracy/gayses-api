package com.gayses.api.service.user

import com.gayses.api.data.repository.AccountRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class UserDetailsServiceImpl(
    private val accountRepository: AccountRepository
) : UserDetailsService {
    @Throws(UsernameNotFoundException::class)
    override fun loadUserByUsername(username: String): UserDetails {
        return accountRepository.findByEmail(username).orElseThrow {
            UsernameNotFoundException("Account with email: \"$username\" not found")
        }
    }
}