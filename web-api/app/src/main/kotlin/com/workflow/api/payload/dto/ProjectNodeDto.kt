package com.workflow.api.payload.dto

import com.fasterxml.jackson.annotation.JsonProperty
import com.workflow.api.utils.DTO
import java.io.Serializable
import java.time.Instant

/**
 * DTO for {@link com.workflow.api.data.model.Work}
 */
@DTO
class ProjectNodeDto(
    @JsonProperty("id")
    var id: Long? = null,

    @JsonProperty("parent_node_id")
    var parentNodeId: Long? = null,

    @JsonProperty("type")
    var typeTitle: String? = null,

    @JsonProperty("work_title")
    var workTitle: String? = null,

    @JsonProperty("product_title")
    var productTitle: String? = null,

    @JsonProperty("unit")
    var unitTitle: String? = null,

    @JsonProperty("amount")
    var amount: Int? = null,

    @JsonProperty("performer")
    var performerTitle: String? = null,

    @JsonProperty("expected_payment_date")
    var expectedPaymentDate: Instant? = null,

    @JsonProperty("payment_date")
    var paymentDate: Instant? = null,

    @JsonProperty("expected_delivery_date")
    var expectedDeliveryDate: Instant? = null,

    @JsonProperty("delivery_date")
    var deliveryDate: Instant? = null,

    @JsonProperty("expected_finish_date")
    var expectedFinishDate: Instant? = null,

    @JsonProperty("finish_date")
    var finishDate: Instant? = null
) : Serializable