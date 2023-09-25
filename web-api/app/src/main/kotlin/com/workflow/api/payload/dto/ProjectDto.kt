package com.workflow.api.payload.dto

import com.fasterxml.jackson.annotation.JsonProperty
import com.workflow.api.utils.DTO
import java.io.Serializable

/**
 * DTO for {@link com.workflow.api.data.model.Project}
 */
@DTO
class ProjectDto(
    @JsonProperty("id")
    var id: Long? = null,

    @JsonProperty("title")
    var title: String? = null
) : Serializable