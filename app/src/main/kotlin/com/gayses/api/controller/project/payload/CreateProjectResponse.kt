package com.gayses.api.controller.project.payload

import com.fasterxml.jackson.annotation.JsonProperty

data class CreateProjectResponse(
    @JsonProperty("is_success")
    val isSuccess: Boolean
)
