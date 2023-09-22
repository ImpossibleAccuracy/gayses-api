package com.gayses.api.data.model

import com.gayses.api.data.model.base.BaseModel
import jakarta.persistence.*
import java.time.Instant

@Entity
@Table(name = "work")
class Work(
    id: Long?,

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "TypeId", nullable = false)
    var type: WorkType,

    @Column(name = "WorkTitle", nullable = false, length = 1024)
    var workTitle: String,

    @Column(name = "ProductTitle", nullable = false, length = 1024)
    var productTitle: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UnitId")
    var unit: Unit? = null,

    @Column(name = "Amount", nullable = false)
    var amount: Int,

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "PerformerId", nullable = false)
    var performer: Performer,

    @Column(name = "ExpectedPaymentDate")
    var expectedPaymentDate: Instant? = null,

    @Column(name = "PaymentDate")
    var paymentDate: Instant? = null,

    @Column(name = "ExpectedDeliveryDate")
    var expectedDeliveryDate: Instant? = null,

    @Column(name = "DeliveryDate")
    var deliveryDate: Instant? = null,

    @Column(name = "ExpectedFinishDate")
    var expectedFinishDate: Instant? = null,

    @Column(name = "FinishDate")
    var finishDate: Instant? = null
) : BaseModel<Long>(id)
