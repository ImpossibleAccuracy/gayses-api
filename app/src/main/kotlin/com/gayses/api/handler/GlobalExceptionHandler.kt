package com.gayses.api.handler

import com.gayses.api.exception.OperationRejectedException
import com.gayses.api.exception.ResourceAccessDeniedException
import com.gayses.api.exception.ResourceNotFoundException
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

@ControllerAdvice
class GlobalExceptionHandler : ResponseEntityExceptionHandler() {
    @ExceptionHandler(ResourceNotFoundException::class)
    fun handleResourceResourceNotFound(e: ResourceNotFoundException, request: WebRequest) {
        handleExceptionInternal(e, e.message ?: "Not found", HttpHeaders(), HttpStatus.NOT_FOUND, request)
    }

    @ExceptionHandler(OperationRejectedException::class)
    fun handleResourceOperationRejected(e: OperationRejectedException, request: WebRequest) {
        handleExceptionInternal(e, e.message ?: "Operation rejected", HttpHeaders(), HttpStatus.FORBIDDEN, request)
    }

    @ExceptionHandler(ResourceAccessDeniedException::class)
    fun handleResourceAccessDeniedException(e: ResourceAccessDeniedException, request: WebRequest) {
        handleExceptionInternal(e, e.message ?: "Resource access denied", HttpHeaders(), HttpStatus.FORBIDDEN, request)
    }
}