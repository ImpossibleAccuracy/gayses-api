package com.gayses.api.data.repository;

import com.gayses.api.data.model.Account
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface AccountRepository : JpaRepository<Account, Long> {
    fun findByEmail(email: String): Optional<Account>


    fun findByEmailIgnoreCase(email: String): Optional<Account>


    fun existsByEmailIgnoreCase(email: String): Boolean
}