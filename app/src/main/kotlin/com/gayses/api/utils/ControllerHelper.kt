package com.gayses.api.utils

import com.gayses.api.data.model.Account
import org.springframework.security.core.context.SecurityContextHolder

object ControllerHelper {
    val account: Account
        get() = SecurityContextHolder.getContext().authentication.principal as Account
}