package com.gayses.api.payload.dto

import com.gayses.api.utils.DTO
import java.io.Serializable
import java.time.Instant

/**
 * DTO for {@link com.gayses.api.data.model.Work}
 */
@DTO
class WorkDto(
    var id: Long? = null,
    var typeTitle: String? = null,
    var workTitle: String? = null,
    var productTitle: String? = null,
    var unitTitle: String? = null,
    var amount: Int? = null,
    var performerTitle: String? = null,
    var expectedPaymentDate: Instant? = null,
    var paymentDate: Instant? = null,
    var expectedDeliveryDate: Instant? = null,
    var deliveryDate: Instant? = null,
    var expectedFinishDate: Instant? = null,
    var finishDate: Instant? = null
) : Serializable