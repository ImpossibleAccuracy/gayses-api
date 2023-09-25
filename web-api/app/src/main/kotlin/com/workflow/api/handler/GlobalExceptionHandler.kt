package com.workflow.api.handler

import com.workflow.api.exception.*
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import java.time.Instant

@ControllerAdvice
class GlobalExceptionHandler : ResponseEntityExceptionHandler() {
    @ExceptionHandler(ResourceNotFoundException::class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    fun handleResourceResourceNotFound(e: ResourceNotFoundException) =
        ErrorResponse(
            Instant.now(),
            HttpStatus.NOT_FOUND.ordinal,
            e.message ?: "Not found",
            e.javaClass.name
        )

    @ExceptionHandler(InvalidServiceArguments::class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    fun handleInvalidServiceArgumentsException(e: InvalidServiceArguments) =
        ErrorResponse(
            Instant.now(),
            HttpStatus.BAD_REQUEST.ordinal,
            e.message ?: "Invalid data",
            e.javaClass.name
        )

    @ExceptionHandler(OperationRejectedException::class)
    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    fun handleOperationRejectedException(e: OperationRejectedException) =
        ErrorResponse(
            Instant.now(),
            HttpStatus.UNAUTHORIZED.ordinal,
            e.message ?: "Operation rejected",
            e.javaClass.name
        )

    @ExceptionHandler(ResourceAccessDeniedException::class)
    @ResponseStatus(value = HttpStatus.FORBIDDEN)
    fun handleResourceAccessDeniedException(e: ResourceAccessDeniedException) =
        ErrorResponse(
            Instant.now(),
            HttpStatus.FORBIDDEN.ordinal,
            e.message ?: "Resource access denied",
            e.javaClass.name
        )

    @ExceptionHandler(OperationDeniedException::class)
    @ResponseStatus(value = HttpStatus.FORBIDDEN)
    fun handleOperationDeniedException(e: OperationDeniedException) =
        ErrorResponse(
            Instant.now(),
            HttpStatus.FORBIDDEN.ordinal,
            e.message ?: "Resource access denied",
            e.javaClass.name
        )

    data class ErrorResponse(
        val timestamp: Instant = Instant.now(),
        val status: Int,
        val error: String,
        val exception: String
    )
}