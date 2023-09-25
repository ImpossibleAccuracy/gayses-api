package com.workflow.api.utils

import com.workflow.api.data.model.Account
import org.springframework.http.HttpStatus
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.server.ResponseStatusException

object ControllerHelper {
    val account: Account
        get() = SecurityContextHolder.getContext().authentication.principal.let {
            if (it !is Account) {
                throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "You are not authorized")
            }

            it
        }
}