package com.workflow.api.controller.node.payload

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import java.util.*

data class CreateNodeRequest(
    @JsonProperty("parent_node_id")
    val parentNodeId: Long?,

    @NotNull
    @NotBlank
    @JsonProperty("type")
    val type: String,

    @NotNull
    @NotBlank
    @JsonProperty("work_title")
    val workTitle: String,

    @NotNull
    @NotBlank
    @JsonProperty("product_title")
    val productTitle: String,

    @NotBlank
    @JsonProperty("unit")
    val unit: String?,

    @JsonProperty("amount")
    val amount: Int,

    @NotNull
    @NotBlank
    @JsonProperty("performer")
    val performer: String,

    @JsonProperty("expected_payment_date")
    val expectedPaymentDate: Date?,

    @JsonProperty("payment_date")
    val paymentDate: Date?,

    @JsonProperty("expected_delivery_date")
    val expectedDeliveryDate: Date?,

    @JsonProperty("delivery_date")
    val deliveryDate: Date?,

    @JsonProperty("expected_finish_date")
    val expectedFinishDate: Date?,

    @JsonProperty("finish_date")
    val finishDate: Date?
)
