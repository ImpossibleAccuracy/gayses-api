package com.gayses.api.payload.dto

import com.gayses.api.utils.DTO
import java.io.Serializable

/**
 * DTO for {@link com.gayses.api.data.model.Project}
 */
@DTO
class ProjectDto(
    var id: Long? = null,
    var title: String? = null
) : Serializable