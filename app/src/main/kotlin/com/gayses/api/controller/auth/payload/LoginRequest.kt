package com.gayses.api.controller.auth.payload

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

data class LoginRequest(
    @NotNull
    @NotBlank
    @JsonProperty("email")
    val email: String,

    @NotNull
    @NotBlank
    @JsonProperty("password")
    val password: String
)
