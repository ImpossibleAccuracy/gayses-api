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
    var finishDate: Instant? = null,

    @OneToOne(mappedBy = "work", fetch = FetchType.LAZY)
    val queueItem: WorkQueueItem? = null
) : BaseModel<Long>(id) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Work) return false
        if (!super.equals(other)) return false

        if (type != other.type) return false
        if (workTitle != other.workTitle) return false
        if (productTitle != other.productTitle) return false
        if (unit != other.unit) return false
        if (amount != other.amount) return false
        if (performer != other.performer) return false
        if (expectedPaymentDate != other.expectedPaymentDate) return false
        if (paymentDate != other.paymentDate) return false
        if (expectedDeliveryDate != other.expectedDeliveryDate) return false
        if (deliveryDate != other.deliveryDate) return false
        if (expectedFinishDate != other.expectedFinishDate) return false
        if (finishDate != other.finishDate) return false
        if (queueItem != other.queueItem) return false

        return true
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + type.hashCode()
        result = 31 * result + workTitle.hashCode()
        result = 31 * result + productTitle.hashCode()
        result = 31 * result + (unit?.hashCode() ?: 0)
        result = 31 * result + amount
        result = 31 * result + performer.hashCode()
        result = 31 * result + (expectedPaymentDate?.hashCode() ?: 0)
        result = 31 * result + (paymentDate?.hashCode() ?: 0)
        result = 31 * result + (expectedDeliveryDate?.hashCode() ?: 0)
        result = 31 * result + (deliveryDate?.hashCode() ?: 0)
        result = 31 * result + (expectedFinishDate?.hashCode() ?: 0)
        result = 31 * result + (finishDate?.hashCode() ?: 0)
        result = 31 * result + (queueItem?.hashCode() ?: 0)
        return result
    }

    override fun toString(): String {
        return "Work(id=$id, type=$type, workTitle='$workTitle', unit=$unit, amount=$amount)"
    }
}
