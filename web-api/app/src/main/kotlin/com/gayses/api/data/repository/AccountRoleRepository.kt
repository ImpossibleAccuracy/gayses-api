package com.gayses.api.data.repository

import com.gayses.api.data.model.AccountRole
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface AccountRoleRepository : JpaRepository<AccountRole, Long>