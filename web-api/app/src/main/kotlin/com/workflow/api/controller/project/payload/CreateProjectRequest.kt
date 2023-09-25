package com.workflow.api.controller.project.payload

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

data class CreateProjectRequest(
    @NotNull
    @NotBlank
    @JsonProperty("title")
    val title: String
)
