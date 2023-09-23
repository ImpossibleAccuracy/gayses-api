package com.gayses.api.payload.dto

import com.gayses.api.utils.DTO
import java.io.Serializable

/**
 * DTO for {@link com.gayses.api.data.model.WorkQueueItem}
 */
@DTO
class WorkQueueItemDto(
    var work: WorkDto? = null,
    var order: Int? = null
) : Serializable